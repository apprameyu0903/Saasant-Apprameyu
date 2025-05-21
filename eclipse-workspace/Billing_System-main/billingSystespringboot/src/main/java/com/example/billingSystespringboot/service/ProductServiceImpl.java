package com.example.billingSystespringboot.service;

import com.example.billingSystespringboot.dao.ProductDAO;
import com.example.billingSystespringboot.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class ProductServiceImpl implements ProductService {


    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDAO productDAO;

    @Override
    public void addProduct(Product product) throws SQLException {
    	 logger.info("Service: Add product {}", product.getName());
        productDAO.addProduct(product);
    }

    @Override
    public List<Product> getAllProducts() throws SQLException {
        logger.info("Service: Get all products");
        return productDAO.getAllProducts();
    }

    @Override
    public void updateProduct(int productId, Product product) throws SQLException {
    	 logger.info("Service: Update product ID {}", productId);
    	productDAO.updateProduct(productId, product);
    }

    @Override
    public void deleteProduct(int productId) throws SQLException {
    	logger.info("Service: Delete product ID {}", productId);
        productDAO.deleteProduct(productId);
    }
}
