package com.example.dbms.repository;

import com.example.dbms.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class DepartmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Department entity
    private final RowMapper<Department> departmentRowMapper = new RowMapper<>() {
        @Override
        public Department mapRow(ResultSet rs, int rowNum) throws SQLException {
            Department department = new Department();
            department.setId(rs.getInt("id"));
            department.setName(rs.getString("name"));
            return department;
        }
    };

    // Find department by ID
    public Optional<Department> findById(Integer id) {
        String sql = "SELECT * FROM department WHERE id = ?";
        try {
            Department department = jdbcTemplate.queryForObject(sql, new Object[]{id}, departmentRowMapper);
            return Optional.ofNullable(department);
        } catch (Exception e) {
            return Optional.empty(); // Return Optional.empty() if department is not found
        }
    }

    // Find all departments
    public List<Department> findAll() {
        String sql = "SELECT * FROM department";
        return jdbcTemplate.query(sql, departmentRowMapper);
    }

    // Save or update a department
    public Department save(Department department) {
        if (department.getId() != null && findById(department.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE department SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, department.getName(), department.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO department (name) VALUES (?)";
            jdbcTemplate.update(sql, department.getName());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            department.setId(generatedId);
        }
        return department;
    }


    // Delete department by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM department WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Check if department exists by ID
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM department WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }
}
