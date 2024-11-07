package com.example.dbms.repository;

import com.example.dbms.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Project entity
    private final RowMapper<Project> projectRowMapper = new RowMapper<>() {
        @Override
        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            Project project = new Project();
            project.setId(rs.getInt("id"));
            project.setName(rs.getString("name"));
            project.setStartDate(rs.getDate("startDate"));
            project.setEndDate(rs.getDate("endDate"));
            project.setSoldTo(rs.getInt("soldTo"));
            project.setBudget(rs.getInt("budget"));
            project.setLocation(rs.getString("location"));
            return project;
        }
    };

    // Find projects by soldTo
    public List<Project> findBySoldTo(Integer soldTo) {
        String sql = "SELECT * FROM project WHERE soldTo = ?";
        return jdbcTemplate.query(sql, new Object[]{soldTo}, projectRowMapper);
    }
    public List<Project> findAllById(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();  // Return empty list if ids are null or empty
        }

        // Create the SQL query with placeholders for the list of ids
        String sql = "SELECT * FROM project WHERE id IN (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) + ")";

        // Execute the query and return the list of projects
        return jdbcTemplate.query(sql, projectRowMapper);
    }
    // Save or update a Project record
    public Project save(Project project) {
        if (project.getId() != null && findById(project.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE project SET name = ?, startDate = ?, endDate = ?, soldTo = ?, budget = ?, location = ? WHERE id = ?";
            jdbcTemplate.update(sql, project.getName(), project.getStartDate(), project.getEndDate(),
                    project.getSoldTo(), project.getBudget(), project.getLocation(), project.getId());
            return project;  // Return the updated project object
        } else {
            // Insert new record
            String sql = "INSERT INTO project (name, startDate, endDate, soldTo, budget, location) VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, project.getName(), project.getStartDate(), project.getEndDate(),
                    project.getSoldTo(), project.getBudget(), project.getLocation());

            // Get the generated id (assuming it's auto-incremented)
            String query = "SELECT LAST_INSERT_ID()";
            Integer generatedId = jdbcTemplate.queryForObject(query, Integer.class);

            // Set the generated id to the project and return the project object
            project.setId(generatedId);
            return project;
        }
    }


    public List<Project> findAll() {
        String sql = "SELECT * FROM project";
        return jdbcTemplate.query(sql, projectRowMapper);
    }

    // Check if a Project exists by ID
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM project WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }
    // Find a Project by its ID
    public Optional<Project> findById(Integer id) {
        String sql = "SELECT * FROM project WHERE id = ?";
        try {
            Project project = jdbcTemplate.queryForObject(sql, new Object[]{id}, projectRowMapper);
            return Optional.ofNullable(project);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    // Delete a Project by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM project WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
