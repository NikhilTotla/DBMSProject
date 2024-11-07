package com.example.dbms.repository;

import com.example.dbms.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Client entity
    private final RowMapper<Client> clientRowMapper = new RowMapper<>() {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getInt("id"));
            client.setUsername(rs.getString("username"));
            client.setFirstName(rs.getString("firstName"));
            client.setLastName(rs.getString("lastName"));
            client.setEmail(rs.getString("email"));
            client.setAddress(rs.getString("address"));
            client.setPhoneNo(rs.getString("phoneNo"));
            client.setPassword(rs.getString("password"));
            return client;
        }
    };

    // Find client by ID
    public Optional<Client> findById(Integer id) {
        String sql = "SELECT * FROM client WHERE id = ?";
        try {
            Client client = jdbcTemplate.queryForObject(sql, new Object[]{id}, clientRowMapper);
            return Optional.ofNullable(client);
        } catch (Exception e) {
            return Optional.empty(); // Return Optional.empty() if client is not found
        }
    }

    // Find client by username
    public Optional<Client> findByUsername(String username) {
        String sql = "SELECT * FROM client WHERE username = ?";
        try {
            Client client = jdbcTemplate.queryForObject(sql, new Object[]{username}, clientRowMapper);
            return Optional.ofNullable(client);
        } catch (Exception e) {
            return Optional.empty(); // Return Optional.empty() if client is not found
        }
    }

    // Find all clients
    public List<Client> findAll() {
        String sql = "SELECT * FROM client";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    // Save or update a client
    public Client save(Client client) {
        if (client.getId() != null && findById(client.getId()).isPresent()) {
            // Update existing record
            String sql = "UPDATE client SET username = ?, firstName = ?, lastName = ?, email = ?, address = ?, phoneNo = ?, password = ? WHERE id = ?";
            jdbcTemplate.update(sql, client.getUsername(), client.getFirstName(), client.getLastName(),
                    client.getEmail(), client.getAddress(), client.getPhoneNo(), client.getPassword(), client.getId());
        } else {
            // Insert new record
            String sql = "INSERT INTO client (username, firstName, lastName, email, address, phoneNo, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, client.getUsername(), client.getFirstName(), client.getLastName(),
                    client.getEmail(), client.getAddress(), client.getPhoneNo(), client.getPassword());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            client.setId(generatedId);
        }
        return client;
    }


    // Delete client by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM client WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Check if client exists by ID
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM client WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
        return count != null && count > 0;
    }
}
