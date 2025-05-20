package com.saasant.firstSpringProject.dao;
import com.saasant.firstSpringProject.vo.CustomerDetails;
import com.saasant.firstSpringProject.entity.Customers;
import java.util.List;
public interface CustomerDaoInterface {
	
	boolean addCustomer(CustomerDetails customer);
	CustomerDetails getCustomerById(String customerId);
	boolean updateCustomer(CustomerDetails customer);
	boolean deleteCustomer(String customerId);
	List<CustomerDetails> getAllCustomers();

}
