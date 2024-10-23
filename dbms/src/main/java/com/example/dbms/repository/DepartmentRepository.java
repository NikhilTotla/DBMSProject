package com.example.dbms.repository;

import com.example.dbms.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // Custom query methods can be added here if needed, e.g.,
    // Optional<Department> findByName(String name);
}
