package com.saasant.firstSpringProject.dao;

import com.saasant.firstSpringProject.util.DBUtil;
import com.saasant.firstSpringProject.vo.ProductInfo;
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
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductDaoTest {

    @Mock
    private DBUtil dbUtilMock;
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement preparedStatementMock;
    @Mock
    private ResultSet resultSetMock;
    @Mock
    private ResultSetMetaData resultSetMetaDataMock;


    @InjectMocks
    private ProductDao productDao;

    private ProductInfo product1;

    @BeforeEach
    void setUp() throws SQLException {
        product1 = new ProductInfo("P001", "Laptop", "Gaming Laptop", "unit", 1200.00f, "Electronics", 10f, 1);
    }

    @Test
    void addProduct_Success() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("insert into products"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(1);
            productDao.addProduct(product1);

            verify(preparedStatementMock, times(1)).setString(1, product1.getProductId());
            verify(preparedStatementMock, times(1)).setString(2, product1.getProductName());
            verify(preparedStatementMock, times(1)).setInt(8, product1.getQuantity());
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
    
    @Test
    void addProduct_ThrowsSqlException() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("insert into products"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException("DB Error"));
            productDao.addProduct(product1);

            verify(preparedStatementMock, times(1)).executeUpdate();

        }
    }


    @Test
    void getProductById_Found() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("SELECT * FROM products WHERE product_id = ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.getMetaData()).thenReturn(resultSetMetaDataMock); 
            when(resultSetMetaDataMock.getColumnCount()).thenReturn(1); 
            when(resultSetMetaDataMock.getColumnName(1)).thenReturn("quantity"); 
            when(resultSetMock.next()).thenReturn(true); 
            when(resultSetMock.getString("product_id")).thenReturn(product1.getProductId());
            when(resultSetMock.getString("product_name")).thenReturn(product1.getProductName());
            when(resultSetMock.getString("product_desc")).thenReturn(product1.getProductDescription());
            when(resultSetMock.getString("unit")).thenReturn(product1.getUnit());
            when(resultSetMock.getFloat("unit_price")).thenReturn(product1.getUnitPrice());
            when(resultSetMock.getString("category")).thenReturn(product1.getCategory());
            when(resultSetMock.getFloat("taxPercentage")).thenReturn(product1.getTax());
            when(resultSetMock.getInt("quantity")).thenReturn(product1.getQuantity());


            ProductInfo foundProduct = productDao.getProductById("P001");

            assertNotNull(foundProduct);
            assertEquals("Laptop", foundProduct.getProductName());
            verify(preparedStatementMock, times(1)).setString(1, "P001");
        }
    }
    
     @Test
    void getProductById_NotFound() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("SELECT * FROM products WHERE product_id = ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false); 
            ProductInfo foundProduct = productDao.getProductById("P999");

            assertNull(foundProduct);
        }
    }
    
    @Test
    void getProductById_SqlException() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("SELECT * FROM products WHERE product_id = ?"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeQuery()).thenThrow(new SQLException("DB Error"));

            ProductInfo foundProduct = productDao.getProductById("P001");

            assertNull(foundProduct); 
             verify(preparedStatementMock, times(1)).executeQuery();
        }
    }

    @Test
    void getAllProducts_Success() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            Statement statementMock = mock(Statement.class); // getAllProducts uses Statement
            when(connectionMock.createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery(startsWith("SELECT * FROM products"))).thenReturn(resultSetMock);
            when(resultSetMock.getMetaData()).thenReturn(resultSetMetaDataMock); // For hasColumn check
            when(resultSetMetaDataMock.getColumnCount()).thenReturn(1);
            when(resultSetMetaDataMock.getColumnName(1)).thenReturn("quantity");


            when(resultSetMock.next()).thenReturn(true).thenReturn(false); // Simulate one product
            when(resultSetMock.getString("product_id")).thenReturn(product1.getProductId());
            when(resultSetMock.getString("product_name")).thenReturn(product1.getProductName());
            when(resultSetMock.getString("product_desc")).thenReturn(product1.getProductDescription());
            when(resultSetMock.getString("unit")).thenReturn(product1.getUnit());
            when(resultSetMock.getFloat("unit_price")).thenReturn(product1.getUnitPrice());
            when(resultSetMock.getString("category")).thenReturn(product1.getCategory());
            when(resultSetMock.getFloat("taxPercentage")).thenReturn(product1.getTax());
            when(resultSetMock.getInt("quantity")).thenReturn(product1.getQuantity());


            List<ProductInfo> products = productDao.getAllProducts();

            assertNotNull(products);
            assertFalse(products.isEmpty());
            assertEquals(1, products.size());
            assertEquals("Laptop", products.get(0).getProductName());
        }
    }
    
    @Test
    void getAllProducts_SqlException() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            Statement statementMock = mock(Statement.class);
            when(connectionMock.createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery(startsWith("SELECT * FROM products"))).thenThrow(new SQLException("DB Error"));

            List<ProductInfo> products = productDao.getAllProducts();

            assertNotNull(products);
            assertTrue(products.isEmpty()); // Expect empty list on SQL error
            verify(statementMock, times(1)).executeQuery(anyString());
        }
    }


    @Test
    void updateProduct_Success() throws SQLException {
         try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("UPDATE products SET"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(1); // Simulate 1 row updated

            boolean result = productDao.updateProduct(product1);

            assertTrue(result);
            verify(preparedStatementMock, times(1)).setString(1, product1.getProductName());
            // ... verify other setString/Float/Int calls
            verify(preparedStatementMock, times(1)).setString(8, product1.getProductId());
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
    
    @Test
    void updateProduct_ReturnsFalseIfNoRowsUpdated() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("UPDATE products SET"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenReturn(0); // Simulate 0 rows updated

            boolean result = productDao.updateProduct(product1);

            assertFalse(result);
             verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
    
    @Test
    void updateProduct_SqlException() throws SQLException {
        try (MockedStatic<DBUtil> mockedDBUtil = Mockito.mockStatic(DBUtil.class)) {
            mockedDBUtil.when(DBUtil::getInstance).thenReturn(dbUtilMock);
            when(dbUtilMock.getConnection()).thenReturn(connectionMock);
            when(connectionMock.prepareStatement(startsWith("UPDATE products SET"))).thenReturn(preparedStatementMock);
            when(preparedStatementMock.executeUpdate()).thenThrow(new SQLException("DB Error"));
            boolean result = productDao.updateProduct(product1);
            assertFalse(result); 
            verify(preparedStatementMock, times(1)).executeUpdate();
        }
    }
}