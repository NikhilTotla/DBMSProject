package com.example.dbms.repository;

import com.example.dbms.entity.ProjectAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAdminRepository extends JpaRepository<ProjectAdmin, Integer> {
    // Custom query methods can be added here
}
