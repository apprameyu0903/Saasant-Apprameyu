package com.saasant.firstSpringProject.service;

import java.util.List;

import com.saasant.firstSpringProject.vo.CustomerDetails;
import com.saasant.firstSpringProject.entity.Customers;

public interface CustomerServiceInterface {
	
	CustomerDetails addCustomer(CustomerDetails customer);
	CustomerDetails updateCustomer(String customerId, CustomerDetails customer);
	void deleteCustomer(String customerId);
	CustomerDetails getCustomerById(String CustomerId);
	List<CustomerDetails> getAllCustomers();

}
