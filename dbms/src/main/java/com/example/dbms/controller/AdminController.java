package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.repository.AdminRepository;
import com.example.dbms.repository.DepartmentRepository;
import com.example.dbms.repository.EquipmentAvailableRepository;
import com.example.dbms.repository.MaterialAvailableRepository;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {
    @Autowired
    private UserInfoService service;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MaterialAvailableRepository materialAvailableRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EquipmentAvailableRepository equipmentAvailableRepository;
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
    @PostMapping("/addEquipment")
    public ResponseEntity<String> addEquipment(@RequestBody EquipmentAvailable equipment) {
        try {
            equipmentAvailableRepository.save(equipment);
            return new ResponseEntity<>("Equipment added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding equipment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/equipment")
    public ResponseEntity<List<EquipmentAvailable>> getAllEquipment() {
        List<EquipmentAvailable> equipmentList = equipmentAvailableRepository.findAll();
        return new ResponseEntity<>(equipmentList, HttpStatus.OK);
    }
    @PostMapping("/addMaterial")
    public ResponseEntity<String> addMaterial(@RequestBody MaterialAvailable material) {
        try {
            materialAvailableRepository.save(material);
            return new ResponseEntity<>("Material added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding material: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/material")
    public ResponseEntity<List<MaterialAvailable>> getAllMaterials() {
        List<MaterialAvailable> materialList = materialAvailableRepository.findAll();
        return new ResponseEntity<>(materialList, HttpStatus.OK);
    }
}
