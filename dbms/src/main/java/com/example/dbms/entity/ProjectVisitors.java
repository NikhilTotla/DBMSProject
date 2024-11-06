package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "projectVisitors")
public class ProjectVisitors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer projectId;
    private Integer visitorId;
    private Integer servedBy;

    // Constructors
    public ProjectVisitors() {}

    public ProjectVisitors(Integer projectId, Integer servedBy, Integer visitorId) {
        this.projectId = projectId;
        this.servedBy = servedBy;
        this.visitorId = visitorId;
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

    public Integer getServedBy() {
        return servedBy;
    }

    public void setServedBy(Integer servedBy) {
        this.servedBy = servedBy;
    }

    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }
}
//package com.example.dbms.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import jakarta.persistence.UniqueConstraint;
//
//@Entity
//@Table(name = "projectVisitors", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"projectId", "visitorId"})
//})
//public class ProjectVisitors {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    @Column(nullable = false)
//    private Integer projectId;
//
//    @Column(nullable = false)
//    private Integer visitorId;
//
//    private Integer servedBy;
//
//    // Constructors
//    public ProjectVisitors() {}
//
//    public ProjectVisitors(Integer projectId, Integer servedBy, Integer visitorId) {
//        this.projectId = projectId;
//        this.servedBy = servedBy;
//        this.visitorId = visitorId;
//    }
//
//    // Getters and Setters
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getProjectId() {
//        return projectId;
//    }
//
//    public void setProjectId(Integer projectId) {
//        this.projectId = projectId;
//    }
//
//    public Integer getServedBy() {
//        return servedBy;
//    }
//
//    public void setServedBy(Integer servedBy) {
//        this.servedBy = servedBy;
//    }
//
//    public Integer getVisitorId() {
//        return visitorId;
//    }
//
//    public void setVisitorId(Integer visitorId) {
//        this.visitorId = visitorId;
//    }
//}
