package com.example.dbms.repository;

import com.example.dbms.entity.ProjectVisitors;
import com.example.dbms.entity.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectVisitorsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to ProjectVisitors entity
    private final RowMapper<ProjectVisitors> projectVisitorsRowMapper = new RowMapper<>() {
        @Override
        public ProjectVisitors mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectVisitors projectVisitors = new ProjectVisitors();
            projectVisitors.setId(rs.getInt("id"));
            projectVisitors.setProjectId(rs.getInt("projectId"));
            projectVisitors.setVisitorId(rs.getInt("visitorId"));
            // Map other fields if needed
            return projectVisitors;
        }
    };

    // RowMapper for Visitor entity
    private final RowMapper<Visitor> visitorRowMapper = new RowMapper<>() {
        @Override
        public Visitor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Visitor visitor = new Visitor();
            visitor.setId(rs.getInt("id"));
            visitor.setFirstName(rs.getString("firstName"));
            visitor.setLastName(rs.getString("lastName"));
            // Map other fields if needed
            return visitor;
        }
    };
    public List<ProjectVisitors> findByProjectId(Integer projectId) {
        String sql = "SELECT * FROM ProjectVisitors WHERE projectId = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, projectVisitorsRowMapper);
    }
    // Find visitors by project ID
    public List<Visitor> findVisitorsByProjectId(Integer projectId) {
        String sql = "SELECT v.* FROM Visitor v " +
                "JOIN ProjectVisitors pv ON v.id = pv.visitorId " +
                "WHERE pv.projectId = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, visitorRowMapper);
    }

    // Delete by Project ID and Visitor ID
    public void deleteByProjectIdAndVisitorId(Integer projectId, Integer visitorId) {
        String sql = "DELETE FROM ProjectVisitors WHERE projectId = ? AND visitorId = ?";
        jdbcTemplate.update(sql, projectId, visitorId);
    }

    // Find ProjectVisitor by Project ID and Visitor ID
    public ProjectVisitors findByProjectIdAndVisitorId(Integer projectId, Integer visitorId) {
        String sql = "SELECT * FROM ProjectVisitors WHERE projectId = ? AND visitorId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{projectId, visitorId}, projectVisitorsRowMapper);
        } catch (Exception e) {
            // If no record is found, return null
            return null;
        }
    }

    // Save (Insert or Update) ProjectVisitors
    public ProjectVisitors save(ProjectVisitors projectVisitors) {
        // Check if the project-visitor combination already exists
        if (findByProjectIdAndVisitorId(projectVisitors.getProjectId(), projectVisitors.getVisitorId()) != null) {
            // Update existing record
            String sql = "UPDATE ProjectVisitors SET projectId = ?, visitorId = ? WHERE id = ?";
            jdbcTemplate.update(sql, projectVisitors.getProjectId(), projectVisitors.getVisitorId(), projectVisitors.getId());
        } else {
            // Insert new record
            String sql = "INSERT INTO ProjectVisitors (projectId, visitorId) VALUES (?, ?)";
            jdbcTemplate.update(sql, projectVisitors.getProjectId(), projectVisitors.getVisitorId());
        }
        return projectVisitors;
    }


    // Delete ProjectVisitor by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM ProjectVisitors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Find all ProjectVisitors
    public List<ProjectVisitors> findAll() {
        String sql = "SELECT * FROM ProjectVisitors";
        return jdbcTemplate.query(sql, projectVisitorsRowMapper);
    }
}
