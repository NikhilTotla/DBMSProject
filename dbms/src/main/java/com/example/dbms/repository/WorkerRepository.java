package com.example.dbms.repository;

import com.example.dbms.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class WorkerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map SQL results to Worker entity
    private final RowMapper<Worker> workerRowMapper = new RowMapper<Worker>() {
        @Override
        public Worker mapRow(ResultSet rs, int rowNum) throws SQLException {
            Worker worker = new Worker();
            worker.setId(rs.getInt("id"));
            worker.setFirstName(rs.getString("firstName"));
            worker.setLastName(rs.getString("lastName"));
            worker.setGender(rs.getString("gender"));
            worker.setDob(rs.getDate("dob").toLocalDate());
            worker.setEmail(rs.getString("email"));
            worker.setUsername(rs.getString("username"));
            worker.setPhone(rs.getString("phone"));
            worker.setAddress(rs.getString("address"));
            worker.setSalary(rs.getInt("salary"));
            worker.setPosition(rs.getString("position"));
            worker.setDept(rs.getInt("dept"));
            worker.setManagedBy(rs.getInt("managedBy"));
            worker.setPassword(rs.getString("password"));
            return worker;
        }
    };

    // Find Worker by username
    public Optional<Worker> findByUsername(String username) {
        String sql = "SELECT * FROM worker WHERE username = ?";
        try {
            Worker worker = jdbcTemplate.queryForObject(sql, new Object[]{username}, workerRowMapper);
            return Optional.of(worker); // Return the worker wrapped in Optional if found
        } catch (Exception e) {
            // Return an empty Optional if not found
            return Optional.empty();
        }
    }

    // Find Worker by id
    public Worker findById(Integer id) {
        String sql = "SELECT * FROM worker WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, workerRowMapper);
        } catch (Exception e) {
            // Handle the case where no worker is found
            return null; // Return null if not found
        }
    }

    // Save or update a Worker record
    public Worker save(Worker worker) {
        if (worker.getId() != null && findById(worker.getId()) != null) {
            // Update existing worker record
            String sql = "UPDATE worker SET firstName = ?, lastName = ?, gender = ?, dob = ?, email = ?, " +
                    "username = ?, phone = ?, address = ?, salary = ?, position = ?, dept = ?, managedBy = ?, password = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sql, worker.getFirstName(), worker.getLastName(), worker.getGender(), worker.getDob(),
                    worker.getEmail(), worker.getUsername(), worker.getPhone(), worker.getAddress(),
                    worker.getSalary(), worker.getPosition(), worker.getDept(), worker.getManagedBy(), worker.getPassword(),
                    worker.getId());
        } else {
            // Insert new worker record
            String sql = "INSERT INTO worker (firstName, lastName, gender, dob, email, username, phone, address, salary, " +
                    "position, dept, managedBy, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, worker.getFirstName(), worker.getLastName(), worker.getGender(), worker.getDob(),
                    worker.getEmail(), worker.getUsername(), worker.getPhone(), worker.getAddress(), worker.getSalary(),
                    worker.getPosition(), worker.getDept(), worker.getManagedBy(), worker.getPassword());
        }
        return worker;
    }

    // Delete Worker by id
    public void deleteById(Integer id) {
        String sql = "DELETE FROM worker WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Find all workers
    public List<Worker> findAll() {
        String sql = "SELECT * FROM worker";
        return jdbcTemplate.query(sql, workerRowMapper);
    }
    public boolean existsById(Integer id) {
        String sql = "SELECT COUNT(*) FROM worker WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;  // Return true if a record with the given id exists, otherwise false
    }
}
