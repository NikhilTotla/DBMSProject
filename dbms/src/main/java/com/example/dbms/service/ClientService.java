package com.example.dbms.service;

import com.example.dbms.entity.Client;
import com.example.dbms.repository.ClientRepository;
import com.example.dbms.repository.WorkerRepository;
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
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
}
