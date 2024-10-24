package com.example.dbms.repository;

import com.example.dbms.entity.ProjectVisitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectVisitorsRepository extends JpaRepository<ProjectVisitors, Integer> {
}
