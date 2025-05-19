package com.saasant.firstSpringProject.service;

import com.saasant.firstSpringProject.dao.CustomerDaoInterface; // Use interface
import com.saasant.firstSpringProject.vo.CustomerDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerDaoInterface customerDaoMock; // Use interface type

    private CustomerService customerService;

    private CustomerDetails customer1;
    private CustomerDetails customer2;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerDaoMock);

        customer1 = new CustomerDetails("C001", "Alice Wonderland", "1234567890", "Fantasy Land");
        customer2 = new CustomerDetails("C002", "Bob The Builder", "0987654321", "Construction City");
        
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalOut); 
    }

    @Test
    void testAddCustomer_Success() {
        when(customerDaoMock.addCustomer(any(CustomerDetails.class))).thenReturn(true);
        when(customerDaoMock.getCustomerById(customer1.getCustomerId())).thenReturn(customer1);

        CustomerDetails addedCustomer = customerService.addCustomer(customer1);

        assertNotNull(addedCustomer);
        assertEquals(customer1.getCustomerName(), addedCustomer.getCustomerName());
        verify(customerDaoMock, times(1)).addCustomer(customer1);
        verify(customerDaoMock, times(1)).getCustomerById(customer1.getCustomerId());
        assertFalse(outContent.toString().contains("❌"));
    }

    @Test
    void testAddCustomer_NullCustomer() {
        CustomerDetails result = customerService.addCustomer(null);
        assertNull(result);
        assertTrue(outContent.toString().contains("❌ Customer or Customer ID cannot be null or empty."));
        verify(customerDaoMock, never()).addCustomer(any());
        verify(customerDaoMock, never()).getCustomerById(any());
    }
    
    @Test
    void testAddCustomer_NullCustomerId() {
        CustomerDetails customerWithNullId = new CustomerDetails(null, "Test", "123", "Loc");
        CustomerDetails result = customerService.addCustomer(customerWithNullId);
        assertNull(result);
        assertTrue(outContent.toString().contains("❌ Customer or Customer ID cannot be null or empty."));
         verify(customerDaoMock, never()).addCustomer(any());
    }


    @Test
    void testAddCustomer_DaoAddReturnsFalse() {
        when(customerDaoMock.addCustomer(any(CustomerDetails.class))).thenReturn(false);

        CustomerDetails addedCustomer = customerService.addCustomer(customer1);

        assertNull(addedCustomer);
        assertTrue(outContent.toString().contains("❌ Failed to add customer: C001"));
        verify(customerDaoMock, times(1)).addCustomer(customer1);
        verify(customerDaoMock, never()).getCustomerById(anyString());
    }

    @Test
    void testAddCustomer_DaoAddSucceedsButGetReturnsNull() {
        when(customerDaoMock.addCustomer(any(CustomerDetails.class))).thenReturn(true);
        when(customerDaoMock.getCustomerById(customer1.getCustomerId())).thenReturn(null); // Unexpected scenario

        CustomerDetails addedCustomer = customerService.addCustomer(customer1);

        assertNull(addedCustomer, "Should be null if getCustomerById returns null after supposedly successful add");
        // Depending on implementation, an error might be logged or not if getCustomerById returns null here.
        // The current service doesn't explicitly log an error for this specific case post-add.
        verify(customerDaoMock, times(1)).addCustomer(customer1);
        verify(customerDaoMock, times(1)).getCustomerById(customer1.getCustomerId());
    }

    // --- getCustomerById Tests ---
    @Test
    void testGetCustomerById_Found() {
        when(customerDaoMock.getCustomerById("C001")).thenReturn(customer1);
        CustomerDetails foundCustomer = customerService.getCustomerById("C001");
        assertNotNull(foundCustomer);
        assertEquals("C001", foundCustomer.getCustomerId());
        verify(customerDaoMock, times(1)).getCustomerById("C001");
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerDaoMock.getCustomerById("C999")).thenReturn(null);
        CustomerDetails foundCustomer = customerService.getCustomerById("C999");
        assertNull(foundCustomer);
        verify(customerDaoMock, times(1)).getCustomerById("C999");
    }
    
    @Test
    void testGetCustomerById_NullId() {
        CustomerDetails found = customerService.getCustomerById(null);
        assertNull(found);
        assertTrue(outContent.toString().contains("❌ Customer ID for retrieval cannot be null or empty."));
        verify(customerDaoMock, never()).getCustomerById(any());
    }

    @Test
    void testGetCustomerById_EmptyId() {
        CustomerDetails found = customerService.getCustomerById("   ");
        assertNull(found);
        assertTrue(outContent.toString().contains("❌ Customer ID for retrieval cannot be null or empty."));
        verify(customerDaoMock, never()).getCustomerById(any());
    }


    // --- getAllCustomers Tests ---
    @Test
    void testGetAllCustomers_Success_WithCustomers() {
        List<CustomerDetails> customers = List.of(customer1, customer2);
        when(customerDaoMock.getAllCustomers()).thenReturn(customers);

        List<CustomerDetails> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(customerDaoMock, times(1)).getAllCustomers();
    }

    @Test
    void testGetAllCustomers_Success_NoCustomers() {
        when(customerDaoMock.getAllCustomers()).thenReturn(Collections.emptyList());
        List<CustomerDetails> result = customerService.getAllCustomers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerDaoMock, times(1)).getAllCustomers();
    }

    // --- updateCustomer Tests ---
    @Test
    void testUpdateCustomer_Success() {
        CustomerDetails existingCustomer = new CustomerDetails("C001", "Old Name", "Old Mobile", "Old Location");
        CustomerDetails updatesToApply = new CustomerDetails("IGNORED_ID", "New Name", "New Mobile", "New Location");
        CustomerDetails updatedCustomerFromDb = new CustomerDetails("C001", "New Name", "New Mobile", "New Location");

        when(customerDaoMock.getCustomerById("C001"))
            .thenReturn(existingCustomer) // For the initial check
            .thenReturn(updatedCustomerFromDb); // After successful update

        when(customerDaoMock.updateCustomer(any(CustomerDetails.class))).thenReturn(true);

        CustomerDetails result = customerService.updateCustomer("C001", updatesToApply);

        assertNotNull(result);
        assertEquals("C001", result.getCustomerId());
        assertEquals("New Name", result.getCustomerName());
        
        verify(customerDaoMock, times(2)).getCustomerById("C001");
        verify(customerDaoMock, times(1)).updateCustomer(argThat(customer ->
            "C001".equals(customer.getCustomerId()) && // ID should be set by service
            "New Name".equals(customer.getCustomerName())
        ));
        assertFalse(outContent.toString().contains("❌"));
    }
    
    @Test
    void testUpdateCustomer_NullCustomerId() {
        CustomerDetails updates = new CustomerDetails(null, "Name", "Mob", "Loc");
        CustomerDetails result = customerService.updateCustomer(null, updates);
        assertNull(result);
        assertTrue(outContent.toString().contains("❌ Customer ID for update or customer data cannot be null or empty."));
        verify(customerDaoMock, never()).updateCustomer(any());
    }

    @Test
    void testUpdateCustomer_NullCustomerDetails() {
        CustomerDetails result = customerService.updateCustomer("C001", null);
        assertNull(result);
        assertTrue(outContent.toString().contains("❌ Customer ID for update or customer data cannot be null or empty."));
        verify(customerDaoMock, never()).updateCustomer(any());
    }


    @Test
    void testUpdateCustomer_CustomerNotFound() {
        CustomerDetails updatesToApply = new CustomerDetails(null, "New Name", "New Mobile", "New Location");
        when(customerDaoMock.getCustomerById("C999")).thenReturn(null);

        CustomerDetails result = customerService.updateCustomer("C999", updatesToApply);

        assertNull(result);
        assertTrue(outContent.toString().contains("❌ Customer not found with ID: C999. Cannot update."));
        verify(customerDaoMock, times(1)).getCustomerById("C999");
        verify(customerDaoMock, never()).updateCustomer(any(CustomerDetails.class));
    }

    @Test
    void testUpdateCustomer_DaoUpdateReturnsFalse() {
        CustomerDetails existingCustomer = new CustomerDetails("C001", "Old Name", "Old Mobile", "Old Location");
        CustomerDetails updatesToApply = new CustomerDetails(null, "New Name", "New Mobile", "New Location");

        when(customerDaoMock.getCustomerById("C001")).thenReturn(existingCustomer);
        when(customerDaoMock.updateCustomer(any(CustomerDetails.class))).thenReturn(false);

        CustomerDetails result = customerService.updateCustomer("C001", updatesToApply);

        assertNull(result);
        assertTrue(outContent.toString().contains("❌ Failed to update customer: C001"));
        verify(customerDaoMock, times(1)).getCustomerById("C001");
        verify(customerDaoMock, times(1)).updateCustomer(any(CustomerDetails.class));
    }

    @Test
    void testUpdateCustomer_DaoUpdateSucceedsButGetUpdatedReturnsNull() {
        CustomerDetails existingCustomer = new CustomerDetails("C001", "Old Name", "Old Mobile", "Old Location");
        CustomerDetails updatesToApply = new CustomerDetails(null, "New Name", "New Mobile", "New Location");

        when(customerDaoMock.getCustomerById("C001"))
            .thenReturn(existingCustomer) // For existing check
            .thenReturn(null);            // After supposed update, DAO returns null

        when(customerDaoMock.updateCustomer(any(CustomerDetails.class))).thenReturn(true);

        CustomerDetails result = customerService.updateCustomer("C001", updatesToApply);

        assertNull(result, "Should be null if getCustomerById returns null after successful DAO update");
        verify(customerDaoMock, times(2)).getCustomerById("C001");
        verify(customerDaoMock, times(1)).updateCustomer(any(CustomerDetails.class));
    }

    // --- deleteCustomer Tests ---
    @Test
    void testDeleteCustomer_Success() {
        when(customerDaoMock.deleteCustomer("C001")).thenReturn(true);
        customerService.deleteCustomer("C001");
        assertTrue(outContent.toString().contains("✅ Customer deleted successfully: C001"));
        verify(customerDaoMock, times(1)).deleteCustomer("C001");
    }

    @Test
    void testDeleteCustomer_DaoReturnsFalseOrNotFound() {
        when(customerDaoMock.deleteCustomer("C001")).thenReturn(false);
        customerService.deleteCustomer("C001");
        assertTrue(outContent.toString().contains("❌ Failed to delete customer or customer not found: C001"));
        verify(customerDaoMock, times(1)).deleteCustomer("C001");
    }
    
    @Test
    void testDeleteCustomer_NullId() {
        customerService.deleteCustomer(null);
        assertTrue(outContent.toString().contains("❌ Customer ID for deletion cannot be null or empty."));
        verify(customerDaoMock, never()).deleteCustomer(any());
    }

    @Test
    void testDeleteCustomer_EmptyId() {
        customerService.deleteCustomer("  ");
        assertTrue(outContent.toString().contains("❌ Customer ID for deletion cannot be null or empty."));
        verify(customerDaoMock, never()).deleteCustomer(any());
    }
}
