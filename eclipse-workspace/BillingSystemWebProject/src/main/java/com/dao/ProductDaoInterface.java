package com.dao;
import com.vo.ProductInfo;
import java.util.List;
public interface ProductDaoInterface {
	
	void addProduct(ProductInfo product);
	ProductInfo getProductById(String productId);
	List<ProductInfo> getAllProducts();
	boolean updateProduct(ProductInfo product);
	
	

}
