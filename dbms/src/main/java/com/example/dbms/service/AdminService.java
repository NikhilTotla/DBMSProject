package com.example.dbms.service;

import com.example.dbms.entity.Admin;
import com.example.dbms.entity.ProjectVisitors;
import com.example.dbms.entity.Visitor;
import com.example.dbms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private ProjectVisitorsRepository projectVisitorRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<String> addAdmin(Admin admin) {
        // Check if username exists in Admin, Client, or Worker
        if (adminRepository.findByUsername(admin.getUsername()).isPresent() ||
                clientRepository.findByUsername(admin.getUsername()).isPresent() ||
                workerRepository.findByUsername(admin.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT); // Username conflict
        }

        try {
            admin.setPassword(encoder.encode(admin.getPassword()));
            adminRepository.save(admin);
            return new ResponseEntity<>("Admin Added Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding admin", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public Map<String, Object> getAdminInfo(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserDetails userDetails = jwtService.loadUserByToken(token);
        Integer id = adminRepository.findByUsername(userDetails.getUsername()).get().getId();
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);  // Add user ID
        response.put("username", userDetails.getUsername());

        return response;
    }

    public ResponseEntity<String> addVisitor(Visitor visitor) {
        // Check if a visitor with the same email already exists
        if (visitorRepository.findByEmail(visitor.getEmail()).isPresent()) {
            return new ResponseEntity<>("Visitor with this email already exists", HttpStatus.CONFLICT);
        }

        try {
            visitorRepository.save(visitor);
            return new ResponseEntity<>("Visitor added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding visitor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Visitor>> getAllVisitors() {
        List<Visitor> visitors = visitorRepository.findAll();
        return new ResponseEntity<>(visitors, HttpStatus.OK);
    }

    // Get all project visitors
    public ResponseEntity<List<ProjectVisitors>> getAllProjectVisitors() {
        List<ProjectVisitors> projectVisitors = projectVisitorRepository.findAll();
        return new ResponseEntity<>(projectVisitors, HttpStatus.OK);
    }

}
