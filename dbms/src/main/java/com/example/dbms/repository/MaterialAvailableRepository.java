package com.example.dbms.repository;

import com.example.dbms.entity.MaterialAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialAvailableRepository extends JpaRepository<MaterialAvailable, Integer> {
    // Custom query methods can be added if needed
}
