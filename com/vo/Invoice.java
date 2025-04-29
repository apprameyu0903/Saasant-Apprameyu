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
        this.productDetailsList = new ArrayList<>();
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
}
