package com.saasant.firstSpringProject.dao;

import com.saasant.firstSpringProject.util.DBUtil;
import com.saasant.firstSpringProject.vo.CustomerDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerDaoTest {

    @Mock
    private DBUtil dbUtilMock;
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;

    @InjectMocks
    private CustomerDao customerDao;

    private CustomerDetails customer1;

    @BeforeEach
    void setUp() throws SQLException {
        customer1 = new CustomerDetails("CUST101", "Test User", "1234567890", "Test City");

        
    }

    @Test
    void testAddCustomer_Success() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);

            // Mocking behavior for getCustomerById (to simulate customer not existing initially)
            when(connectionMock.prepareStatement(startsWith("SELECT * FROM customers WHERE customer_id = ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false); // Customer does not exist

            // Mocking behavior for the insert statement
            when(connectionMock.prepareStatement(startsWith("insert into customers"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(1); // 1 row affected

            // Act
            boolean result = customerDao.addCustomer(customer1);

            // Assert
            assertTrue(result);
            verify(preparedStatementMock, times(1)).setString(1, customer1.getCustomerId());
            verify(preparedStatementMock, times(1)).setString(2, customer1.getCustomerName());
            verify(preparedStatementMock, times(1)).setString(3, customer1.getMobileNumber());
            verify(preparedStatementMock, times(1)).setString(4, customer1.getCustomerLocation());
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testAddCustomer_CustomerAlreadyExists() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock); // General prepareStatement mock

            // Mock getCustomerById to return an existing customer
            // This sequence is simplified; CustomerDao's addCustomer calls its own getCustomerById
            // So we need to ensure the ResultSet for that internal call indicates existence.
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock); // For the getCustomerById call within addCustomer
            when(resultSetMock.next()).thenReturn(true); // Simulate customer found
            // Populate fields for the found customer
            when(resultSetMock.getString("customer_id")).thenReturn(customer1.getCustomerId());
            when(resultSetMock.getString("customer_name")).thenReturn(customer1.getCustomerName());
            // ... other fields

            // Act
            boolean result = customerDao.addCustomer(customer1);

            // Assert
            assertFalse(result);
            verify(preparedStatementMock, never()).executeUpdate(); // Insert should not happen
        }
    }

    @Test
    void testGetCustomerById_Found() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true); // Simulate customer found
            when(resultSetMock.getString("customer_id")).thenReturn(customer1.getCustomerId());
            when(resultSetMock.getString("customer_name")).thenReturn(customer1.getCustomerName());
            when(resultSetMock.getString("customer_mobile")).thenReturn(customer1.getMobileNumber());
            when(resultSetMock.getString("customer_location")).thenReturn(customer1.getCustomerLocation());

            // Act
            CustomerDetails found = customerDao.getCustomerById(customer1.getCustomerId());

            // Assert
            assertNotNull(found);
            assertEquals(customer1.getCustomerName(), found.getCustomerName());
            verify(preparedStatementMock, times(1)).setString(1, customer1.getCustomerId());
        }
    }

    @Test
    void testGetCustomerById_NotFound() throws SQLException {
         try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false); // Simulate customer not found

            // Act
            CustomerDetails found = customerDao.getCustomerById("NON_EXISTENT_ID");

            // Assert
            assertNull(found); // Expect null or an empty CustomerDetails depending on constructor
                               // The DAO returns null if not found.
         }
    }
    
    @Test
    void testGetAllCustomers_Success_ReturnsListOfCustomers() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            // For getAllCustomers, it uses createStatement, not prepareStatement
            Statement statementMock = mock(Statement.class);
            when(connectionMock.createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);

            // Simulate two customers in the ResultSet
            when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Two rows, then end

            // First customer
            when(resultSetMock.getString("customer_id")).thenReturn("C001").thenReturn("C002");
            when(resultSetMock.getString("customer_name")).thenReturn("Alice").thenReturn("Bob");
            when(resultSetMock.getString("customer_mobile")).thenReturn("111").thenReturn("222");
            when(resultSetMock.getString("customer_location")).thenReturn("Loc1").thenReturn("Loc2");

            // Act
            List<CustomerDetails> customers = customerDao.getAllCustomers();

            // Assert
            assertNotNull(customers);
            assertEquals(2, customers.size());
            assertEquals("Alice", customers.get(0).getCustomerName());
            assertEquals("Bob", customers.get(1).getCustomerName());
            verify(statementMock, times(1)).executeQuery("SELECT * FROM customers");
        }
    }
    
    @Test
    void testUpdateCustomer_Success() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("UPDATE customers SET"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(1); // 1 row updated

            CustomerDetails customerToUpdate = new CustomerDetails("CUST101", "Updated Name", "0000000000", "Updated City");

            // Act
            boolean result = customerDao.updateCustomer(customerToUpdate);

            // Assert
            assertTrue(result);
            verify(preparedStatementMock, times(1)).setString(1, "Updated Name");
            verify(preparedStatementMock, times(1)).setString(2, "0000000000");
            verify(preparedStatementMock, times(1)).setString(3, "Updated City");
            verify(preparedStatementMock, times(1)).setString(4, "CUST101");
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }

    @Test
    void testDeleteCustomer_Success() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("DELETE FROM customers WHERE customer_id = ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(1); // 1 row deleted

            // Act
            boolean result = customerDao.deleteCustomer("CUST101");

            // Assert
            assertTrue(result);
            verify(preparedStatementMock, times(1)).setString(1, "CUST101");
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
    
    @Test
    void testSearchCustomers_ReturnsMatchingCustomers() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("SELECT * FROM customers WHERE customer_id LIKE ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true).thenReturn(false); // One matching customer
            when(resultSetMock.getString("customer_id")).thenReturn(customer1.getCustomerId());
            when(resultSetMock.getString("customer_name")).thenReturn(customer1.getCustomerName());
            when(resultSetMock.getString("customer_mobile")).thenReturn(customer1.getMobileNumber());
            when(resultSetMock.getString("customer_location")).thenReturn(customer1.getCustomerLocation());
            
            String searchTerm = "Test";

            // Act
            List<CustomerDetails> customers = customerDao.searchCustomers(searchTerm);

            // Assert
            assertNotNull(customers);
            assertEquals(1, customers.size());
            assertEquals(customer1.getCustomerName(), customers.get(0).getCustomerName());
            verify(preparedStatementMock, times(4)).setString(anyInt(), eq("%" + searchTerm + "%")); // Called for 4 placeholders
            verify(preparedStatementMock, times(1)).executeQuery();
        }
    }

    // Add tests for SQLException handling (e.g., when getConnection throws an exception)
    @Test
    void testAddCustomer_ThrowsSqlException_ReturnsFalse() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenThrow(new SQLException("DB Connection Error"));

            // Act
            boolean result = customerDao.addCustomer(customer1);

            // Assert
            assertFalse(result);
            // You might also want to verify that e.printStackTrace() was called if you have a way to capture System.err
        }
    }
}