package com.example.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipmentAvailable")
public class EquipmentAvailable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment for the 'id' column
    private Integer id;

    private String name;
    private Integer quantity;
    private Integer costPerHr;

    // Default constructor
    public EquipmentAvailable() {}

    // Parameterized constructor
    public EquipmentAvailable(String name, Integer quantity, Integer costPerHr) {
        this.name = name;
        this.quantity = quantity;
        this.costPerHr = costPerHr;
    }

    // Getters and Setters
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

    public Integer getCostPerHr() {
        return costPerHr;
    }

    public void setCostPerHr(Integer costPerHr) {
        this.costPerHr = costPerHr;
    }

}
