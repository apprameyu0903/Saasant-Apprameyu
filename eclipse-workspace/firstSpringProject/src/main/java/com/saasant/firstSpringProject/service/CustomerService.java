package com.saasant.firstSpringProject.service;
import com.saasant.firstSpringProject.vo.CustomerDetails;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.saasant.firstSpringProject.dao.CustomerDaoInterface;

@Service
public class CustomerService implements CustomerServiceInterface {
	
	private final CustomerDaoInterface customerDao;
	
	@Autowired
	public CustomerService(CustomerDaoInterface customerDao){
		this.customerDao = customerDao;
	}
	
	@Override
	public CustomerDetails addCustomer(CustomerDetails customer) {
		if (customer == null || customer.getCustomerId() == null || customer.getCustomerId().trim().isEmpty()) {
			System.out.println("❌ Customer or Customer ID cannot be null or empty.");
			return null;
		}
		boolean success = customerDao.addCustomer(customer);
		if(success) {
			return customerDao.getCustomerById(customer.getCustomerId());
		}
		System.out.println("❌ Failed to add customer: " + customer.getCustomerId());
		return null;
	}

	@Override
	public CustomerDetails updateCustomer(String customerId, CustomerDetails customerUpdates) {
		if (customerId == null || customerId.trim().isEmpty() || customerUpdates == null) {
            System.out.println("❌ Customer ID for update or customer data cannot be null or empty.");
            return null;
        }
		
        CustomerDetails existingCustomer = customerDao.getCustomerById(customerId);
        if (existingCustomer == null) {
            System.out.println("❌ Customer not found with ID: " + customerId + ". Cannot update.");
            return null;
        }

        customerUpdates.setCustomerId(customerId); 
        
        boolean success = customerDao.updateCustomer(customerUpdates);
        if (success) {
        	return customerDao.getCustomerById(customerId);
        }
        System.out.println("❌ Failed to update customer: " + customerId);
        return null;
	}

	@Override
	public void deleteCustomer(String customerId) {
		if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println("❌ Customer ID for deletion cannot be null or empty.");
            return;
        }
        if (customerDao.deleteCustomer(customerId)) {
            System.out.println("✅ Customer deleted successfully: " + customerId);
        } else {
            System.out.println("❌ Failed to delete customer or customer not found: " + customerId);
        }
	}

	@Override
	public List<CustomerDetails> getAllCustomers() {
		return customerDao.getAllCustomers();
	}

	@Override
	public CustomerDetails getCustomerById(String customerId) {
		 if (customerId == null || customerId.trim().isEmpty()) {
            System.out.println("❌ Customer ID for retrieval cannot be null or empty.");
            return null;
        }
		return customerDao.getCustomerById(customerId);
	}
}
