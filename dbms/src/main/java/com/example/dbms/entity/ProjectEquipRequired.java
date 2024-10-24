package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "projectEquipRequired")
public class ProjectEquipRequired {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for the id
    private Integer id;

    private Integer projectId;
    private Integer equipId;
    private Integer quantityRequired;
    private Integer durationReq;

    // Constructors
    public ProjectEquipRequired() {}

    public ProjectEquipRequired(Integer projectId, Integer equipId, Integer quantityRequired, Integer durationReq) {
        this.projectId = projectId;
        this.equipId = equipId;
        this.quantityRequired = quantityRequired;
        this.durationReq = durationReq;
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

    public Integer getEquipId() {
        return equipId;
    }

    public void setEquipId(Integer equipId) {
        this.equipId = equipId;
    }

    public Integer getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(Integer quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public Integer getDurationReq() {
        return durationReq;
    }

    public void setDurationReq(Integer durationReq) {
        this.durationReq = durationReq;
    }
}
