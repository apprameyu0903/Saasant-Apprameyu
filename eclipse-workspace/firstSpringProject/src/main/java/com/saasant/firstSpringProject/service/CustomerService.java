package com.saasant.firstSpringProject.service;
import com.saasant.firstSpringProject.vo.CustomerDetails;
import java.util.*;

import org.springframework.stereotype.Service;

import com.saasant.firstSpringProject.dao.CustomerDao;

@Service
public class CustomerService implements CustomerServiceInterface{
	
	private CustomerDao customerDao;
	
	
	public CustomerService(){
		customerDao = new CustomerDao();
	}
	
	

	@Override
	public CustomerDetails addCustomer(CustomerDetails customer) {
		boolean success = customerDao.addCustomer(customer);
		if(success) {
			return customerDao.getCustomerById(customer.getCustomerId());
		}
		return null;
	}

	@Override
	public CustomerDetails updateCustomer(String customerId, CustomerDetails customer) {
		// TODO Auto-generated method stub
        CustomerDetails existing = customerDao.getCustomerById(customerId);
        if (existing == null) {
            System.out.println("Customer not found.");
        }
        customer.setCustomerId(customerId);
        boolean success = customerDao.updateCustomer(customer);
        if (success) {
        	return customerDao.getCustomerById(customerId);
        }
        return null;
		
	}

	@Override
	public void deleteCustomer(String customerId) {
		// TODO Auto-generated method stub

        if (customerDao.deleteCustomer(customerId)) {
            System.out.println("✅ Customer deleted.");
        } else {
            System.out.println("❌ Failed to delete or customer not found.");
        }
		
	}

	@Override
	public List<CustomerDetails> getAllCustomers() {
		// TODO Auto-generated method stub
		return customerDao.getAllCustomers();
	}

	public CustomerDetails getCustomerById(String CustomerId) {
		// TODO Auto-generated method stub
		return customerDao.getCustomerById(CustomerId);
		
	}
	
	
	
	
}
	

