package com.example.dbms.repository;

import com.example.dbms.entity.ProjectAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectAdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to ProjectAdmin entity
    private final RowMapper<ProjectAdmin> projectAdminRowMapper = new RowMapper<>() {
        @Override
        public ProjectAdmin mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectAdmin projectAdmin = new ProjectAdmin();
            projectAdmin.setId(rs.getInt("id"));
            projectAdmin.setProjectId(rs.getInt("projectId"));
            projectAdmin.setControlledBy(rs.getInt("controlledBy"));
            return projectAdmin;
        }
    };

    // Find projectAdmin by ID
    public Optional<ProjectAdmin> findById(Integer id) {
        String sql = "SELECT * FROM projectAdmin WHERE id = ?";
        try {
            ProjectAdmin projectAdmin = jdbcTemplate.queryForObject(sql, new Object[]{id}, projectAdminRowMapper);
            return Optional.ofNullable(projectAdmin);
        } catch (Exception e) {
            return Optional.empty(); // Return Optional.empty() if not found
        }
    }

    // Find all projectAdmins
    public List<ProjectAdmin> findAll() {
        String sql = "SELECT * FROM projectAdmin";
        return jdbcTemplate.query(sql, projectAdminRowMapper);
    }

    // Save or update projectAdmin
    public ProjectAdmin save(ProjectAdmin projectAdmin) {
        if (projectAdmin.getId() != null && findById(projectAdmin.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE projectAdmin SET projectId = ?, controlledBy = ? WHERE id = ?";
            jdbcTemplate.update(sql, projectAdmin.getProjectId(), projectAdmin.getControlledBy(), projectAdmin.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO projectAdmin (projectId, controlledBy) VALUES (?, ?)";
            jdbcTemplate.update(sql, projectAdmin.getProjectId(), projectAdmin.getControlledBy());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            projectAdmin.setId(generatedId);
        }
        return projectAdmin;
    }


    // Delete projectAdmin by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM projectAdmin WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Check if projectAdmin exists by ID
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM projectAdmin WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    // Find projectAdmins by projectId
    public List<ProjectAdmin> findByProjectId(Integer projectId) {
        String sql = "SELECT * FROM projectAdmin WHERE projectId = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, projectAdminRowMapper);
    }
}
