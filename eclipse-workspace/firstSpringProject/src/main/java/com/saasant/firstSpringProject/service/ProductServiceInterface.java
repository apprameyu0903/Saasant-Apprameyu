package com.saasant.firstSpringProject.service;
import com.saasant.firstSpringProject.vo.ProductInfo;
import java.util.*;

public interface ProductServiceInterface {
	
	
    void updateProductDetails(String productId);
    List<ProductInfo> getAllProducts();
    ProductInfo getProductById(String productId);
    void printProductCatalog();
    void addProducts();

	
	

}
