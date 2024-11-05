package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "materialAvailable") // Maps to the 'materialAvailable' table
public class MaterialAvailable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for 'id'
    private Integer id;

    private String name;
    private Integer quantity;
    private Integer storedIn; // Foreign key reference to 'warehouse' table

    // Default constructor
    public MaterialAvailable() {}

    // Parameterized constructor
    public MaterialAvailable(String name, Integer quantity, Integer storedIn) {
        this.name = name;
        this.quantity = quantity;
        this.storedIn = storedIn;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStoredIn() {
        return storedIn;
    }

    public void setStoredIn(Integer storedIn) {
        this.storedIn = storedIn;
    }
}
