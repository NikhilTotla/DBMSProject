package com.example.dbms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/worker")
public class WorkerController {
    @GetMapping("/")
    public String worker() {
        return "Welcome to worker Profile";
    }

}
