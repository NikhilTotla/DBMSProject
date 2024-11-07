package com.example.dbms.repository;

import com.example.dbms.entity.MaterialAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MaterialAvailableRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to MaterialAvailable entity
    private final RowMapper<MaterialAvailable> materialAvailableRowMapper = new RowMapper<>() {
        @Override
        public MaterialAvailable mapRow(ResultSet rs, int rowNum) throws SQLException {
            MaterialAvailable material = new MaterialAvailable();
            material.setId(rs.getInt("id"));
            material.setName(rs.getString("name"));
            material.setQuantity(rs.getInt("quantity"));
            material.setStoredIn(rs.getInt("storedIn"));
            return material;
        }
    };

    // Find material by ID
    public Optional<MaterialAvailable> findById(Integer id) {
        String sql = "SELECT * FROM materialAvailable WHERE id = ?";
        try {
            MaterialAvailable material = jdbcTemplate.queryForObject(sql, new Object[]{id}, materialAvailableRowMapper);
            return Optional.ofNullable(material);
        } catch (Exception e) {
            return Optional.empty(); // Return Optional.empty() if material is not found
        }
    }

    // Find all materials
    public List<MaterialAvailable> findAll() {
        String sql = "SELECT * FROM materialAvailable";
        return jdbcTemplate.query(sql, materialAvailableRowMapper);
    }

    // Save or update material
    public MaterialAvailable save(MaterialAvailable materialAvailable) {
        if (materialAvailable.getId() != null && findById(materialAvailable.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE materialAvailable SET name = ?, quantity = ?, storedIn = ? WHERE id = ?";
            jdbcTemplate.update(sql, materialAvailable.getName(), materialAvailable.getQuantity(), materialAvailable.getStoredIn(), materialAvailable.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO materialAvailable (name, quantity, storedIn) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, materialAvailable.getName(), materialAvailable.getQuantity(), materialAvailable.getStoredIn());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            materialAvailable.setId(generatedId);
        }
        return materialAvailable;
    }


    // Delete material by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM materialAvailable WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Check if material exists by ID
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM materialAvailable WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    // Example of a custom query: Find materials by name
    public List<MaterialAvailable> findByName(String name) {
        String sql = "SELECT * FROM materialAvailable WHERE name = ?";
        return jdbcTemplate.query(sql, new Object[]{name}, materialAvailableRowMapper);
    }
}
