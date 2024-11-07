package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.exception.CustomException;
import com.example.dbms.repository.*;
import com.example.dbms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/admin")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectEquipRequiredRepository projectEquipRequiredRepository;
    @Autowired
    private ProjectMaterialReqRepository projectMaterialReqRepository;
    @Autowired
    private ProjectAdminRepository projectAdminRepository;
    @Autowired
    private ProjectVisitorsRepository projectVisitorRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private MaterialAvailableRepository materialAvailableRepository;
    @Autowired
    private ProjectWorkersRepository projectWorkerRepository;
    @PostMapping("/addproject")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new CustomException("ERROR!");
//          return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> showProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteProject")
    public ResponseEntity<Void> deleteProject(@RequestBody Map<String, Integer> request) {
        Integer projectId = request.get("id");
        try {
            if (projectId != null && projectRepository.existsById(projectId)) {
                projectRepository.deleteById(projectId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content on successful deletion
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 Not Found if project doesn't exist
            }
        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            throw new CustomException("ERROR!");
        }
    }

    @PostMapping("/addprojectequiprequired")
    public ResponseEntity<ProjectEquipRequired> addProjectEquipRequired(@RequestBody ProjectEquipRequired projectEquipRequired) {
        try {
            ProjectEquipRequired savedProjectEquipRequired = projectEquipRequiredRepository.save(projectEquipRequired);
            return new ResponseEntity<>(savedProjectEquipRequired, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project equipment requirement: ", e);
            throw new CustomException("ERROR!");
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addprojectvisitor")
    public ResponseEntity<ProjectVisitors> addProjectVisitor(@RequestBody ProjectVisitors projectVisitor) {
        try {
            ProjectVisitors savedProjectVisitor = projectVisitorRepository.save(projectVisitor);
            return new ResponseEntity<>(savedProjectVisitor, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project visitor: ", e);
            throw new CustomException("ERROR!");
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteprojectvisitor")
    public ResponseEntity<Void> deleteProjectVisitor(@RequestBody Map<String, Integer> request) {
        Integer projectId = request.get("projectId");
        Integer visitorId = request.get("visitorId");

        try {
            // Find the ProjectVisitors entry based on projectId and visitorId
            ProjectVisitors projectVisitor = projectVisitorRepository.findByProjectIdAndVisitorId(projectId, visitorId);

            if (projectVisitor != null) {
                // Delete by the found project's ID
                projectVisitorRepository.deleteById(projectVisitor.getId());
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content on successful deletion
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if the entry doesn't exist
            }
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while deleting project visitor: ", e);
            throw new CustomException("ERROR while deleting project visitor!");
        }
    }




    // Method to add a project worker
    @PostMapping("/addprojectworker")
    public ResponseEntity<ProjectWorkers> addProjectWorker(@RequestBody ProjectWorkers projectWorker) {
        try {
            ProjectWorkers savedProjectWorker = projectWorkerRepository.save(projectWorker);
            return new ResponseEntity<>(savedProjectWorker, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project worker: ", e);
            throw new CustomException("ERROR!");
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addprojectadmin")
    public ResponseEntity<ProjectAdmin> addProjectAdmin(@RequestBody ProjectAdmin projectAdmin) {
        try {
            ProjectAdmin savedProjectAdmin = projectAdminRepository.save(projectAdmin);
            return new ResponseEntity<>(savedProjectAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project admin: ", e);
            throw new CustomException("ERROR!");
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addprojectmaterialrequired")
    public ResponseEntity<ProjectMaterialReq> addProjectMaterialRequired(@RequestBody ProjectMaterialReq projectMaterialRequired) {
        try {
            // Save the ProjectMaterialReq entity
            ProjectMaterialReq savedProjectMaterialRequired = projectMaterialReqRepository.save(projectMaterialRequired);

            // Update the MaterialAvailable quantity
            MaterialAvailable material = materialAvailableRepository.findById(projectMaterialRequired.getMaterialReq()).orElse(null);
            if (material != null) {
                int newQuantity = material.getQuantity() - projectMaterialRequired.getQuantityReq();
                if (newQuantity < 0) {
                    // Optional: handle cases where quantity would become negative
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                material.setQuantity(newQuantity);
                materialAvailableRepository.save(material);
            } else {
                // Handle case where material is not found, if necessary
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(savedProjectMaterialRequired, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new CustomException("ERROR!");
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addwarehouse")
    public ResponseEntity<Warehouse> addWarehouse(@RequestBody Warehouse warehouse) {
        try {
            Warehouse savedWarehouse = warehouseRepository.save(warehouse);
            return new ResponseEntity<>(savedWarehouse, HttpStatus.CREATED);
        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            throw new CustomException("ERROR!");
        }
    }

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
//            List<ProjectVisitors> visitors = projectVisitorRepository.findByProjectId(projectId);
            List<Visitor> visitors = projectVisitorRepository.findVisitorsByProjectId(projectId); // Retrieve full visitor details
            List<Worker> workers = projectWorkerRepository.findWorkersByProjectId(projectId);
            List<ProjectAdmin> admins = projectAdminRepository.findByProjectId(projectId); // Get list of admins
//            Optional<ProjectAdmin> admin = projectAdminRepository.findById(projectId);
            List<Map<String, Object>> materials = projectMaterialReqRepository.findMaterialsAndQuantitiesByProjectId(projectId);
            List<Map<String, Object>> equipment = projectEquipRequiredRepository.findEquipmentAndQuantitiesByProjectId(projectId);

            // Build the response map
            Map<String, Object> response = new HashMap<>();
            response.put("project", project);
            response.put("visitors", visitors);
            response.put("workers", workers);
//            response.put("admin", admin.orElse(null));
            response.put("admins", admins); // Add list of admins to response
            response.put("materials", materials);
            response.put("equipment", equipment);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Unable to fetch project details"), HttpStatus.BAD_REQUEST);
        }
    }
//    @PostMapping("/addmaterialdetail")
//    public ResponseEntity<MaterialDetails> addMaterialDetail(@RequestBody MaterialDetails materialDetail) {
//        try {
//            MaterialDetails savedMaterialDetail = materialDetailsRepository.save(materialDetail);
//            return new ResponseEntity<>(savedMaterialDetail, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//@PostMapping("/addmaterialdetail")
//public ResponseEntity<MaterialDetails> addMaterialDetail(@RequestBody MaterialDetails materialDetail) {
//    try {
//        // Save the MaterialDetails entry
//        MaterialDetails savedMaterialDetail = materialDetailsRepository.save(materialDetail);
//
//        // Update the corresponding MaterialAvailable entry
//        Optional<MaterialAvailable> materialAvailableOpt = materialAvailableRepository.findById(materialDetail.getMaterialId());
//
//        if (materialAvailableOpt.isPresent()) {
//            MaterialAvailable materialAvailable = materialAvailableOpt.get();
//            // Add the quantity specified in MaterialDetails to the MaterialAvailable
//            materialAvailable.setQuantity(materialAvailable.getQuantity() + materialDetail.getQuantity());
//            materialAvailableRepository.save(materialAvailable); // Save updated quantity
//        } else {
//            // Handle the case where the MaterialAvailable entry does not exist
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(savedMaterialDetail, HttpStatus.CREATED);
//    } catch (Exception e) {
//        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
}
