package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.exception.CustomException;
import com.example.dbms.repository.ProjectRepository;
import com.example.dbms.repository.ProjectVisitorsRepository;
import com.example.dbms.repository.*;
import com.example.dbms.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth/client")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectVisitorsRepository projectVisitorsRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private ProjectEquipRequiredRepository projectEquipRequiredRepository;
    @Autowired
    private ProjectMaterialReqRepository projectMaterialReqRepository;
    @Autowired
    private ProjectAdminRepository projectAdminRepository;
    @Autowired
    private ProjectVisitorsRepository projectVisitorRepository;
//    @Autowired
//    private MaterialAvailableRepository materialDetailsRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private MaterialAvailableRepository materialAvailableRepository;
    @Autowired
    private ProjectWorkersRepository projectWorkerRepository;

    @GetMapping("/")
    public String client() {
        return "Welcome to client Profile";
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getMyProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            Map<String, Object> clientInfo = clientService.getClientInfo(authHeader);
            Integer clientId = (Integer) clientInfo.get("id");

            List<Project> projects = projectRepository.findBySoldTo(clientId);
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            throw new CustomException("ERROR!");
        }
    }


    @GetMapping("/visitors")
    public ResponseEntity<List<Visitor>> getAllVisitors(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            // Fetch client info from the authorization header
            Map<String, Object> clientInfo = clientService.getClientInfo(authHeader);
            Integer clientId = (Integer) clientInfo.get("id");

            // Optionally, you can add logic to ensure the client has permission to view the visitor data

            // Retrieve all visitors from the repository
            List<Visitor> visitors = visitorRepository.findAll();
            if (visitors.isEmpty()) {
                return ResponseEntity.noContent().build();  // If no visitors, return HTTP 204 (No Content)
            }

            return ResponseEntity.ok(visitors);  // Return visitors with HTTP 200 (OK)
        } catch (Exception e) {
            // Handle any errors that occur during the retrieval
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);  // Return HTTP 500 (Internal Server Error)
        }
    }


    //    add here according to remaining code
    @GetMapping("/project/details/{projectId}")
    public ResponseEntity<Map<String, Object>> getProjectDetailsById(@PathVariable Integer projectId) {
        try {
            // Retrieve basic project information
            Optional<Project> projectOpt = projectRepository.findById(projectId);
            if (projectOpt.isEmpty()) {
                return new ResponseEntity<>(Map.of("error", "Project not found"), HttpStatus.NOT_FOUND);
            }
            Project project = projectOpt.get();

            // Fetch associated data
            List<ProjectVisitors> visitors = projectVisitorsRepository.findByProjectId(projectId);
            List<Worker> workers = projectWorkerRepository.findWorkersByProjectId(projectId);
            Optional<ProjectAdmin> admin = projectAdminRepository.findById(projectId);
            List<Map<String, Object>> materials = projectMaterialReqRepository.findMaterialsAndQuantitiesByProjectId(projectId);
            List<Map<String, Object>> equipment = projectEquipRequiredRepository.findEquipmentAndQuantitiesByProjectId(projectId);

            // Build the response map, ensuring empty lists if no data found
            Map<String, Object> response = new HashMap<>();
            response.put("project", project);
            response.put("visitors", visitors.isEmpty() ? List.of() : visitors); // If no visitors, return empty list
            response.put("workers", workers.isEmpty() ? List.of() : workers); // If no workers, return empty list
            response.put("admin", admin.orElse(null)); // If no admin, return null
            response.put("materials", materials.isEmpty() ? List.of() : materials); // If no materials, return empty list
            response.put("equipment", equipment.isEmpty() ? List.of() : equipment); // If no equipment, return empty list

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Unable to fetch project details"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/projects/{id}/visitors")
    public ResponseEntity<List<Visitor>> getVisitorsByProjectId(@PathVariable Integer id, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            Map<String, Object> clientInfo = clientService.getClientInfo(authHeader);
            Integer clientId = (Integer) clientInfo.get("id");

            Project project = projectRepository.findById(id).orElse(null);
            if (project == null || !project.getSoldTo().equals(clientId)) {
                return ResponseEntity.badRequest().body(List.of());
            }

            List<Visitor> visitors = clientService.getVisitorsByProjectId(id);
            return ResponseEntity.ok(visitors);
        } catch (Exception e) {

            throw new CustomException("ERROR!");
        }
    }

    @PostMapping("/addprojectvisitor")
    public ResponseEntity<ProjectVisitors> addProjectVisitor(@RequestBody ProjectVisitors projectVisitor, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            Map<String, Object> clientInfo = clientService.getClientInfo(authHeader);
            Integer clientId = (Integer) clientInfo.get("id");

            Project project = projectRepository.findById(projectVisitor.getProjectId()).orElse(null);
            if (project == null || !project.getSoldTo().equals(clientId)) {
                return ResponseEntity.badRequest().body(null);
            }

            ProjectVisitors savedProjectVisitor = projectVisitorsRepository.save(projectVisitor);
            return new ResponseEntity<>(savedProjectVisitor, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new CustomException("ERROR!");
        }
    }

    @PostMapping("/deleteprojectvisitor")
    public ResponseEntity<Map<String, String>> deleteProjectVisitor(@RequestBody ProjectVisitors projectVisitor, @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            // Extract client information from the authorization header
            Map<String, Object> clientInfo = clientService.getClientInfo(authHeader);
            Integer clientId = (Integer) clientInfo.get("id");

            // Retrieve the project to confirm ownership
            Project project = projectRepository.findById(projectVisitor.getProjectId()).orElse(null);
            if (project == null || !project.getSoldTo().equals(clientId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project does not belong to the client"));
            }

            // Check if the visitor exists in the project
            ProjectVisitors existingProjectVisitor = projectVisitorsRepository.findByProjectIdAndVisitorId(
                    projectVisitor.getProjectId(), projectVisitor.getVisitorId());

            if (existingProjectVisitor != null) {
                // Delete the visitor from the project
                projectVisitorsRepository.deleteByProjectIdAndVisitorId(projectVisitor.getProjectId(), projectVisitor.getVisitorId());
                return ResponseEntity.ok(Map.of("message", "Visitor removed from project successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Visitor not found for this project"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Unable to delete visitor from project"));
        }
    }

}
