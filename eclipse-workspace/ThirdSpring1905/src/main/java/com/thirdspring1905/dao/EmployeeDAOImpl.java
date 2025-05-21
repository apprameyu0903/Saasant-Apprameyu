package com.thirdspring1905.dao;

import com.thirdspring1905.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//import java.util.UUID;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAOImpl.class);
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertEmployee(Employee emp) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Inserting employee: [{}] ", emp.getEmpId());
        
        String sql = "INSERT INTO empformservlet(emp_id, emp_name, position, phone) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, emp.getEmpId(), emp.getEmpName(), emp.getPosition(), emp.getPhone());
    }

    @Override
    public boolean updateEmployee(Employee emp) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Updating employee: [{}] ", emp.getEmpId());
        
        String sql = "UPDATE empformservlet SET emp_name = ?, position = ?, phone = ? WHERE emp_id = ?";
        int rows = jdbcTemplate.update(sql, emp.getEmpName(), emp.getPosition(), emp.getPhone(), emp.getEmpId());
        return rows > 0;
    }

    @Override
    public boolean deleteEmployeeById(String empId) {
       // String txId = UUID.randomUUID().toString();
        logger.info("Deleting employee: [{}] ", empId);
        
        String sql = "DELETE FROM empformservlet WHERE emp_id = ?";
        int rows = jdbcTemplate.update(sql, empId);
        return rows > 0;
    }

    @Override
    public List<Employee> findAll() {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Fetching all employees");
        
        String sql = "SELECT emp_id, emp_name, position, phone FROM empformservlet";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    @Override
    public Employee findById(String empId) {
     //   String txId = UUID.randomUUID().toString();
        logger.info("Finding employee by ID: [{}] ", empId);
        
        String sql = "SELECT emp_id, emp_name, position, phone FROM empformservlet WHERE emp_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), empId);
        } catch (Exception e) {
            logger.error("Employee not found: [{}] ",empId);
            return null;
        }
    }

    @Override
    public boolean existsById(String empId) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Checking if employee exists: [{}] ", empId);
        
        String sql = "SELECT COUNT(*) FROM empformservlet WHERE emp_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, empId);
        return count != null && count > 0;
    }

    private static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee e = new Employee();
            e.setEmpId(rs.getString("emp_id"));
            e.setEmpName(rs.getString("emp_name"));
            e.setPosition(rs.getString("position"));
            e.setPhone(rs.getString("phone"));
            return e;
        }
    }
}