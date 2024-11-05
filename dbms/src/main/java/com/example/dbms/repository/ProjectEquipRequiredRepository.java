package com.example.dbms.repository;

import com.example.dbms.entity.ProjectEquipRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ProjectEquipRequiredRepository extends JpaRepository<ProjectEquipRequired, Integer> {
    @Query("SELECT new map(e.name as equipment, p.quantityRequired as quantity) " +
            "FROM ProjectEquipRequired p JOIN EquipmentAvailable e ON p.equipId = e.id " +
            "WHERE p.projectId = :projectId")
    List<Map<String, Object>> findEquipmentAndQuantitiesByProjectId(@Param("projectId") Integer projectId);
}
