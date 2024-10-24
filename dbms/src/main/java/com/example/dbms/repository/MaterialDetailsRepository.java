package com.example.dbms.repository;

import com.example.dbms.entity.MaterialDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialDetailsRepository extends JpaRepository<MaterialDetails, Integer> {
    // Custom query methods can be added here
}
