package com.saasant.firstSpringProject.service;

import com.saasant.firstSpringProject.dao.ProductDao; // Assuming ProductService uses ProductDao directly
import com.saasant.firstSpringProject.vo.ProductInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDaoMock;

    @InjectMocks
    private ProductService productService; 

    private ProductInfo product1;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private InputStream originalIn;


    @BeforeEach
    void setUp() {
        

        product1 = new ProductInfo("P001", "Old Laptop", "Old Gaming Laptop", "unit", 1000f, "Old Electronics", 8f, 1);
        
        System.setOut(new PrintStream(outContent));
        originalIn = System.in; // Store original System.in
    }
    
    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn); // Restore original System.in
    }


    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        List<ProductInfo> expectedProducts = Collections.singletonList(product1);
        when(productDaoMock.getAllProducts()).thenReturn(expectedProducts);

        List<ProductInfo> actualProducts = productService.getAllProducts();

        assertEquals(expectedProducts, actualProducts);
        verify(productDaoMock, times(1)).getAllProducts();
    }

    @Test
    void getProductById_whenProductExists_shouldReturnProduct() {
        when(productDaoMock.getProductById("P001")).thenReturn(product1);

        ProductInfo actualProduct = productService.getProductById("P001");

        assertEquals(product1, actualProduct);
        verify(productDaoMock, times(1)).getProductById("P001");
    }

    @Test
    void getProductById_whenProductNotExists_shouldReturnNull() {
        when(productDaoMock.getProductById("P999")).thenReturn(null);

        ProductInfo actualProduct = productService.getProductById("P999");

        assertNull(actualProduct);
        verify(productDaoMock, times(1)).getProductById("P999");
    }
    
    @Test
    void printProductCatalog_shouldPrintProducts() {
        List<ProductInfo> products = List.of(
            new ProductInfo("P001", "Laptop", "Gaming Laptop", "unit", 1200.00f, "Electronics", 10f, 1),
            new ProductInfo("P002", "Mouse", "Wireless Mouse", "unit", 25.00f, "Electronics", 5f, 2)
        );
        when(productDaoMock.getAllProducts()).thenReturn(products);

        productService.printProductCatalog();
        
        String output = outContent.toString();
        assertTrue(output.contains("Product Catalog:"));
        assertTrue(output.contains("P001"));
        assertTrue(output.contains("Gaming Laptop"));
        assertTrue(output.contains("P002"));
        assertTrue(output.contains("Wireless Mouse"));
        verify(productDaoMock, times(1)).getAllProducts();
    }
    
    @Test
    void addProducts_whenNewProduct_shouldAddSuccessfully() {
        String simulatedInput = "P003\nNewProd\nNewDesc\nitem\n50.0\nCategory\n5.0\n"; // Input for a new product
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        when(productDaoMock.getProductById("P003")).thenReturn(null);
        doNothing().when(productDaoMock).addProduct(any(ProductInfo.class));


        productService.addProducts(); // This will read from the mocked System.in

        String output = outContent.toString();
        assertTrue(output.contains("Product added successfully!"));
        verify(productDaoMock, times(1)).getProductById("P003"); // Checked if product exists
        verify(productDaoMock, times(1)).addProduct(argThat(p -> // Verifying addProduct was called with correct details
            p.getProductId().equals("P003") && p.getProductName().equals("NewProd")
        ));
    }

    @Test
    void addProducts_whenProductAlreadyExists_shouldPromptAgainAndAdd() {
        String simulatedInput = "P001\nP004\nAnotherProd\nAnotherDesc\npiece\n75.0\nAnotherCat\n2.0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        when(productDaoMock.getProductById("P001")).thenReturn(product1); // P001 exists
        when(productDaoMock.getProductById("P004")).thenReturn(null);   // P004 is new
        doNothing().when(productDaoMock).addProduct(any(ProductInfo.class));

        productService.addProducts();

        String output = outContent.toString();
        assertTrue(output.contains("Product already exists"));
        assertTrue(output.contains("Product added successfully!")); // For P004
        verify(productDaoMock, times(1)).getProductById("P001");
        verify(productDaoMock, times(1)).getProductById("P004");
        verify(productDaoMock, times(1)).addProduct(argThat(p -> p.getProductId().equals("P004")));
    }
    
    @Test
    void updateProductDetails_whenProductExists_shouldUpdateSuccessfully() {
        String simulatedInput = "Updated Laptop\nUpdated Desc\nbox\n1100.0\nUpdated Cat\n9.0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        when(productDaoMock.getProductById("P001")).thenReturn(product1);
        when(productDaoMock.updateProduct(any(ProductInfo.class))).thenReturn(true);

        productService.updateProductDetails("P001");

        String output = outContent.toString();
        assertTrue(output.contains("Product updated successfully."));
        verify(productDaoMock, times(1)).getProductById("P001");
        verify(productDaoMock, times(1)).updateProduct(argThat(p ->
            p.getProductId().equals("P001") &&
            p.getProductName().equals("Updated Laptop") &&
            p.getUnitPrice() == 1100.0f
        ));
    }
    
    @Test
    void updateProductDetails_whenProductNotExists_shouldShowNotFoundMessage() {
        // No input needed as it should exit early
        when(productDaoMock.getProductById("P999")).thenReturn(null);

        productService.updateProductDetails("P999");

        String output = outContent.toString();
        assertTrue(output.contains("Product not found."));
        verify(productDaoMock, times(1)).getProductById("P999");
        verify(productDaoMock, never()).updateProduct(any(ProductInfo.class));
    }

    @Test
    void updateProductDetails_whenUpdateFailsInDao_shouldShowFailedMessage() {
        String simulatedInput = "Updated Laptop\nUpdated Desc\nbox\n1100.0\nUpdated Cat\n9.0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        
        when(productDaoMock.getProductById("P001")).thenReturn(product1);
        when(productDaoMock.updateProduct(any(ProductInfo.class))).thenReturn(false); // DAO update fails

        productService.updateProductDetails("P001");

        String output = outContent.toString();
        assertTrue(output.contains("Failed to update product."));
        verify(productDaoMock, times(1)).updateProduct(any(ProductInfo.class));
    }

     @Test
    void updateProductDetails_usesExistingValuesWhenInputIsEmpty() {
        // Simulate user pressing Enter for all fields, thus keeping existing values
        String simulatedInput = "\n\n\n\n\n\n"; // Empty inputs for name, desc, unit, price, category, tax
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ProductInfo existingProduct = new ProductInfo("P001", "Original Name", "Original Desc", "kg", 10.0f, "Category A", 5.0f, 10);
        when(productDaoMock.getProductById("P001")).thenReturn(existingProduct);
        when(productDaoMock.updateProduct(any(ProductInfo.class))).thenReturn(true);

        productService.updateProductDetails("P001");

        verify(productDaoMock, times(1)).updateProduct(argThat(p ->
            p.getProductId().equals("P001") &&
            p.getProductName().equals(existingProduct.getProductName()) &&
            p.getProductDescription().equals(existingProduct.getProductDescription()) &&
            p.getUnit().equals(existingProduct.getUnit()) &&
            p.getUnitPrice() == existingProduct.getUnitPrice() &&
            p.getCategory().equals(existingProduct.getCategory()) &&
            p.getTax() == existingProduct.getTax()
        ));
        assertTrue(outContent.toString().contains("Product updated successfully."));
    }
}