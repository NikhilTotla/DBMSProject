package com.example.dbms.repository;

import com.example.dbms.entity.ProjectVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectVisitorsRepository extends JpaRepository<ProjectVisitors, Integer> {
    List<ProjectVisitors> findByProjectId(Integer projectId);
}
