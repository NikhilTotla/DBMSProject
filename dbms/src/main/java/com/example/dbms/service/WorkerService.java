package com.example.dbms.service;

import com.example.dbms.entity.Worker;
import com.example.dbms.repository.WorkerRepository;
import com.example.dbms.repository.ClientRepository;
import com.example.dbms.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    public ResponseEntity<Worker> addWorker(Worker worker) {
        // Check if username exists in Worker, Client, or Admin
        if (workerRepository.findByUsername(worker.getUsername()).isPresent() ||
                clientRepository.findByUsername(worker.getUsername()).isPresent() ||
                adminRepository.findByUsername(worker.getUsername()).isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // Username already exists
        }

        try {
            worker.setPassword(encoder.encode(worker.getPassword()));
            Worker savedWorker = workerRepository.save(worker);
            return new ResponseEntity<>(savedWorker, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public Map<String, Object> getWorkerInfo(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserDetails userDetails = jwtService.loadUserByToken(token);
        Integer id = workerRepository.findByUsername(userDetails.getUsername()).orElseThrow().getId();

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("username", userDetails.getUsername());

        return response;
    }
}
