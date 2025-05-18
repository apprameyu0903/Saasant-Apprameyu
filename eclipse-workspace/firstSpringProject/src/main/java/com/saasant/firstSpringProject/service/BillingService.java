package com.saasant.firstSpringProject.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import java.util.*;

import com.saasant.firstSpringProject.dao.ProductDao;
import com.saasant.firstSpringProject.vo.ProductInfo;

@Service
public class BillingService implements BillingServiceInterface {
	
	private ProductDao prods = new ProductDao();
	private Map<String,ProductInfo> billedProducts = new HashMap<>();
	

	@Override
	public Map<String, ProductInfo> billingService() {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		do {
            System.out.print("Enter Product ID: ");
            String productId = sc.nextLine();
            ProductInfo product = prods.getProductById(productId);
            if (product == null) {
                System.out.println("⚠️ Product not found!");
                continue;
            }
            System.out.print("Enter Quantity: ");
            int quantity = sc.nextInt();
            sc.nextLine();
            ProductInfo existingProduct = billedProducts.get(productId);
            if(existingProduct != null) {
            	existingProduct.setQuantity(quantity + existingProduct.getQuantity());
            }else {
	            ProductInfo billedProduct = new ProductInfo(
	                    product.getProductId(),
	                    product.getProductName(),
	                    product.getProductDescription(),
	                    product.getUnit(),
	                    product.getUnitPrice(),
	                    product.getCategory(),
	                    product.getTax()
	            );
	            billedProduct.setQuantity(quantity);
	            billedProducts.put(productId,billedProduct);
            }
            System.out.println("Product added successfully!");
            
            System.out.println("Do you want to add another product? (Y/N): ");
            String addMore = sc.nextLine();
            if (!addMore.equalsIgnoreCase("Y")) {
                break;
            }

        } while (true);
		return billedProducts;
	}

	@Override
	public float subTotal() {
		// TODO Auto-generated method stub
        float subTotal = 0.0f;
        for (ProductInfo p : billedProducts.values()) {
            subTotal += p.getTotalAmount();
        }
		return subTotal;
	}

	@Override
	public float gstCalc() {
		// TODO Auto-generated method stub
        float gst = subTotal() * 0.18f;
		return gst;
	}

	@Override
	public float calcTotal() {
		// TODO Auto-generated method stub
		float grandTotal = subTotal() + gstCalc();
		return grandTotal;
	}
}
