package com.thirdspring1905.service;

import java.util.List;

import com.thirdspring1905.model.Employee;

public interface EmployeeService {
    void create(Employee emp);
    boolean update(Employee emp);
    boolean delete(String empId);
    List<Employee> getAll();
    Employee getById(String empId);
    boolean existsById(String empId);
}