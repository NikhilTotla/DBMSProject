package com.example.dbms.repository;

import com.example.dbms.entity.Project;
import com.example.dbms.entity.ProjectVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Custom query methods can be added here
    List<Project> findBySoldTo(Integer soldTo);
}
