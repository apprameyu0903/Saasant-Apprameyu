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
            when(connectionMock.prepareStatement(startsWith("SELECT * FROM customers WHERE customer_id = ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock); // Stub for SELECT
            when(resultSetMock.next()).thenReturn(false);
            when(connectionMock.prepareStatement(startsWith("insert into customers"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(1); // Stub for INSERT

            boolean result = customerDao.addCustomer(customer1);

            assertTrue(result);
        }
    }
    
    @Test
    void testAddCustomer_CustomerAlreadyExists() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(true); // Simulate customer found by internal getCustomerById

            when(resultSetMock.getString("customer_name")).thenReturn(customer1.getCustomerName());
            when(resultSetMock.getString("customer_mobile")).thenReturn(customer1.getMobileNumber());
            when(resultSetMock.getString("customer_location")).thenReturn(customer1.getCustomerLocation());

            boolean result = customerDao.addCustomer(customer1);

            assertFalse(result);
            verify(preparedStatementMock, never()).executeUpdate();
        }
    }
    @Test
    void testGetCustomerById_Found() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true); 
            when(resultSetMock.getString("customer_name")).thenReturn(customer1.getCustomerName());
            when(resultSetMock.getString("customer_mobile")).thenReturn(customer1.getMobileNumber());
            when(resultSetMock.getString("customer_location")).thenReturn(customer1.getCustomerLocation());

            CustomerDetails found = customerDao.getCustomerById(customer1.getCustomerId());

            assertNotNull(found);
            assertEquals(customer1.getCustomerId(), found.getCustomerId()); 
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
            when(resultSetMock.next()).thenReturn(false);
            CustomerDetails found = customerDao.getCustomerById("NON_EXISTENT_ID");
            assertNull(found); 
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

            when(resultSetMock.next()).thenReturn(true).thenReturn(false);
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
            verify(preparedStatementMock, times(4)).setString(anyInt(), eq("%" + searchTerm + "%"));
            verify(preparedStatementMock, times(1)).executeQuery();
        }
    }

    @Test
    void testAddCustomer_ThrowsSqlException_ReturnsFalse() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenThrow(new SQLException("DB Connection Error"));
            boolean result = customerDao.addCustomer(customer1);
            assertFalse(result);
            
        }
    }
}