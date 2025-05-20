package com.saasant.firstSpringProject.service;
import com.saasant.firstSpringProject.vo.CustomerDetails;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saasant.firstSpringProject.FirstSpringProjectApplication;
import com.saasant.firstSpringProject.dao.CustomerDaoInterface;
import com.saasant.firstSpringProject.exception.CustomerNotFoundException;

@Service
public class CustomerService implements CustomerServiceInterface {
	
	private final CustomerDaoInterface customerDao;
	private static final Logger log = LoggerFactory.getLogger(FirstSpringProjectApplication.class);
	
	@Autowired
	public CustomerService(CustomerDaoInterface customerDao){
		this.customerDao = customerDao;
	}
	
	@Override
	public CustomerDetails addCustomer(CustomerDetails customer) {
		log.info("Attempting to add customer: {}", customer != null ? customer.getCustomerId() : "null");
		if (customer == null || customer.getCustomerId() == null || customer.getCustomerId().trim().isEmpty()) {
			log.warn("Customer or Customer ID cannot be null or empty.");
			System.out.println("❌ Customer or Customer ID cannot be null or empty.");
			return null;
		}
		boolean success = customerDao.addCustomer(customer);
		if(success) {
			return customerDao.getCustomerById(customer.getCustomerId());
		}
		log.error("Failed to add customer using DAO: {}", customer.getCustomerId());
		System.out.println("Failed to add customer: " + customer.getCustomerId());
		return null;
	}

	@Override
	public CustomerDetails updateCustomer(String customerId, CustomerDetails customerUpdates) {
		log.info("Attempting to update customer with ID: {}", customerId);
		if (customerId == null || customerId.trim().isEmpty() || customerUpdates == null) {
            System.out.println("Customer ID for update or customer data cannot be null or empty.");
            return null;
        }
		
        CustomerDetails existingCustomer = customerDao.getCustomerById(customerId);
        if (existingCustomer == null) {
        	log.warn("Customer not found with ID: {}. Cannot update.", customerId);
            System.out.println("Customer not found with ID: " + customerId + ". Cannot update.");
            throw new CustomerNotFoundException(customerId);
        }

        customerUpdates.setCustomerId(customerId); 
        
        boolean success = customerDao.updateCustomer(customerUpdates);
        if (success) {
        	log.info("✅ Customer updated successfully");
        	return customerDao.getCustomerById(customerId);
        }
        log.error("Failed to update customer using DAO: {}", customerId);
        System.out.println("Failed to update customer: " + customerId);
        return null;
	}

	@Override
	public void deleteCustomer(String customerId) {
		log.info("Attempting to delete customer with ID: {}", customerId);
		if (customerId == null || customerId.trim().isEmpty()) {
			log.warn("Customer ID for deletion cannot be null or empty.");
            System.out.println("Customer ID for deletion cannot be null or empty.");
            return;
        }
		
		CustomerDetails existingCustomer = customerDao.getCustomerById(customerId); 
        if (existingCustomer == null) {
            log.warn("Customer not found with ID: {}. Cannot delete.", customerId);
            throw new CustomerNotFoundException(customerId);
        }
        if (customerDao.deleteCustomer(customerId)) {
        	log.info("Customer deleted successfully: {}", customerId);
            System.out.println("✅ Customer deleted successfully: " + customerId);
        } else {
        	log.warn("Failed to delete customer or customer not found with ID: {}", customerId);
            System.out.println("Failed to delete customer or customer not found: " + customerId);
        }
	}

	@Override
	public List<CustomerDetails> getAllCustomers() {
		log.info("Fetching all customers.");
		List<CustomerDetails> customers = customerDao.getAllCustomers();
		log.debug("Found {} customers.", customers.size());
		return customers;
	}

	@Override
	public CustomerDetails getCustomerById(String customerId) {
		log.info("Fetching customer by ID: {}", customerId);
		 if (customerId == null || customerId.trim().isEmpty()) {
			 log.warn("Customer ID for retrieval cannot be null or empty.");
            System.out.println("Customer ID for retrieval cannot be null or empty.");
            return null;
        }
		CustomerDetails customer = customerDao.getCustomerById(customerId);
		if(customer == null) {
			log.warn("No customer founud with ID: {}",customerId);
			throw new CustomerNotFoundException(customerId);
		}
		return customer;
	}
	
	@Override
    public List<CustomerDetails> searchCustomers(String query) {
        log.info("Service: Searching customers with query: '{}'", query);
        List<CustomerDetails> customers = customerDao.searchCustomers(query);
        log.debug("Service: Found {} customers for query: '{}'", customers.size(), query);
        return customers;
    }
}
