package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProjectMaterialReq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer projectId;   // Foreign key to Projects
    private Integer materialReq; // Foreign key to MaterialAvailable
    private Integer quantityReq;

    // Constructors
    public ProjectMaterialReq() {}

    public ProjectMaterialReq(Integer projectId, Integer materialReq, Integer quantityReq) {
        this.projectId = projectId;
        this.materialReq = materialReq;
        this.quantityReq = quantityReq;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getMaterialReq() {
        return materialReq;
    }

    public void setMaterialReq(Integer materialReq) {
        this.materialReq = materialReq;
    }

    public Integer getQuantityReq() {
        return quantityReq;
    }

    public void setQuantityReq(Integer quantityReq) {
        this.quantityReq = quantityReq;
    }
}
