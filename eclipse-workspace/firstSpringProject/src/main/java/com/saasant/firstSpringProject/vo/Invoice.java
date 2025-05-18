package com.saasant.firstSpringProject.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Invoice
{
    private MerchantDetails merchantDetails;
    private CustomerDetails customerDetails;
    private Map<String,ProductInfo> productDetailsList;
    private float subTotal;
    private float gst;
    private float grandTotal;
    private LocalDate dueDate;
    private LocalDateTime dateTime;

    public Invoice(MerchantDetails merchantDetails, CustomerDetails customerDetails,
                   Map<String,ProductInfo> productDetailsList, float subTotal,
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

    public Map<String,ProductInfo> getProductDetailsList() {
        return productDetailsList;
    }

    public void setProductDetailsList(Map<String,ProductInfo> productDetailsList) {
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
        System.out.println("Merchant          : " + merchantDetails.getMerchantName());
        System.out.println("Customer          : " + customerDetails.getCustomerName());
        System.out.println("Customer ID       : " + customerDetails.getCustomerId());
        System.out.println("Mobile Number     : " + customerDetails.getMobileNumber());
        System.out.println("Date              : " + dateTime);
        System.out.println("Due Date          : " + dueDate);
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("%-10s %-25s %-10s %-10s %-8s %-12s %-12s%n",
                "ID", "Description", "Price", "Quantity", "Tax(%)", "Tax Amt (₹)", "Total (₹)");
        System.out.println("--------------------------------------------------------------------");

        for (ProductInfo product : productDetailsList.values()) {
            double taxAmount = product.getUnitPrice() * product.getQuantity() * (product.getTax() / 100.0);

            System.out.printf("%-10s %-25s ₹%-9.2f %-10d %-8.2f ₹%-11.2f ₹%.2f%n",
                    product.getProductId(),
                    product.getProductDescription(),
                    product.getUnitPrice(),
                    product.getQuantity(),
                    product.getTax(),
                    taxAmount,
                    product.getTotalAmount());
        }

        System.out.println("--------------------------------------------------------------------");
        System.out.printf("Subtotal   : ₹%.2f%n", subTotal);
        System.out.printf("GST (18%%)  : ₹%.2f%n", gst);
        System.out.printf("Grand Total: ₹%.2f%n", grandTotal);
        System.out.println("====================================================================");

    }


}


