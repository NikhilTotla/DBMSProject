package com.example.dbms.repository;

import com.example.dbms.entity.ProjectWorkers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectWorkersRepository extends JpaRepository<ProjectWorkers, Integer> {
    // Custom query methods can be added here
    List<ProjectWorkers> findByWorkerId(Integer workerId); // Finds projects associated with a worker

}
