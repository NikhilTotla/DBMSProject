package com.example.dbms.controller;

import com.example.dbms.entity.Project;
import com.example.dbms.exception.CustomException;
import com.example.dbms.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth/worker")
public class WorkerController {
    @Autowired
    private WorkerService workerService;
    @GetMapping("/")
    public String worker() {
        return "Welcome to worker Profile";
    }
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjectsForWorker(Authentication authentication) {
        // Get the currently logged-in user's username
        try {
            String username = authentication.getName();

            // Fetch projects associated with the worker
            List<Project> projects = workerService.getProjectsForWorker(username);

            return new ResponseEntity<>(projects, HttpStatus.OK);
        }catch (Exception e) {
            throw new CustomException("ERROR!");
        }
    }
}
