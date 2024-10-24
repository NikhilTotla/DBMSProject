package com.example.dbms.repository;

import com.example.dbms.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Custom query methods can be added here
    // Example: List<Projects> findByName(String name);
}
