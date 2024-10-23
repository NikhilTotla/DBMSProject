package com.example.dbms.repository;

import com.example.dbms.entity.EquipmentAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentAvailableRepository extends JpaRepository<EquipmentAvailable, Integer> {
    // You can add custom query methods here if needed, for example:
    // List<EquipmentAvailable> findByName(String name);
}
