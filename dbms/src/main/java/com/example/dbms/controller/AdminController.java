package com.example.dbms.controller;

import com.example.dbms.entity.Client;
import com.example.dbms.entity.Department;
import com.example.dbms.entity.Worker;
import com.example.dbms.repository.AdminRepository;
import com.example.dbms.repository.DepartmentRepository;
import com.example.dbms.service.JwtService;
import com.example.dbms.service.UserInfoService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {
    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private AdminRepository repository;
    @GetMapping("/")
    public String admin() {
        return "Welcome to admin Profile";
    }
    @PostMapping("/addworker")
    public String addworker(@RequestBody Worker worker) {
        return service.addWorker(worker);
    }
    @PostMapping("/addclient")
    public String addclient(@RequestBody Client client) {
        return service.addClient(client);
    }
    @GetMapping("/details")
    public ResponseEntity<Map<String,Object>> getUserDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // Extract token from the Authorization header
        String token = authHeader.replace("Bearer ", "");

        // Fetch user details using the token
        UserDetails userDetails = jwtService.loadUserByToken(token);
        Integer id = repository.findByUsername(userDetails.getUsername()).get().getId();
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);  // Add user ID
        response.put("username", userDetails.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/adddepartment")
    public ResponseEntity<String> addDepartment(@RequestBody Department department) {
        try {
            // Save the department entity to the database
            departmentRepository.save(department);
            return new ResponseEntity<>("Department added successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding department", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
