package com.saasant.firstSpringProject.vo;
public class ProductInfo {

    private String productId;
    private String productName;
    private String productDescription;
    private String unit;
    private float unitPrice;
    private String category;
    private float tax;
    private int quantity;


    public ProductInfo(String productId, String productName, String productDescription, String unit, float unitPrice, String category, float tax) {
        this(productId, productName, productDescription, unit, unitPrice, category, tax, 1); // default quantity = 1
    }

    public ProductInfo(String productId, String productName, String productDescription, String unit, float unitPrice, String category, float tax, int quantity)
    {

        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.category = category;
        this.tax = tax;
        this.quantity = quantity;
    }


    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalAmount() {
        float taxAmount = unitPrice * (tax / 100);  
        return (unitPrice + taxAmount) * quantity;  
    }




}
 