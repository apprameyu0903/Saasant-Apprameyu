package com.saasant.firstSpringProject.service;
import java.util.*;
import com.saasant.firstSpringProject.dao.ProductDao;

import com.saasant.firstSpringProject.vo.ProductInfo;

public class ProductService implements ProductServiceInterface {

	private ProductDao prods;
	Scanner sc = new Scanner(System.in);
	
	
	public ProductService() {
        prods = new ProductDao();  
    }
	
	private List<ProductInfo> productCatalog;
	
	public void updateProductDetails(String productId) {
        ProductInfo product = prods.getProductById(productId);
        if (product == null) {
            System.out.println("❌ Product not found.");
            return;
        }
        ProductInfo updatedProduct = getUpdatedProductInfo(product);
        boolean success = prods.updateProduct(updatedProduct);
        if (success) {
            System.out.println("✅ Product updated successfully.");
        } else {
            System.out.println("❌ Failed to update product.");
        }
    }
	
	private ProductInfo getUpdatedProductInfo(ProductInfo existingProduct) {
        System.out.println("Editing product: " + existingProduct.getProductName());

        System.out.print("Enter New Product Name (" + existingProduct.getProductName() + "): ");
        String name = sc.nextLine();
        if (name.isEmpty()) name = existingProduct.getProductName();

        System.out.print("Enter New Product Description (" + existingProduct.getProductDescription() + "): ");
        String desc = sc.nextLine();
        if (desc.isEmpty()) desc = existingProduct.getProductDescription();

        System.out.print("Enter New Unit (" + existingProduct.getUnit() + "): ");
        String unit = sc.nextLine();
        if (unit.isEmpty()) unit = existingProduct.getUnit();

        System.out.print("Enter New Unit Price (" + existingProduct.getUnitPrice() + "): ");
        String priceInput = sc.nextLine();
        float price = priceInput.isEmpty() ? existingProduct.getUnitPrice() : Float.parseFloat(priceInput);

        System.out.print("Enter New Category (" + existingProduct.getCategory() + "): ");
        String category = sc.nextLine();
        if (category.isEmpty()) category = existingProduct.getCategory();

        System.out.print("Enter New Tax (%) (" + existingProduct.getTax() + "): ");
        String taxInput = sc.nextLine();
        float tax = taxInput.isEmpty() ? existingProduct.getTax() : Float.parseFloat(taxInput);

        return new ProductInfo(existingProduct.getProductId(), name, desc, unit, price, category, tax);
    }
	
	public List<ProductInfo> getAllProducts() {
        return prods.getAllProducts();
    }
	
	public ProductInfo getProductById(String productId) {
        return prods.getProductById(productId);
    }

	public void printProductCatalog() {
		// TODO Auto-generated method stub
		productCatalog = prods.getAllProducts();
		System.out.println("\nProduct Catalog:");
        System.out.println("ID        Description               Price     Tax(%)");
        System.out.println("-------------------------------------------------------");
        for (ProductInfo p : productCatalog) {
            String line = p.getProductId() + "    " + String.format("%-25s", p.getProductDescription()) + "₹" + String.format("%.2f", p.getUnitPrice()) + "   " + String.format("%.2f", p.getTax()) + "%";
            System.out.println(line);
        }
		
	}

	@Override
	public void addProducts() {
		// TODO Auto-generated method stub
		System.out.println("\nEnter New Product Details:");
        System.out.print("Enter Product ID: ");
        String newProductId = sc.nextLine();
        ProductInfo existingProduct = prods.getProductById(newProductId);
        if(existingProduct != null) {
        	System.out.println("Product already exists");
        	System.out.print("Enter Product ID: ");
            newProductId = sc.nextLine();
        }
        System.out.print("Enter Product Name: ");
        String newProductName = sc.nextLine();
        System.out.print("Enter Product Description: ");
        String newProductDescription = sc.nextLine();
        System.out.print("Enter Product Unit: ");
        String productUnit = sc.nextLine();
        System.out.print("Enter Unit Price: ₹");
        float newUnitPrice = sc.nextFloat();
        sc.nextLine();
        System.out.print("Enter Product Category: ");
        String newProductCategory = sc.nextLine();
        System.out.print("Enter Tax: %");
        float newTax = sc.nextFloat();
        sc.nextLine();
        ProductInfo newProduct = new ProductInfo(newProductId, newProductName, newProductDescription, productUnit, newUnitPrice, newProductCategory, newTax);
        prods.addProduct(newProduct);
        System.out.println("Product added successfully!");
		
	}

}