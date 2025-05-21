package com.example.billingSystespringboot.controller;

import com.example.billingSystespringboot.model.Product;
import com.example.billingSystespringboot.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Product product1;
    private Product product2;

    @BeforeEach
    void setup() {
        product1 = new Product(1, "Prod1", 100.0, 5.0);
        product2 = new Product(2, "Prod2", 200.0, 10.0);
    }

    @Test
    void testAddProduct_Success() throws Exception {
        doNothing().when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added successfully"));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void testAddProduct_Error() throws Exception {
        doThrow(new RuntimeException("DB error")).when(productService).addProduct(any(Product.class));

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error")));

        verify(productService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Prod1"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        doNothing().when(productService).updateProduct(eq(1), any(Product.class));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));

        verify(productService, times(1)).updateProduct(eq(1), any(Product.class));
    }

    @Test
    void testUpdateProduct_Error() throws Exception {
        doThrow(new RuntimeException("DB error")).when(productService).updateProduct(eq(1), any(Product.class));

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error")));

        verify(productService, times(1)).updateProduct(eq(1), any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        doNothing().when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));

        verify(productService, times(1)).deleteProduct(1);
    }

    @Test
    void testDeleteProduct_Error() throws Exception {
        doThrow(new RuntimeException("DB error")).when(productService).deleteProduct(1);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error")));

        verify(productService, times(1)).deleteProduct(1);
    }
}
