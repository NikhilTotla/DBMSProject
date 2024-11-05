package com.example.dbms.repository;

import com.example.dbms.entity.ProjectMaterialReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectMaterialReqRepository extends JpaRepository<ProjectMaterialReq, Integer> {
    @Query("SELECT new map(m.name as material, p.quantityReq as quantity) " +
            "FROM ProjectMaterialReq p JOIN MaterialAvailable m ON p.materialReq = m.id " +
            "WHERE p.projectId = :projectId")
    List<Map<String, Object>> findMaterialsAndQuantitiesByProjectId(@Param("projectId") Integer projectId);
}
