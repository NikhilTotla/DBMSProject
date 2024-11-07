package com.example.dbms.repository;

import com.example.dbms.entity.ProjectEquipRequired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectEquipRequiredRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to ProjectEquipRequired entity
    private final RowMapper<ProjectEquipRequired> projectEquipRequiredRowMapper = new RowMapper<>() {
        @Override
        public ProjectEquipRequired mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectEquipRequired projectEquipRequired = new ProjectEquipRequired();
            projectEquipRequired.setId(rs.getInt("id"));
            projectEquipRequired.setProjectId(rs.getInt("projectId"));
            projectEquipRequired.setEquipId(rs.getInt("equipId"));
            projectEquipRequired.setQuantityRequired(rs.getInt("quantityRequired"));
            projectEquipRequired.setDurationReq(rs.getInt("durationReq"));
            return projectEquipRequired;
        }
    };

    // Find all ProjectEquipRequired records by projectId
    public List<ProjectEquipRequired> findByProjectId(Integer projectId) {
        String sql = "SELECT * FROM projectEquipRequired WHERE projectId = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, projectEquipRequiredRowMapper);
    }

    // Save or update a ProjectEquipRequired record
    public ProjectEquipRequired save(ProjectEquipRequired projectEquipRequired) {
        if (projectEquipRequired.getId() != null && findById(projectEquipRequired.getId()) != null) {
            // Update existing record
            String sql = "UPDATE projectEquipRequired SET projectId = ?, equipId = ?, quantityRequired = ?, durationReq = ? WHERE id = ?";
            jdbcTemplate.update(sql, projectEquipRequired.getProjectId(), projectEquipRequired.getEquipId(),
                    projectEquipRequired.getQuantityRequired(), projectEquipRequired.getDurationReq(), projectEquipRequired.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO projectEquipRequired (projectId, equipId, quantityRequired, durationReq) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, projectEquipRequired.getProjectId(), projectEquipRequired.getEquipId(),
                    projectEquipRequired.getQuantityRequired(), projectEquipRequired.getDurationReq());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            projectEquipRequired.setId(generatedId);
        }
        return projectEquipRequired;
    }


    // Find a ProjectEquipRequired by its ID
    public ProjectEquipRequired findById(Integer id) {
        String sql = "SELECT * FROM projectEquipRequired WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, projectEquipRequiredRowMapper);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }

    // Delete ProjectEquipRequired by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM projectEquipRequired WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Get equipment names and required quantities by projectId
    public List<Map<String, Object>> findEquipmentAndQuantitiesByProjectId(Integer projectId) {
        String sql = "SELECT e.name as equipment, p.quantityRequired as quantity " +
                "FROM projectEquipRequired p " +
                "JOIN equipmentAvailable e ON p.equipId = e.id " +
                "WHERE p.projectId = ?";
        return jdbcTemplate.queryForList(sql, projectId);
    }
}
