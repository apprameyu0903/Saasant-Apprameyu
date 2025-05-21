package com.example.billingSystespringboot.dao;

import com.example.billingSystespringboot.exception.DataNotFoundException;
import com.example.billingSystespringboot.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ProductDAO {

    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addProduct(Product product) {
        logger.debug("Inserting product into DB: {}", product.getName());
        String sql = "INSERT INTO products (name, price, tax_percent) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getTaxPercent());
    }

    public List<Product> getAllProducts() {
        logger.debug("Fetching all products from DB");
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, new RowMapper<Product>() {
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Product(
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getDouble("tax_percent")
                );
            }
        });
    }

    public void updateProduct(int productId, Product product) {
        logger.debug("Updating product with ID: {}", productId);
        String sql = "UPDATE products SET name = ?, price = ?, tax_percent = ? WHERE product_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getTaxPercent(), productId);
        if (rowsAffected == 0) {
            throw new DataNotFoundException("Product with ID " + productId + " not found for update");
        }
    }

    public void deleteProduct(int productId) {
        logger.debug("Deleting product with ID: {}", productId);
        String sql = "DELETE FROM products WHERE product_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, productId);
        if (rowsAffected == 0) {
            throw new DataNotFoundException("Product with ID " + productId + " not found for deletion");
        }
    }
}
