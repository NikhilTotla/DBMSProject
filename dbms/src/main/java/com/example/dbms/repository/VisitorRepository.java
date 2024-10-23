package com.example.dbms.repository;

import com.example.dbms.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {
    // Add custom query methods if needed, for example:
    // List<Visitor> findByEmail(String email);
}
