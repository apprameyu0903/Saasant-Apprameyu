package com.thirdspring1905.service;

import com.thirdspring1905.dao.EmployeeDAO;
import com.thirdspring1905.exception.DataNotFoundException;
import com.thirdspring1905.exception.BusinessException;

import com.thirdspring1905.model.Employee;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//import java.util.UUID;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    /** one ID for the whole app run, generated once when Spring bootstraps this bean **/
    private static final String APP_TX_ID = UUID.randomUUID().toString();
    /** record start‑time when bean is created **/
    private static final long   START_MS  = System.currentTimeMillis();
    private final EmployeeDAO dao;

    @Autowired
    public EmployeeServiceImpl(EmployeeDAO dao) {
        this.dao = dao;
    }

    /** runs immediately after construction **/
    @PostConstruct
    public void init() {
        MDC.put("txId", APP_TX_ID);
        logger.info(">>> Application start [{}] at {} ms since epoch", APP_TX_ID, START_MS);
        // leave MDC set so any early logs from service/DAO carry the same ID
    }

    /** runs just before bean destruction (i.e. app shutdown) **/
    @PreDestroy
    public void shutdown() {
        long elapsed = System.currentTimeMillis() - START_MS;
        MDC.put("txId", APP_TX_ID);
        logger.info("<<< Application stop [{}], total uptime = {} ms", APP_TX_ID, elapsed);
        MDC.clear();
    }
    
    @Override
    public void create(Employee emp) {
        //String txId = UUID.randomUUID().toString();
        logger.info("Creating employee: [{}]", emp.getEmpId());
        
        if (dao.existsById(emp.getEmpId())) {
            logger.error("Employee ID already exists: [{}]", emp.getEmpId());
            throw new BusinessException("Employee ID already exists");
        }
        dao.insertEmployee(emp);
    }

    @Override
    public boolean update(Employee emp) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Updating employee: [{}]", emp.getEmpId());
        
        if (!dao.existsById(emp.getEmpId())) {
            logger.error("Employee not found for update: [{}]", emp.getEmpId());
            throw new DataNotFoundException("Employee not found with ID: " + emp.getEmpId());
        }
        return dao.updateEmployee(emp);
    }

    @Override
    public boolean delete(String id) {
       // String txId = UUID.randomUUID().toString();
        logger.info("Deleting employee: [{}]", id);
        
        if (!dao.existsById(id)) {
            logger.error("Employee not found for deletion: [{}]", id);
            throw new DataNotFoundException("Employee not found with ID: " + id);
        }
        return dao.deleteEmployeeById(id);
    }

    @Override
    public List<Employee> getAll() {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Getting all employees in the database");
        
        List<Employee> employees = dao.findAll();
        if (employees.isEmpty()) {
            logger.warn("No employees found");
        }
        return employees;
    }

    @Override
    public Employee getById(String id) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Getting employee by ID: [{}]", id);
        
        Employee employee = dao.findById(id);
        if (employee == null) {
            logger.error("Employee not found: [{}]", id);
            throw new DataNotFoundException("Employee not found with ID: " + id);
        }
        return employee;
    }

    @Override
    public boolean existsById(String empId) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Checking employee existence: [{}]", empId);
        
        return dao.existsById(empId);
    }
}