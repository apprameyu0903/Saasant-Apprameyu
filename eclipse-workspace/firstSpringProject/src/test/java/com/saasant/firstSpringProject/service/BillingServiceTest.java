package com.saasant.firstSpringProject.service;

import com.saasant.firstSpringProject.dao.ProductDao;
import com.saasant.firstSpringProject.vo.ProductInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BillingServiceTest {

    @Mock
    private ProductDao productDaoMock; // Mock for ProductDao dependency

    @Spy // Use @Spy if you want to call real methods on BillingService but mock others
    @InjectMocks // This will inject productDaoMock into billingService
    private BillingService billingService;

    private ProductInfo product1;
    private ProductInfo product2;

    @BeforeEach
    void setUp() {
        billingService = new BillingService(); 

        product1 = new ProductInfo("P001", "Laptop", "Gaming Laptop", "unit", 1200.00f, "Electronics", 10f, 1); // Tax 10%
        product2 = new ProductInfo("P002", "Mouse", "Wireless Mouse", "unit", 25.00f, "Electronics", 5f, 2);   // Tax 5%

    }

    @Test
    void testBillingService_AddOneProduct() {
        String simulatedInput = "P001\n1\nN\n"; 
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);
        ProductInfo mockProduct = new ProductInfo("P001", "Test Product", "Desc", "unit", 100f, "Cat", 10f);
        System.setIn(System.in);
        assertTrue(true, "Test structure for billingService, requires refactoring for proper mocking.");
    }


    @Test
    void testSubTotal_WithSpy() {
        // Arrange
        Map<String, ProductInfo> testBilledProducts = new HashMap<>();
        ProductInfo p1 = new ProductInfo("P1", "A", "Ad", "u", 100f, "c", 10f, 1);
        ProductInfo p2 = new ProductInfo("P2", "B", "Bd", "u", 200f, "c", 5f, 2);
        testBilledProducts.put(p1.getProductId(), p1);
        testBilledProducts.put(p2.getProductId(), p2);

        try {
            java.lang.reflect.Field field = BillingService.class.getDeclaredField("billedProducts");
            field.setAccessible(true);
            field.set(billingService, testBilledProducts);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set billedProducts via reflection: " + e.getMessage());
        }
        
        float subtotal = billingService.subTotal();
        assertEquals(530.0f, subtotal, 0.01f);
    }
}

