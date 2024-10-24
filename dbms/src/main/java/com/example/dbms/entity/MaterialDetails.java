package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MaterialDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer materialId; // Foreign key to MaterialAvailable
    private Integer storedIn;   // Foreign key to Warehouse
    private Integer quantity;

    // Constructors
    public MaterialDetails() {}

    public MaterialDetails(Integer materialId, Integer storedIn, Integer quantity) {
        this.materialId = materialId;
        this.storedIn = storedIn;
        this.quantity = quantity;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getStoredIn() {
        return storedIn;
    }

    public void setStoredIn(Integer storedIn) {
        this.storedIn = storedIn;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
