package com.example.billingSystespringboot.service;

import com.example.billingSystespringboot.dao.ProductDAO;
import com.example.billingSystespringboot.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductDAO productDAO;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() throws SQLException {
        Product product = new Product(0, "Test Product", 100.0, 10.0);
        doNothing().when(productDAO).addProduct(product);

        assertDoesNotThrow(() -> productService.addProduct(product));
        verify(productDAO, times(1)).addProduct(product);
    }

    @Test
    void testGetAllProducts() throws SQLException {
        List<Product> mockProducts = Arrays.asList(
                new Product(1, "Prod1", 100.0, 5.0),
                new Product(2, "Prod2", 200.0, 10.0)
        );
        when(productDAO.getAllProducts()).thenReturn(mockProducts);

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productDAO, times(1)).getAllProducts();
    }

    @Test
    void testUpdateProduct() throws SQLException {
        Product product = new Product(1, "Updated Prod", 150.0, 8.0);
        doNothing().when(productDAO).updateProduct(1, product);

        assertDoesNotThrow(() -> productService.updateProduct(1, product));
        verify(productDAO, times(1)).updateProduct(1, product);
    }

    @Test
    void testDeleteProduct() throws SQLException {
        doNothing().when(productDAO).deleteProduct(1);

        assertDoesNotThrow(() -> productService.deleteProduct(1));
        verify(productDAO, times(1)).deleteProduct(1);
    }
}
