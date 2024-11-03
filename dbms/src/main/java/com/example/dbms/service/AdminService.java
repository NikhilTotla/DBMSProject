package com.example.dbms.service;

import com.example.dbms.entity.Admin;
import com.example.dbms.repository.AdminRepository;
import com.example.dbms.repository.ClientRepository;
import com.example.dbms.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

}
