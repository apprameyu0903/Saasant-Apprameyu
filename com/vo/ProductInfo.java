package com.vo;

public class ProductInfo {

    private String productId;
    private String productDescription;
    private float unitPrice;
    private float tax;
    private int quantity;


    public ProductInfo(String productId, String productDescription, float unitPrice, float tax) {
        this(productId, productDescription, unitPrice, tax, 1); // default quantity = 1
    }

    public ProductInfo(String productId, String productDescription, float unitPrice, float tax, int quantity)
    {

        this.productId = productId;
        this.productDescription = productDescription;
        this.unitPrice = unitPrice;
        this.tax = tax;
        this.quantity = quantity;
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
        float taxAmount = unitPrice * (tax / 100);  // Tax is calculated as a percentage
        return (unitPrice + taxAmount) * quantity;  // Total amount = (Unit price + Tax) * Quantity
    }



}
