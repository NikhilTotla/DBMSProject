package com.example.dbms.controller;

import com.example.dbms.entity.Project;
import com.example.dbms.repository.ProjectRepository;
import com.example.dbms.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/client")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProjectRepository projectRepository;
    @GetMapping("/")
    public String client() {
        return "Welcome to client Profile";
    }
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getMyProjects(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        Map<String,Object> clientInfo = clientService.getClientInfo(authHeader);
        List<Project> projects = projectRepository.findBySoldTo((Integer)clientInfo.get("id"));
        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no projects found
        }
        return ResponseEntity.ok(projects); // Return 200 OK with the list of projects
    }
}
