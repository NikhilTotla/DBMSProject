package com.example.dbms.controller;

import com.example.dbms.entity.AuthRequest;
import com.example.dbms.entity.Client;
import com.example.dbms.entity.UserInfo;
import com.example.dbms.entity.Worker;
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

    @PostMapping("/addclient")
    public String addNewclient(@RequestBody Client client) {
        return service.addClient(client);
    }
    @PostMapping("/addworker")
    public String addNewworker(@RequestBody Worker worker) {
        return service.addWorker(worker);
    }

    @GetMapping("/client/**")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String clientProfile() {
        return "Welcome to client Profile";
    }


//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin/adminProfile")
    public String adminProfile() {
        return "Welcome to Admin Profile";
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
