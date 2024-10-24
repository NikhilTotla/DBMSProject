package com.example.dbms.repository;

import com.example.dbms.entity.ProjectWorkers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectWorkersRepository extends JpaRepository<ProjectWorkers, Integer> {
    // Custom query methods can be added here
}
