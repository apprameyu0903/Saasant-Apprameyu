package com.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Invoice
{
    private MerchantDetails merchantDetails;
    private CustomerDetails customerDetails;
    private List<ProductInfo> productDetailsList;
    private float subTotal;
    private float gst;
    private float grandTotal;
    private LocalDate dueDate;
    private LocalDateTime dateTime;

    public Invoice(MerchantDetails merchantDetails, CustomerDetails customerDetails,
                   List<ProductInfo> productDetailsList, float subTotal,
                   float gst, float grandTotal,
                   LocalDate dueDate, LocalDateTime dateTime) {
        this.merchantDetails = merchantDetails;
        this.customerDetails = customerDetails;
        this.productDetailsList = productDetailsList;
        this.subTotal = subTotal;
        this.gst = gst;
        this.grandTotal = grandTotal;
        this.dueDate = dueDate;
        this.dateTime = dateTime;
    }

    public MerchantDetails getMerchantDetails() {
        return merchantDetails;
    }

    public void setMerchantDetails(MerchantDetails merchantDetails) {
        this.merchantDetails = merchantDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public List<ProductInfo> getProductDetailsList() {
        return productDetailsList;
    }

    public void setProductDetailsList(List<ProductInfo> productDetailsList) {
        this.productDetailsList = productDetailsList;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "merchantDetails=" + merchantDetails +
                ", customerDetails=" + customerDetails +
                ", productDetailsList=" + productDetailsList +
                ", subTotal=" + subTotal +
                ", gst=" + gst +
                ", grandTotal=" + grandTotal +
                ", dueDate=" + dueDate +
                ", dateTime=" + dateTime +
                '}';
    }

    public void printInvoice() {
        System.out.println("\n============================ INVOICE ==============================");
        System.out.println("Merchant: " + merchantDetails.getMerchantName());
        System.out.println("Customer: " + customerDetails.getCustomerName());
        System.out.println("Customer ID: " + customerDetails.getCustomerId());
        System.out.println("Customer Mobile Number: " + customerDetails.getMobileNumber());
        System.out.println("Date: " + dateTime);
        System.out.println("Due Date: " + dueDate);
        System.out.println("--------------------------------------------------------------------");
        System.out.println("ID        Description               Price     Quantity  Tax(%)   Total Price");
        System.out.println("--------------------------------------------------------------------");

        for (ProductInfo product : productDetailsList) {
            String line = product.getProductId() + "    " +
                    String.format("%-25s", product.getProductDescription()) +
                    "₹" + String.format("%-9.2f", product.getUnitPrice()) + " " +
                    String.format("%-9d", product.getQuantity()) +
                    String.format("%-8.2f", product.getTax()) + "% " +
                    "₹" + String.format("%.2f", product.getTotalAmount());
            System.out.println(line);
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.println(String.format("%-55s ₹%.2f", "Subtotal:", subTotal));
        System.out.println(String.format("%-55s ₹%.2f", "GST (18%):", gst));
        System.out.println(String.format("%-55s ₹%.2f", "Grand Total:", grandTotal));
        System.out.println("====================================================================");
    }

}


