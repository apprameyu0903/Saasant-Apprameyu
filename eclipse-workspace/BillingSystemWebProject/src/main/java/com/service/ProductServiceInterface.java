package com.service;
import com.vo.ProductInfo;
import java.util.*;

public interface ProductServiceInterface {
	
	
    void updateProductDetails(String productId);
    List<ProductInfo> getAllProducts();
    ProductInfo getProductById(String productId);
    void printProductCatalog();
    void addProducts();

	
	

}
