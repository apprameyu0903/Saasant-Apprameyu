package com.service;
import com.vo.*;
import java.util.*;
import com.dao.CustomerDao;

public class CustomerService implements CustomerServiceInterface{
	
	private CustomerDao customerDao;
	private Scanner sc = new Scanner(System.in);
	
	public CustomerService(){
		customerDao = new CustomerDao();
	}
	
	

	@Override
	public CustomerDetails addCustomer() {
		CustomerDetails newCustomer;
		System.out.print("Enter Customer id: ");
		String id = sc.nextLine();
        if (customerDao.getCustomerById(id) != null) {
        	newCustomer = customerDao.getCustomerById(id);
        	System.out.println("✔️ Existing customer found: " + newCustomer.getCustomerName() + " (" + newCustomer.getMobileNumber() + ") " + "Location: " + newCustomer.getCustomerLocation());
        }else {
	        System.out.print("Enter Name: ");
	        String name = sc.nextLine();
	        System.out.print("Enter Mobile Number: ");
	        String mobile = sc.nextLine();
	        while (mobile.length() < 10) {
	            System.out.print("Please enter a valid 10-digit mobile number: ");
	            mobile = sc.nextLine();
	        }
            System.out.print("Enter Customer Location: ");
            String location = sc.nextLine();
	        newCustomer = new CustomerDetails(id, name, mobile, location);
	        if (customerDao.addCustomer(newCustomer)) {
	            System.out.println("✅ Customer added successfully!");
	        } else {
	            System.out.println("❌ Failed to add customer.");
	        }
        }
        return newCustomer;
	}

	@Override
	public void updateCustomer(String customerId) {
		// TODO Auto-generated method stub
        CustomerDetails existing = customerDao.getCustomerById(customerId);
        if (existing == null) {
            System.out.println("❌ Customer not found.");
        }else {
	        System.out.print("Enter New Name (current: " + existing.getCustomerName() + "): ");
	        String newName = sc.nextLine();
	        System.out.print("Enter New Mobile (current: " + existing.getMobileNumber() + "): ");
	        String newMobile = sc.nextLine();
	        while (newMobile.length() < 10) {
	            System.out.print("Please enter a valid 10-digit mobile number: ");
	            newMobile = sc.nextLine();
	        }
            System.out.print("Enter New Customer Location: ");
            String newLocation = sc.nextLine();
	        CustomerDetails updatedCustomer = new CustomerDetails(customerId, newName, newMobile, newLocation);
	        if (customerDao.updateCustomer(updatedCustomer)) {
	            System.out.println("✅ Customer updated successfully!");
	        } else {
	            System.out.println("❌ Update failed.");
	        }
        }
		
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
	

