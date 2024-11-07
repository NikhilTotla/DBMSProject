package com.example.dbms.repository;

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
public class VisitorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Visitor entity
    private final RowMapper<Visitor> visitorRowMapper = new RowMapper<Visitor>() {
        @Override
        public Visitor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Visitor visitor = new Visitor();
            visitor.setId(rs.getInt("id"));
            visitor.setFirstName(rs.getString("firstName"));
            visitor.setLastName(rs.getString("lastName"));
            visitor.setEmail(rs.getString("email"));
            visitor.setPhone(rs.getString("phone"));
            return visitor;
        }
    };

    // Find a Visitor by their email
    public Optional<Visitor> findByEmail(String email) {
        String sql = "SELECT * FROM visitor WHERE email = ?";
        try {
            Visitor visitor = jdbcTemplate.queryForObject(sql, new Object[]{email}, visitorRowMapper);
            return Optional.of(visitor);
        } catch (Exception e) {
            return Optional.empty(); // Return empty if no visitor is found
        }
    }

    // Save or update a Visitor record
    public Visitor save(Visitor visitor) {
        if (visitor.getId() != null && findById(visitor.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE visitor SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE id = ?";
            jdbcTemplate.update(sql, visitor.getFirstName(), visitor.getLastName(), visitor.getEmail(),
                    visitor.getPhone(), visitor.getId());
        } else {
            // Insert new record and get the generated ID
            String sql = "INSERT INTO visitor (firstName, lastName, email, phone) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, visitor.getFirstName(), visitor.getLastName(), visitor.getEmail(), visitor.getPhone());

            // Fetch the generated ID (this assumes the use of MySQL or a similar DB that supports LAST_INSERT_ID)
            String getGeneratedIdSql = "SELECT LAST_INSERT_ID()";
            Integer generatedId = jdbcTemplate.queryForObject(getGeneratedIdSql, Integer.class);

            // Set the generated ID on the visitor object
            visitor.setId(generatedId);
        }
        return visitor;
    }


    // Find a Visitor by their ID
    public Optional<Visitor> findById(Integer id) {
        String sql = "SELECT * FROM visitor WHERE id = ?";
        try {
            Visitor visitor = jdbcTemplate.queryForObject(sql, new Object[]{id}, visitorRowMapper);
            return Optional.of(visitor);
        } catch (Exception e) {
            return Optional.empty(); // Return empty if no visitor is found
        }
    }

    // Delete Visitor by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM visitor WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Find all Visitors
    public List<Visitor> findAll() {
        String sql = "SELECT * FROM visitor";
        return jdbcTemplate.query(sql, visitorRowMapper);
    }
}
