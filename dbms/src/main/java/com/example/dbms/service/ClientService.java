package com.example.dbms.service;

import com.example.dbms.entity.Client;
import com.example.dbms.entity.Worker;
import com.example.dbms.entity.Visitor;
import com.example.dbms.exception.CustomException;
import com.example.dbms.repository.ClientRepository;
import com.example.dbms.repository.WorkerRepository;
import com.example.dbms.repository.AdminRepository;
import com.example.dbms.repository.ProjectWorkersRepository;
import com.example.dbms.repository.ProjectMaterialReqRepository;
import com.example.dbms.repository.ProjectVisitorsRepository;
import com.example.dbms.entity.ProjectVisitors;

import com.example.dbms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ProjectWorkersRepository projectWorkersRepository;
    @Autowired
    private ProjectMaterialReqRepository projectMaterialReqRepository;
    @Autowired
    private ProjectEquipRequiredRepository projectEquipRequiredRepository;
    @Autowired
    private ProjectVisitorsRepository projectVisitorsRepository;
    @Autowired
    private VisitorRepository visitorRepository;

    public ResponseEntity<Client> addClient(Client client) {
        // Check if username exists in Client, Worker, or Admin
        if (clientRepository.findByUsername(client.getUsername()).isPresent() ||
                workerRepository.findByUsername(client.getUsername()).isPresent() ||
                adminRepository.findByUsername(client.getUsername()).isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Username already exists
        }

        try {
            client.setPassword(encoder.encode(client.getPassword()));
            Client savedClient = clientRepository.save(client);
            return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new CustomException("ERROR!");
        }
    }

    public Map<String, Object> getClientInfo(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserDetails userDetails = jwtService.loadUserByToken(token);
        Integer id = clientRepository.findByUsername(userDetails.getUsername()).orElseThrow().getId();

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("username", userDetails.getUsername());

        return response;
    }

    public Map<String, Object> getProjectDetails(Integer projectId) {
        List<Worker> workers = projectWorkersRepository.findWorkersByProjectId(projectId);
        List<Map<String, Object>> materials = projectMaterialReqRepository.findMaterialsAndQuantitiesByProjectId(projectId);
        List<Map<String, Object>> equipment = projectEquipRequiredRepository.findEquipmentAndQuantitiesByProjectId(projectId);

        Map<String, Object> projectDetails = new HashMap<>();
        projectDetails.put("workers", workers);
        projectDetails.put("materials", materials);
        projectDetails.put("equipment", equipment);

        return projectDetails;
    }

    public List<Visitor> getVisitorsByProjectId(Integer projectId) {
        return projectVisitorsRepository.findVisitorsByProjectId(projectId);
    }

    public boolean deleteVisitorFromProject(Integer projectId, Integer visitorId) {
        // Find the ProjectVisitors record
        ProjectVisitors projectVisitor = projectVisitorsRepository.findByProjectIdAndServedBy(projectId, visitorId);
        if (projectVisitor != null) {
            // Delete the ProjectVisitors record
            projectVisitorsRepository.delete(projectVisitor);
            return true;
        }
        return false;
    }

}



