package com.saasant.firstSpringProject.dao;
import com.saasant.firstSpringProject.vo.ProductInfo;
import java.util.List;
public interface ProductDaoInterface {
	
	void addProduct(ProductInfo product);
	ProductInfo getProductById(String productId);
	List<ProductInfo> getAllProducts();
	boolean updateProduct(ProductInfo product);
	
	

}
