package com.example.dbms.repository;

import com.example.dbms.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class WarehouseRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Warehouse entity
    private final RowMapper<Warehouse> warehouseRowMapper = new RowMapper<Warehouse>() {
        @Override
        public Warehouse mapRow(ResultSet rs, int rowNum) throws SQLException {
            Warehouse warehouse = new Warehouse();
            warehouse.setId(rs.getInt("id"));
            warehouse.setAddress(rs.getString("address"));
            warehouse.setSize(rs.getInt("size"));
            warehouse.setName(rs.getString("name"));
            return warehouse;
        }
    };

    // Save or update a Warehouse record
    public Warehouse save(Warehouse warehouse) {
        if (warehouse.getId() != null && findById(warehouse.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE warehouse SET address = ?, size = ?, name = ? WHERE id = ?";
            jdbcTemplate.update(sql, warehouse.getAddress(), warehouse.getSize(), warehouse.getName(), warehouse.getId());
        } else {
            // Insert new record
            String sql = "INSERT INTO warehouse (address, size, name) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, warehouse.getAddress(), warehouse.getSize(), warehouse.getName());

            // Fetch the generated ID (if using MySQL, for example)
            String getGeneratedIdSql = "SELECT LAST_INSERT_ID()";
            Integer generatedId = jdbcTemplate.queryForObject(getGeneratedIdSql, Integer.class);
            warehouse.setId(generatedId);  // Set the generated ID
        }
        return warehouse;
    }

    // Find a Warehouse by its ID
    public Optional<Warehouse> findById(Integer id) {
        String sql = "SELECT * FROM warehouse WHERE id = ?";
        try {
            Warehouse warehouse = jdbcTemplate.queryForObject(sql, new Object[]{id}, warehouseRowMapper);
            return Optional.of(warehouse);
        } catch (Exception e) {
            return Optional.empty(); // Return empty if not found
        }
    }

    // Find all Warehouse records
    public List<Warehouse> findAll() {
        String sql = "SELECT * FROM warehouse";
        return jdbcTemplate.query(sql, warehouseRowMapper);
    }

    // Delete Warehouse by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM warehouse WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
