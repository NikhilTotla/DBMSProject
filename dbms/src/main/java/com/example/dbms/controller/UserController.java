package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.service.JwtService;
import com.example.dbms.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("admin/addclient")
    public String addclient(@RequestBody Client client) {
        return service.addClient(client);
    }
    @PostMapping("addAdmin")
    public String addAdmin(@RequestBody Admin admin) {
        return service.addAdmin(admin);
    }

    @PostMapping("admin/addworker")
    public String addworker(@RequestBody Worker worker) {
        return service.addWorker(worker);
    }

    @GetMapping("/client/**")
        public String client() {
        return "Welcome to client Profile";
    }
    @GetMapping("/worker/**")
        public String worker() {
        return "Welcome to worker Profile";
    }
    @GetMapping("/admin/**")
        public String admin() {
        return "Welcome to admin Profile";
    }


    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
