package com.example.dbms.repository;

import com.example.dbms.entity.EquipmentAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EquipmentAvailableRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to EquipmentAvailable entity
    private final RowMapper<EquipmentAvailable> equipmentAvailableRowMapper = new RowMapper<>() {
        @Override
        public EquipmentAvailable mapRow(ResultSet rs, int rowNum) throws SQLException {
            EquipmentAvailable equipment = new EquipmentAvailable();
            equipment.setId(rs.getInt("id"));
            equipment.setName(rs.getString("name"));
            equipment.setQuantity(rs.getInt("quantity"));
            equipment.setCostPerHr(rs.getInt("costPerHr"));
            return equipment;
        }
    };

    // Find equipment by ID
    public Optional<EquipmentAvailable> findById(Integer id) {
        String sql = "SELECT * FROM equipmentAvailable WHERE id = ?";
        try {
            EquipmentAvailable equipment = jdbcTemplate.queryForObject(sql, new Object[]{id}, equipmentAvailableRowMapper);
            return Optional.ofNullable(equipment);
        } catch (Exception e) {
            return Optional.empty(); // Return Optional.empty() if equipment is not found
        }
    }

    // Find all equipment
    public List<EquipmentAvailable> findAll() {
        String sql = "SELECT * FROM equipmentAvailable";
        return jdbcTemplate.query(sql, equipmentAvailableRowMapper);
    }

    // Save or update equipment
    public EquipmentAvailable save(EquipmentAvailable equipmentAvailable) {
        if (equipmentAvailable.getId() != null && findById(equipmentAvailable.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE equipmentAvailable SET name = ?, quantity = ?, costPerHr = ? WHERE id = ?";
            jdbcTemplate.update(sql, equipmentAvailable.getName(), equipmentAvailable.getQuantity(), equipmentAvailable.getCostPerHr(), equipmentAvailable.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO equipmentAvailable (name, quantity, costPerHr) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, equipmentAvailable.getName(), equipmentAvailable.getQuantity(), equipmentAvailable.getCostPerHr());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            equipmentAvailable.setId(generatedId);
        }
        return equipmentAvailable;
    }


    // Delete equipment by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM equipmentAvailable WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Check if equipment exists by ID
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM equipmentAvailable WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }

    // Example of a custom query: Find equipment by name
    public List<EquipmentAvailable> findByName(String name) {
        String sql = "SELECT * FROM equipmentAvailable WHERE name = ?";
        return jdbcTemplate.query(sql, new Object[]{name}, equipmentAvailableRowMapper);
    }
}
