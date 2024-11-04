package com.example.dbms.repository;

import com.example.dbms.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AdminRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Admin entity
    private final RowMapper<Admin> adminRowMapper = new RowMapper<>() {
        @Override
        public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
            Admin admin = new Admin();
            admin.setId(rs.getInt("id"));
            admin.setUsername(rs.getString("username"));
            admin.setPassword(rs.getString("password"));
            // Map other fields as needed
            return admin;
        }
    };

    public Optional<Admin> findByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try {
            Admin admin = jdbcTemplate.queryForObject(sql, new Object[]{username}, adminRowMapper);
            return Optional.ofNullable(admin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Admin> findById(Integer id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
        try {
            Admin admin = jdbcTemplate.queryForObject(sql, new Object[]{id}, adminRowMapper);
            return Optional.ofNullable(admin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Admin> findAll() {
        String sql = "SELECT * FROM admin";
        return jdbcTemplate.query(sql, adminRowMapper);
    }

    public Admin save(Admin admin) {
        if (admin.getId() != null && findById(admin.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE admin SET username = ?, password = ? WHERE id = ?";
            jdbcTemplate.update(sql, admin.getUsername(), admin.getPassword(), admin.getId());
        } else {
            // Insert new record
            String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
            jdbcTemplate.update(sql, admin.getUsername(), admin.getPassword());
        }
        return admin;
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM admin WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
