package com.billing;
import com.vo.CustomerDetails;
import com.vo.Invoice;
import com.vo.MerchantDetails;
import com.vo.ProductInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class BillingSystemMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Merchant Name: ");
        String merchantName = sc.nextLine();
        MerchantDetails merchant = new MerchantDetails(merchantName);


        System.out.print("Enter Customer id: ");
        String customerId = sc.nextLine();
        System.out.print("Enter Customer Name: ");
        String customerName = sc.nextLine();
        System.out.print("Enter Customer Mobile No.: ");
        String customerMobile = sc.nextLine();
        if(customerMobile.length() < 10){
            System.out.print("Please enter a valid Number: ");
            customerMobile = sc.nextLine();
        }
        CustomerDetails customer = new CustomerDetails(customerId, customerName, customerMobile);

        List<ProductInfo> products = new ArrayList<>();
        int choice;
        //products.add(new ProductInfo("P101", "Organic Apples - 1kg",3.50f, 7.00f));
        //products.add(new ProductInfo("P102", "Bananas - 1 dozen", 2.00f, 2.00f));
        do {
            System.out.println("----- MENU -----");
            System.out.println("1. Enter product details");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID: ");
                    String productId = sc.nextLine();

                    System.out.print("Enter Product Description: ");
                    String description = sc.nextLine();

                    System.out.print("Enter Unit Price: ");
                    float unitPrice = sc.nextFloat();

                    System.out.print("Enter Total Amount: ");
                    float totalAmount = sc.nextFloat();
                    sc.nextLine();

                    ProductInfo product = new ProductInfo(productId, description, unitPrice, totalAmount);
                    products.add(product);

                    System.out.println("Product added successfully!");
                    break;

                case 2:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 2);

        float subTotal = 0.0f;
        for (ProductInfo product : products) {
            subTotal += product.getTotalAmount();
        }
        float gst = subTotal * 0.10f;
        float grandTotal = subTotal + gst;

        Invoice invoice = new Invoice(
                merchant,
                customer,
                products,
                subTotal,
                gst,
                grandTotal,
                LocalDate.now().plusDays(30),
                LocalDateTime.now()
        );
        //System.out.print(merchant);
        //System.out.println(products);
        //System.out.println(customer);
        System.out.println(invoice);
    }
}
