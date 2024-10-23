package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "department") // This maps to the 'department' table in the database
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This will map to auto_increment in SQL
    private Integer id;

    private String name;

    // Default constructor
    public Department() {}

    // Parameterized constructor
    public Department(String name) {
        this.name = name;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
