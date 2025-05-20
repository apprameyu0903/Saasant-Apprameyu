package com.saasant.firstSpringProject.dao;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.saasant.firstSpringProject.entity.Customers;
import com.saasant.firstSpringProject.repo.CustomerRepository;
import com.saasant.firstSpringProject.util.DBUtil;
import com.saasant.firstSpringProject.vo.CustomerDetails;

import java.sql.*;

@Service
public class CustomerDao implements CustomerDaoInterface{

    private static final Logger log = LoggerFactory.getLogger(CustomerDao.class);
    
    @Autowired
    CustomerRepository customerRepository;
    

    public boolean addCustomer(CustomerDetails customer) {
        String sqlSelect = "SELECT customer_id FROM customers WHERE customer_id = ?";
        String sqlInsert = "insert into customers (customer_id, customer_name, customer_mobile, customer_location) values (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {

            stmtSelect.setString(1, customer.getCustomerId());
            try (ResultSet rs = stmtSelect.executeQuery()) {
                if (rs.next()) {
                    log.warn("Customer ID {} already exists. Cannot add.", customer.getCustomerId());
                    System.out.println("Customer ID already exists.");
                    return false;
                }
            }
            try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                stmtInsert.setString(1, customer.getCustomerId());
                stmtInsert.setString(2, customer.getCustomerName());
                stmtInsert.setString(3, customer.getMobileNumber());
                stmtInsert.setString(4, customer.getCustomerLocation());

                int rows = stmtInsert.executeUpdate();
                if (rows > 0) {
                    log.info("Successfully added customer {} to database.", customer.getCustomerId());
                    return true;
                } else {
                    log.warn("No rows affected when trying to add customer {}.", customer.getCustomerId());
                    return false;
                }
            }

        } catch (SQLException e) {
            log.error("SQL Error in addCustomer for ID {}: {}", customer.getCustomerId(), e.getMessage(), e);
            System.out.println("Error in the DB Connectivity");
        }
        return false;
    }

    public CustomerDetails getCustomerById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        log.debug("DAO: Executing SQL to get customer by ID: {} with query: {}", customerId, sql);
        try (Connection conn = DBUtil.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            CustomerDetails customer = new CustomerDetails(); 
            if (rs.next()) {
                customer.setCustomerId(customerId);
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setMobileNumber(rs.getString("customer_mobile"));
                customer.setCustomerLocation(rs.getString("customer_location"));
                log.debug("DAO: Customer found for ID: {}", customerId);
                return customer;
            } else {
                log.debug("DAO: No customer found for ID: {}", customerId);
            }

        } catch (SQLException e) {
            log.error("SQL Error in getCustomerById for ID {}: {}", customerId, e.getMessage(), e);
            System.out.println("Error in the DB Connectivity");
        }
        return null;
    }

    public boolean updateCustomer(CustomerDetails customer) {
        String sql = "UPDATE customers SET customer_name = ?, customer_mobile = ?, customer_location = ? WHERE customer_id = ?";
        log.debug("DAO: Executing SQL to update customer: {} with query: {}", customer.getCustomerId(), sql);
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getMobileNumber());
            stmt.setString(3, customer.getCustomerLocation()); 
            stmt.setString(4, customer.getCustomerId()); 
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                log.info("Successfully updated customer {} in database.", customer.getCustomerId());
                return true;
            } else {
                log.warn("No rows affected when trying to update customer {}. Customer might not exist or data is the same.", customer.getCustomerId());
                return false;
            }

        } catch (SQLException e) {
            log.error("SQL Error in updateCustomer for ID {}: {}", customer.getCustomerId(), e.getMessage(), e);
            System.out.println("Error in the DB Connectivity");
        }
        return false;
    }
   
    public boolean deleteCustomer(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        log.debug("DAO: Executing SQL to delete customer: {} with query: {}", customerId, sql);
        try (Connection conn = DBUtil.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            int rows = stmt.executeUpdate();
             if (rows > 0) {
                log.info("Successfully deleted customer {} from database.", customerId);
                return true;
            } else {
                log.warn("No rows affected when trying to delete customer {}. Customer might not exist.", customerId);
                return false;
            }
        } catch (SQLException e) {
            log.error("SQL Error in deleteCustomer for ID {}: {}", customerId, e.getMessage(), e);
            System.out.println("Error in the DB Connectivity");
        }
        return false;
    }

    public List<CustomerDetails> getAllCustomers() {
        List<CustomerDetails> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        log.debug("DAO: Executing SQL to get all customers: {}", sql);

        try (Connection conn = DBUtil.getInstance().getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CustomerDetails customer = new CustomerDetails(); 
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setMobileNumber(rs.getString("customer_mobile"));
                customer.setCustomerLocation(rs.getString("customer_location"));
                customers.add(customer);
            }
            log.debug("DAO: Retrieved {} customers.", customers.size());

        } catch (SQLException e) {
            log.error("SQL Error in getAllCustomers: {}", e.getMessage(), e);
            System.out.println("Error in the DB Connectivity");
        }
        return customers;
    }
    
    public List<CustomerDetails> searchCustomers(String searchTerm) {
        List<CustomerDetails> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE customer_id LIKE ? OR customer_name LIKE ? OR customer_mobile LIKE ? OR customer_location LIKE ? ORDER BY customer_name ASC";
        log.debug("DAO: Executing SQL to search customers with term '{}': {}", searchTerm, sql);
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String queryParam = "%" + searchTerm + "%";
            stmt.setString(1, queryParam);
            stmt.setString(2, queryParam);
            stmt.setString(3, queryParam);
            stmt.setString(4, queryParam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(new CustomerDetails(
                    rs.getString("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_mobile"),
                    rs.getString("customer_location")
                ));
            }
            log.debug("DAO: Found {} customers for search term '{}'", customers.size(), searchTerm);
        } catch (SQLException e) {
            log.error("SQL Error in searchCustomers for term '{}': {}", searchTerm, e.getMessage(), e);
            System.out.println("Error in the DB Connectivity during searchCustomers");
        }
        return customers;
    }
    

}