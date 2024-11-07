package com.example.dbms.repository;

import com.example.dbms.entity.ProjectWorkers;
import com.example.dbms.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProjectWorkersRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for ProjectWorkers entity
    private final RowMapper<ProjectWorkers> projectWorkersRowMapper = new RowMapper<>() {
        @Override
        public ProjectWorkers mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProjectWorkers projectWorkers = new ProjectWorkers();
            projectWorkers.setId(rs.getInt("id"));
            projectWorkers.setProjectId(rs.getInt("projectId"));
            projectWorkers.setWorkerId(rs.getInt("workerId"));
            return projectWorkers;
        }
    };

    // RowMapper for Worker entity
    private final RowMapper<Worker> workerRowMapper = new RowMapper<>() {
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

    // Find all ProjectWorkers by worker ID
    public List<ProjectWorkers> findByWorkerId(Integer workerId) {
        String sql = "SELECT * FROM ProjectWorkers WHERE workerId = ?";
        return jdbcTemplate.query(sql, new Object[]{workerId}, projectWorkersRowMapper);
    }

    // Find all Workers by project ID
    public List<Worker> findWorkersByProjectId(Integer projectId) {
        String sql = "SELECT w.* FROM Worker w " +
                "JOIN ProjectWorkers pw ON w.id = pw.workerId " +
                "WHERE pw.projectId = ?";
        return jdbcTemplate.query(sql, new Object[]{projectId}, workerRowMapper);
    }

    // Save ProjectWorkers (Insert or Update)
    public ProjectWorkers save(ProjectWorkers projectWorkers) {
        if (projectWorkers.getId() != null && findByProjectIdAndWorkerId(projectWorkers.getProjectId(), projectWorkers.getWorkerId()) != null) {
            // Update existing record
            String sql = "UPDATE ProjectWorkers SET projectId = ?, workerId = ? WHERE id = ?";
            jdbcTemplate.update(sql, projectWorkers.getProjectId(), projectWorkers.getWorkerId(), projectWorkers.getId());
        } else {
            // Insert new record
            String sql = "INSERT INTO ProjectWorkers (projectId, workerId) VALUES (?, ?)";
            jdbcTemplate.update(sql, projectWorkers.getProjectId(), projectWorkers.getWorkerId());

            // After insertion, fetch the generated ID and set it on the entity
            String fetchIdSql = "SELECT LAST_INSERT_ID()"; // This gets the last inserted ID
            Integer generatedId = jdbcTemplate.queryForObject(fetchIdSql, Integer.class);
            projectWorkers.setId(generatedId);
        }
        return projectWorkers;
    }


    // Find ProjectWorker by ProjectId and WorkerId
    public ProjectWorkers findByProjectIdAndWorkerId(Integer projectId, Integer workerId) {
        String sql = "SELECT * FROM ProjectWorkers WHERE projectId = ? AND workerId = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{projectId, workerId}, projectWorkersRowMapper);
        } catch (Exception e) {
            return null; // Return null if no record is found
        }
    }

    // Delete ProjectWorker by ID
    public void deleteById(Integer id) {
        String sql = "DELETE FROM ProjectWorkers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
