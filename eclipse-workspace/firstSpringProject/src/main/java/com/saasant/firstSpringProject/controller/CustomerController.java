package com.saasant.firstSpringProject.controller;

import com.saasant.firstSpringProject.exception.CustomerNotFoundException;
import com.saasant.firstSpringProject.service.CustomerServiceInterface;
import com.saasant.firstSpringProject.vo.CustomerDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/customers") 
public class CustomerController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerServiceInterface customerService;
	
	@Autowired
	private CustomerDetails customer;

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDetails> getCustomerById(@PathVariable String customerId) {
    	log.info("API: Request to get customer by ID: {}", customerId);
        customer = customerService.getCustomerById(customerId);
        if (customer != null && customer.getCustomerId() != null) {
        	log.debug("API: Customer found: {}", customerId);
            return ResponseEntity.ok(customer);
        } else {
        	log.warn("API: Customer not found for ID: {}", customerId);
        	throw new CustomerNotFoundException(customerId);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDetails>> getAllOrSearchCustomers(@RequestParam(name = "query", required = false) String searchQuery) {
        log.info("API: Request to get customers. Query: '{}'", searchQuery);
        List<CustomerDetails> customers;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            customers = customerService.searchCustomers(searchQuery.trim());
            log.debug("API: Returning {} customers based on search query: '{}'", customers.size(), searchQuery);
        } else {
            customers = customerService.getAllCustomers();
            log.debug("API: Returning all {} customers (no query).", customers.size());
        }
        return ResponseEntity.ok(customers);
    }
    
    @PostMapping
    public ResponseEntity<CustomerDetails> addCustomer(@RequestBody CustomerDetails customerDetails) {
    	log.info("API: Request to add new customer: {}", customerDetails.getCustomerId());
        CustomerDetails newCustomer = customerService.addCustomer(customerDetails);
        if (newCustomer != null && newCustomer.getCustomerId() != null) {
        	log.info("API: Customer added successfully with ID: {}", newCustomer.getCustomerId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
        } else {
        	log.warn("API: Failed to add customer. Input: {}", customerDetails.getCustomerId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDetails> updateCustomer(@PathVariable String customerId, @RequestBody CustomerDetails customerDetails) {
    	log.info("API: Request to update customer with ID: {}", customerId);
        CustomerDetails updatedCustomer = customerService.updateCustomer(customerId, customerDetails);
        if (updatedCustomer != null) {
        	log.info("API: Customer updated successfully: {}", customerId);
            return ResponseEntity.ok(updatedCustomer);
        } else {
        	log.warn("API: Failed to update customer or customer not found: {}", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String customerId) {
    	log.info("API: Request to delete customer with ID: {}", customerId);
        CustomerDetails existingCustomer = customerService.getCustomerById(customerId);
        if (existingCustomer == null) {
        	log.warn("API: Customer not found for deletion: {}", customerId);
        	throw new CustomerNotFoundException(customerId);
        }
        
        customerService.deleteCustomer(customerId);
        log.info("API: Customer deletion processed for ID: {}", customerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerId + "Deleted");
    }
}