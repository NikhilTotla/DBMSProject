package com.example.dbms.repository;

import com.example.dbms.entity.ProjectWorkers;
import com.example.dbms.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectWorkersRepository extends JpaRepository<ProjectWorkers, Integer> {

    // Finds projects associated with a worker
    List<ProjectWorkers> findByWorkerId(Integer workerId);

    // Finds workers associated with a project
    @Query("SELECT w FROM Worker w JOIN ProjectWorkers pw ON w.id = pw.workerId WHERE pw.projectId = :projectId")
    List<Worker> findWorkersByProjectId(@Param("projectId") Integer projectId);
}
