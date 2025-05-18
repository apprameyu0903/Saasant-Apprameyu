package com.dao;
import java.util.*;
import com.util.DBUtil; //
import com.vo.CustomerDetails; //

import java.sql.*;

public class CustomerDao implements CustomerDaoInterface{

    public boolean addCustomer(CustomerDetails customer) {
        String sql = "insert into customers (customer_id, customer_name, customer_mobile, customer_location) values (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (getCustomerById(customer.getCustomerId()) != null) {
                System.out.println("❗ Customer ID already exists.");
                return false;
            }

            stmt.setString(1, customer.getCustomerId());
            stmt.setString(2, customer.getCustomerName());
            stmt.setString(3, customer.getMobileNumber());
            stmt.setString(4, customer.getCustomerLocation());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity");
        }
        return false;
    }

    public CustomerDetails getCustomerById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
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
                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity");
        }
        return null;
    }

    public boolean updateCustomer(CustomerDetails customer) {
        String sql = "UPDATE customers SET customer_name = ?, customer_mobile = ?, customer_location = ? WHERE customer_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getMobileNumber());
            stmt.setString(3, customer.getCustomerLocation()); 
            stmt.setString(4, customer.getCustomerId()); 
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity");
        }
        return false;
    }

    public boolean deleteCustomer(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DBUtil.getInstance().getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity");
        }
        return false;
    }

    public List<CustomerDetails> getAllCustomers() {
        List<CustomerDetails> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DBUtil.getInstance().getConnection(); //
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CustomerDetails customer = new CustomerDetails(); //
                customer.setCustomerId(rs.getString("customer_id"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setMobileNumber(rs.getString("customer_mobile"));
                customer.setCustomerLocation(rs.getString("customer_location"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity");
        }
        return customers;
    }
    
    public List<CustomerDetails> searchCustomers(String searchTerm) {
        List<CustomerDetails> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE customer_id LIKE ? OR customer_name LIKE ? OR customer_mobile LIKE ? OR customer_location LIKE ? ORDER BY customer_name ASC";
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in the DB Connectivity during searchCustomers");
        }
        return customers;
    }
}