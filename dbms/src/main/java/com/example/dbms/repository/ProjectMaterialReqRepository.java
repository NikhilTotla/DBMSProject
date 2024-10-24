package com.example.dbms.repository;

import com.example.dbms.entity.ProjectMaterialReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMaterialReqRepository extends JpaRepository<ProjectMaterialReq, Integer> {
    // Custom query methods can be added here
}
