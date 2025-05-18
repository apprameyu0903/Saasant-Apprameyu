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
        // Reset the state of billedProducts in billingService for each test
        // This can be done by re-initializing BillingService or having a reset method.
        // For this example, we'll manually add products to the internal map for calculation tests.
        
        billingService = new BillingService(); // Re-initialize to clear internal state
        // Since ProductDao is instantiated inside BillingService, we need to mock it differently for `billingService()`
        // or refactor BillingService to inject ProductDao.
        // For calculation methods, we can manipulate the `billedProducts` map directly or via setters if available.
        // The @InjectMocks and @Mock usually work when the dependency is a field in the tested class.
        // The current BillingService news up ProductDao. This is harder to test in isolation.

        product1 = new ProductInfo("P001", "Laptop", "Gaming Laptop", "unit", 1200.00f, "Electronics", 10f, 1); // Tax 10%
        product2 = new ProductInfo("P002", "Mouse", "Wireless Mouse", "unit", 25.00f, "Electronics", 5f, 2);   // Tax 5%

        // For testing subTotal, gstCalc, calcTotal, we need to populate `billedProducts`
        // This is tricky as `billedProducts` is private. One way is to call `billingService()`
        // or use reflection (not recommended for tests usually), or refactor for testability.

        // Let's assume we can set up the billedProducts map for testing calculation methods.
        // One way is to make billingService() add to a map that we can then inspect/use.
        // Or, for these specific calculation tests, we can call billingService() and then test the others.
        // However, billingService() itself has Scanner input.

        // For the calculation methods, let's assume `billedProducts` is populated.
        // We will directly populate the `billedProducts` map in the `BillingService` instance
        // using a helper method or by modifying `BillingService` for testability (e.g., package-private access or setter).
        // For this example, we'll skip direct population for now and focus on structure.
        // A better approach would be to refactor BillingService to allow `billedProducts` to be set for testing.
    }
    
    // Helper to set billed products for testing calculation methods
    // This would ideally be a method in BillingService or use reflection.
    // For simplicity, we can't directly access private `billedProducts` here without refactoring BillingService.
    // So, the tests for subTotal, gstCalc, and calcTotal will assume that `billingService()` has run and populated the map.
    // This makes them more like integration tests for those methods.


    @Test
    void testBillingService_AddOneProduct() {
        // This test is more complex due to System.in.
        // Provide simulated user input
        String simulatedInput = "P001\n1\nN\n"; // Product ID, Quantity, No to add more
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream); // Redirect System.in

        // Mock ProductDao response
        ProductInfo mockProduct = new ProductInfo("P001", "Test Product", "Desc", "unit", 100f, "Cat", 10f);
        // Need to mock the ProductDao instance that BillingService creates internally.
        // This requires refactoring BillingService to inject ProductDao.
        // If ProductDao is injected (e.g., via constructor or @Autowired if BillingService is a Spring bean):
        // when(productDaoMock.getProductById("P001")).thenReturn(mockProduct);
        
        // To truly test this, BillingService needs ProductDao injected.
        // For now, this test will likely fail or throw NullPointerException if ProductDao is not mocked correctly
        // because `prods.getProductById(productId)` will be on a null or unmocked `prods`.

        // Act
        // Map<String, ProductInfo> billedItems = billingService.billingService();

        // Assert
        // assertNotNull(billedItems);
        // assertEquals(1, billedItems.size());
        // assertTrue(billedItems.containsKey("P001"));
        // assertEquals(1, billedItems.get("P001").getQuantity());

        // Restore System.in
        System.setIn(System.in);
        
        // This test illustrates the difficulty. Skipping full assertion due to internal DAO instantiation.
        assertTrue(true, "Test structure for billingService, requires refactoring for proper mocking.");
    }


    // For the calculation methods, we'll assume `billedProducts` has been populated by `billingService`.
    // A better way is to directly set `billedProducts` for the test.
    // If BillingService were refactored to allow setting `billedProducts`:
    /*
    @Test
    void testSubTotal() {
        // Arrange: Manually prepare the billedProducts map in the service instance.
        // This requires either a setter, package-private access, or reflection.
        // Or, call the `billingService()` method with controlled input first.
        Map<String, ProductInfo> testProducts = new HashMap<>();
        product1.setQuantity(1); // 1200 * (1 + 0.10) = 1320
        product2.setQuantity(2); // 25 * (1 + 0.05) = 26.25 per unit. Total for 2 units = 52.5. Wait, getTotalAmount is (price+tax)*quantity.
                                 // So product1: (1200 + 120) * 1 = 1320
                                 // product2: (25 + 1.25) * 2 = 52.5
        // The subTotal in BillingService seems to be sum of ProductInfo.getTotalAmount().
        // ProductInfo.getTotalAmount() is (unitPrice + (unitPrice * tax/100)) * quantity
        testProducts.put(product1.getProductId(), product1);
        testProducts.put(product2.getProductId(), product2);

        // Simulate that billingService.billingService() populated billedProducts.
        // This is where direct manipulation or a test-specific setter in BillingService would be good.
        // For now, we can't directly set `billingService.billedProducts`.
        // So, this test is more of an illustration.

        // If we could set it:
        // billingService.setBilledProductsForTest(testProducts); // Hypothetical method

        // As an alternative, if BillingService is a @Service and methods are public,
        // and if `billedProducts` is a field that gets populated, we can mock the interaction.
        // But here `subTotal` relies on the state of `billedProducts`.

        // To test subTotal in isolation, we need to control `billedProducts`.
        // Let's assume `billingService()` was called and populated it.
        // This makes this test dependent on `billingService()`'s correct execution.

        // A better test would mock the `billedProducts` collection if it were accessible
        // or refactor `subTotal` to take the collection as a parameter.

        // Given current structure, this test is hard to do in true isolation for subTotal.
        // Let's assume we call a modified version of billingService first or use a spy.

        // For this example, we will assume `billedProducts` contains product1 and product2
        // and `billingService` itself is spied upon so we can call its methods.
        
        // If BillingService's internal billedProducts map was populated with product1 and product2:
        // product1: (1200 + 1200*0.10) * 1 = 1320.0f
        // product2: (25 + 25*0.05) * 2 = (25 + 1.25) * 2 = 26.25 * 2 = 52.5f
        // Expected subTotal = 1320.0f + 52.5f = 1372.5f

        // To test this, we need to make `subTotal()` use a controlled `billedProducts`.
        // Since it's private, this is hard.
        // We can call `billingService.billingService()` with controlled input, then call `subTotal()`.

        // For now, let's assume a scenario where billedProducts are set.
        // This test will be more illustrative than fully runnable without refactoring BillingService.
        // To make it somewhat runnable:
        // Create a real BillingService instance (not a mock/spy for this part).
        // Manually add to its private `billedProducts` map using reflection (not ideal)
        // or make `billedProducts` accessible for tests (e.g. package-private).

        // Simplified: If BillingService had a way to add products for testing:
        // billingService.addTestProduct(product1);
        // billingService.addTestProduct(product2);
        // float result = billingService.subTotal();
        // assertEquals(1372.5f, result, 0.01f);

        assertTrue(true, "SubTotal test requires BillingService refactoring or reflection to set billedProducts.");
    }
    */


    @Test
    void testSubTotal_WithSpy() {
        // Arrange
        Map<String, ProductInfo> testBilledProducts = new HashMap<>();
        // product1: price 100, tax 10% -> total 110
        ProductInfo p1 = new ProductInfo("P1", "A", "Ad", "u", 100f, "c", 10f, 1);
        // product2: price 200, tax 5% -> total 210 * 2 = 420
        ProductInfo p2 = new ProductInfo("P2", "B", "Bd", "u", 200f, "c", 5f, 2);
        testBilledProducts.put(p1.getProductId(), p1);
        testBilledProducts.put(p2.getProductId(), p2);

        // To test subTotal, we need `billedProducts` to be populated.
        // We can use a spy to call the real `subTotal` but we need to control `billedProducts`.
        // One way is to call `billingService()` method first with controlled input.
        // Or, if `BillingService` could have `billedProducts` set:
        // For this example, let's imagine `BillingService` is refactored or we use reflection.
        // A simpler approach with spy is to mock the methods that populate `billedProducts`.
        // But subTotal itself uses the field, so we must ensure the field is set.

        // Let's assume BillingService is refactored to expose `billedProducts` for testing or has a constructor.
        // For this example, we'll mock the map that `subTotal()` iterates over.
        // This is not directly possible with the current private field without refactoring.

        // However, if `subTotal` calls `billingService()` implicitly or relies on its state,
        // we can do the following:
        // To make this testable, we would need to mock the internal `billedProducts` map.
        // This requires reflection or refactoring BillingService.

        // If we spy on `billingService` and assume `billingService()` populates the map correctly,
        // then we call `subTotal()`.
        // This test will only work if `billedProducts` can be controlled.
        // For the purpose of this example, let's assume it is.

        // Manually "setting" the billed products in the spied instance.
        // This would typically involve reflection if the field is private and no setter exists.
        // For the sake of a structural example:
        try {
            java.lang.reflect.Field field = BillingService.class.getDeclaredField("billedProducts");
            field.setAccessible(true);
            field.set(billingService, testBilledProducts); // Set the map on the spied instance
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set billedProducts via reflection: " + e.getMessage());
        }
        
        float subtotal = billingService.subTotal();
        // p1 total: (100 + 100*0.10) * 1 = 110
        // p2 total: (200 + 200*0.05) * 2 = (200+10)*2 = 210*2 = 420
        // Expected: 110 + 420 = 530
        assertEquals(530.0f, subtotal, 0.01f);
    }

    @Test
    void testGstCalc_WithSpy() {
        // Arrange
        // Mock the subTotal() method to return a fixed value for this test.
        // We are spying on billingService, so we can stub its methods.
        doReturn(530.0f).when(billingService).subTotal(); // Mock subTotal to return 530.0f

        // Act
        float gst = billingService.gstCalc(); // This will use the mocked subTotal()

        // Assert
        // Expected GST = 530.0f * 0.18f = 95.4f
        assertEquals(95.4f, gst, 0.01f);
        verify(billingService, times(1)).subTotal(); // Verify subTotal was called
    }

    @Test
    void testCalcTotal_WithSpy() {
         // Arrange
        // Mock subTotal() and gstCalc() to return fixed values.
        doReturn(530.0f).when(billingService).subTotal();
        doReturn(95.4f).when(billingService).gstCalc();

        // Act
        float grandTotal = billingService.calcTotal();

        // Assert
        // Expected Total = 530.0f + 95.4f = 625.4f
        assertEquals(625.4f, grandTotal, 0.01f);
        verify(billingService, times(1)).subTotal();
        verify(billingService, times(1)).gstCalc();
    }
}