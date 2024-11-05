package com.example.dbms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "projectAdmin")
public class ProjectAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer projectId;    // Foreign key to Projects
    private Integer controlledBy; // Foreign key to Workers

    // Constructors
    public ProjectAdmin() {}

    public ProjectAdmin(Integer projectId, Integer controlledBy) {
        this.projectId = projectId;
        this.controlledBy = controlledBy;
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

    public Integer getControlledBy() {
        return controlledBy;
    }

    public void setControlledBy(Integer controlledBy) {
        this.controlledBy = controlledBy;
    }
}
