package com.example.dbms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/client")
public class ClientController {
    @GetMapping("/")
    public String client() {
        return "Welcome to client Profile";
    }


}
