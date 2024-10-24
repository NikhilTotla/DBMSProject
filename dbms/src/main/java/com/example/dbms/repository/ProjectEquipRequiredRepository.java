package com.example.dbms.repository;

import com.example.dbms.entity.ProjectEquipRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectEquipRequiredRepository extends JpaRepository<ProjectEquipRequired, Integer> {
    // You can add custom query methods here if needed
}
