package com.example.dbms.repository;

import com.example.dbms.entity.ProjectMaterialReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class ProjectMaterialReqRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to ProjectMaterialReq entity
    private final RowMapper<ProjectMaterialReq> projectMaterialReqRowMapper = new RowMapper<>() {
        @Override
        public ProjectMaterialReq mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectMaterialReq projectMaterialReq = new ProjectMaterialReq();
            projectMaterialReq.setId(rs.getInt("id"));
            projectMaterialReq.setProjectId(rs.getInt("projectId"));
            projectMaterialReq.setMaterialReq(rs.getInt("materialReq"));
            projectMaterialReq.setQuantityReq(rs.getInt("quantityReq"));
            return projectMaterialReq;
        }
    };

    // Find all ProjectMaterialReq records by projectId
    public List<ProjectMaterialReq> findByProjectId(Integer projectId) {
        String sql = "SELECT * FROM projectMaterialReq WHERE projectId = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, projectMaterialReqRowMapper);
    }

    // Save or update a ProjectMaterialReq record
    public ProjectMaterialReq save(ProjectMaterialReq projectMaterialReq) {
        if (projectMaterialReq.getId() != null && findById(projectMaterialReq.getId()) != null) {
            // Update existing record
            String sql = "UPDATE projectMaterialReq SET projectId = ?, materialReq = ?, quantityReq = ? WHERE id = ?";
            jdbcTemplate.update(sql, projectMaterialReq.getProjectId(), projectMaterialReq.getMaterialReq(),
                    projectMaterialReq.getQuantityReq(), projectMaterialReq.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO projectMaterialReq (projectId, materialReq, quantityReq) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, projectMaterialReq.getProjectId(), projectMaterialReq.getMaterialReq(),
                    projectMaterialReq.getQuantityReq());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            projectMaterialReq.setId(generatedId);
        }
        return projectMaterialReq;
    }


    // Find a ProjectMaterialReq by its ID
    public ProjectMaterialReq findById(Integer id) {
        String sql = "SELECT * FROM projectMaterialReq WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, projectMaterialReqRowMapper);
        } catch (Exception e) {
            return null; // Return null if not found
        }
    }

    // Delete ProjectMaterialReq by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM projectMaterialReq WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Get material names and required quantities by projectId
    public List<Map<String, Object>> findMaterialsAndQuantitiesByProjectId(Integer projectId) {
        String sql = "SELECT m.name as material, p.quantityReq as quantity " +
                "FROM projectMaterialReq p " +
                "JOIN materialAvailable m ON p.materialReq = m.id " +
                "WHERE p.projectId = ?";
        return jdbcTemplate.queryForList(sql, projectId);
    }
}
