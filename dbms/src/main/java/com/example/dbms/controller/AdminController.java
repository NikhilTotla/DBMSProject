package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.repository.*;
import com.example.dbms.service.*;
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
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private WorkerService workerService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private MaterialAvailableRepository materialAvailableRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private AdminRepository adminRepository;
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
        return workerService.addWorker(worker);
    }
    @GetMapping("/clients")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clientList = clientRepository.findAll();
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }
    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> adminList = adminRepository.findAll();
        return new ResponseEntity<>(adminList, HttpStatus.OK);
    }
    @PostMapping("/addclient")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        return clientService.addClient(client);
    }
    @PostMapping("/deleteClient/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        try {
            if (clientRepository.existsById(id)) {
                clientRepository.deleteById(id);
                return new ResponseEntity<>("Client deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting client", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/deleteWorker/{id}")
    public ResponseEntity<String> deleteWorker(@PathVariable Integer id) {
        try {
            if (workerRepository.existsById(id)) {
                workerRepository.deleteById(id);
                return new ResponseEntity<>("Worker deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Worker not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting worker", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getAdminDetails(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        try {
            return new ResponseEntity<>(adminService.getAdminInfo(authHeader), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Unable to fetch user details"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @PostMapping("/deleteDepartment/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Integer id) {
        try {
            if (departmentRepository.existsById(id)) {
                departmentRepository.deleteById(id);
                return new ResponseEntity<>("Department deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting department", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @PostMapping("/deleteEquipment/{id}")
    public ResponseEntity<String> deleteEquipment(@PathVariable Integer id) {
        try {
            if (equipmentAvailableRepository.existsById(id)) {
                equipmentAvailableRepository.deleteById(id);
                return new ResponseEntity<>("Equipment deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Equipment not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting equipment", HttpStatus.INTERNAL_SERVER_ERROR);
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
