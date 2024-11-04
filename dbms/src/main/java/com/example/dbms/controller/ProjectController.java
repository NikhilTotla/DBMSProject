package com.example.dbms.controller;

import com.example.dbms.entity.*;
import com.example.dbms.repository.*;
import com.example.dbms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/admin")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectEquipRequiredRepository projectEquipRequiredRepository;
    @Autowired
    private ProjectMaterialReqRepository projectMaterialReqRepository;
    @Autowired
    private ProjectAdminRepository projectAdminRepository;
    @Autowired
    private ProjectVisitorsRepository projectVisitorRepository;
    @Autowired
    private MaterialDetailsRepository materialDetailsRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private MaterialAvailableRepository materialAvailableRepository;
    @Autowired
    private ProjectWorkersRepository projectWorkerRepository;
    @PostMapping("/addproject")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addprojectequiprequired")
    public ResponseEntity<ProjectEquipRequired> addProjectEquipRequired(@RequestBody ProjectEquipRequired projectEquipRequired) {
        try {
            ProjectEquipRequired savedProjectEquipRequired = projectEquipRequiredRepository.save(projectEquipRequired);
            return new ResponseEntity<>(savedProjectEquipRequired, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project equipment requirement: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/addprojectvisitor")
    public ResponseEntity<ProjectVisitors> addProjectVisitor(@RequestBody ProjectVisitors projectVisitor) {
        try {
            ProjectVisitors savedProjectVisitor = projectVisitorRepository.save(projectVisitor);
            return new ResponseEntity<>(savedProjectVisitor, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project visitor: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to add a project worker
    @PostMapping("/addprojectworker")
    public ResponseEntity<ProjectWorkers> addProjectWorker(@RequestBody ProjectWorkers projectWorker) {
        try {
            ProjectWorkers savedProjectWorker = projectWorkerRepository.save(projectWorker);
            return new ResponseEntity<>(savedProjectWorker, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project worker: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addprojectadmin")
    public ResponseEntity<ProjectAdmin> addProjectAdmin(@RequestBody ProjectAdmin projectAdmin) {
        try {
            ProjectAdmin savedProjectAdmin = projectAdminRepository.save(projectAdmin);
            return new ResponseEntity<>(savedProjectAdmin, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log the error if necessary
            // logger.error("Error occurred while saving project admin: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PostMapping("/addprojectmaterialrequired")
//    public ResponseEntity<ProjectMaterialReq> addProjectMaterialRequired(@RequestBody ProjectMaterialReq projectMaterialRequired) {
//        try {
//            ProjectMaterialReq savedProjectMaterialRequired = projectMaterialReqRepository.save(projectMaterialRequired);
//            return new ResponseEntity<>(savedProjectMaterialRequired, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PostMapping("/addprojectmaterialrequired")
    public ResponseEntity<ProjectMaterialReq> addProjectMaterialRequired(@RequestBody ProjectMaterialReq projectMaterialRequired) {
        try {
            // Save the ProjectMaterialReq entity
            ProjectMaterialReq savedProjectMaterialRequired = projectMaterialReqRepository.save(projectMaterialRequired);

            // Update the MaterialAvailable quantity
            MaterialAvailable material = materialAvailableRepository.findById(projectMaterialRequired.getMaterialReq()).orElse(null);
            if (material != null) {
                int newQuantity = material.getQuantity() - projectMaterialRequired.getQuantityReq();
                if (newQuantity < 0) {
                    // Optional: handle cases where quantity would become negative
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                material.setQuantity(newQuantity);
                materialAvailableRepository.save(material);
            } else {
                // Handle case where material is not found, if necessary
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(savedProjectMaterialRequired, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addwarehouse")
    public ResponseEntity<Warehouse> addWarehouse(@RequestBody Warehouse warehouse) {
        try {
            Warehouse savedWarehouse = warehouseRepository.save(warehouse);
            return new ResponseEntity<>(savedWarehouse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/addmaterialdetail")
    public ResponseEntity<MaterialDetails> addMaterialDetail(@RequestBody MaterialDetails materialDetail) {
        try {
            MaterialDetails savedMaterialDetail = materialDetailsRepository.save(materialDetail);
            return new ResponseEntity<>(savedMaterialDetail, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
