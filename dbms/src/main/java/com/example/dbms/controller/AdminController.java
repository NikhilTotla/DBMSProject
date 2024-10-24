package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.repository.*;
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
    private ClientRepository clientRepository;
    @Autowired
    private WorkerRepository workerRepository;
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
    @GetMapping("/workers")
    public ResponseEntity<List<Worker>> getAllWorkers() {
        List<Worker> workerList = workerRepository.findAll();
        return new ResponseEntity<>(workerList, HttpStatus.OK);
    }
    @PostMapping("/addworker")
    public ResponseEntity<Worker> addWorker(@RequestBody Worker worker) {
        return service.addWorker(worker);
    }
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clientList = clientRepository.findAll();
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }

    @PostMapping("/addclient")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
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
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        try {
            Department savedDepartment = departmentRepository.save(department);
            return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
        } catch (Exception e) {
            // Return an error response if something goes wrong
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/department")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departmentList = departmentRepository.findAll();
        return new ResponseEntity<>(departmentList, HttpStatus.OK);
    }
    @PostMapping("/addEquipment")
    public ResponseEntity<EquipmentAvailable> addEquipment(@RequestBody EquipmentAvailable equipment) {
        try {
            EquipmentAvailable savedEquipment = equipmentAvailableRepository.save(equipment);

            return new ResponseEntity<>(savedEquipment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/equipment")
    public ResponseEntity<List<EquipmentAvailable>> getAllEquipment() {
        List<EquipmentAvailable> equipmentList = equipmentAvailableRepository.findAll();
        return new ResponseEntity<>(equipmentList, HttpStatus.OK);
    }
    @PostMapping("/addMaterial")
    public ResponseEntity<MaterialAvailable> addMaterial(@RequestBody MaterialAvailable material) {
        try {
            MaterialAvailable savedMaterial = materialAvailableRepository.save(material);
            return new ResponseEntity<>(savedMaterial, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/material")
    public ResponseEntity<List<MaterialAvailable>> getAllMaterials() {
        List<MaterialAvailable> materialList = materialAvailableRepository.findAll();
        return new ResponseEntity<>(materialList, HttpStatus.OK);
    }
}
