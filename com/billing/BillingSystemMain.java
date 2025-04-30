package com.billing;

import com.vo.CustomerDetails;
import com.vo.Invoice;
import com.vo.MerchantDetails;
import com.vo.ProductInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class BillingSystemMain {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MerchantDetails merchant = new MerchantDetails("Apprameyu Silks");

        Map<String, CustomerDetails> customerDatabase = new HashMap<>();

        Map<String, ProductInfo> productCatalog = new HashMap<>();
        productCatalog.put("P001", new ProductInfo("P001", "Cotton Saree", 1200.00f, 5.0f));
        productCatalog.put("P002", new ProductInfo("P002", "Silk Saree", 3200.00f, 10f));
        productCatalog.put("P003", new ProductInfo("P003", "Dress Material", 800.00f, 7f));
        productCatalog.put("P004", new ProductInfo("P004", "Shirt Fabric", 500.00f, 5f));
        productCatalog.put("P005", new ProductInfo("P005", "Kurti", 1000.00f, 3f));

        List<ProductInfo> products = new ArrayList<>();
        int choice;
        do {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Update the Product Catalog");
            System.out.println("2. Billing System");
            System.out.println("3. Print Product Catalog");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nEnter New Product Details:");
                    System.out.print("Enter Product ID: ");
                    String newProductId = sc.nextLine();
                    System.out.print("Enter Product Description: ");
                    String newProductDescription = sc.nextLine();
                    System.out.print("Enter Unit Price: ₹");
                    float newUnitPrice = sc.nextFloat();
                    System.out.print("Enter Tax: ₹");
                    float newTax = sc.nextFloat();
                    sc.nextLine();
                    ProductInfo newProduct = new ProductInfo(newProductId, newProductDescription, newUnitPrice, newTax);
                    productCatalog.put(newProductId, newProduct);

                    System.out.println("Product added successfully!");
                    break;

                case 2:
                    System.out.print("Enter Customer ID: ");
                    String customerId = sc.nextLine();

                    CustomerDetails customer;

                    if (customerDatabase.containsKey(customerId)) {
                        customer = customerDatabase.get(customerId);
                        System.out.println("✔️ Existing customer found: " + customer.getCustomerName() + " (" + customer.getMobileNumber() + ")");
                    } else {
                        System.out.print("Enter Customer Name: ");
                        String customerName = sc.nextLine();
                        System.out.print("Enter Customer Mobile No.: ");
                        String customerMobile = sc.nextLine();
                        while (customerMobile.length() < 10) {
                            System.out.print("Please enter a valid 10-digit mobile number: ");
                            customerMobile = sc.nextLine();
                        }

                        customer = new CustomerDetails(customerId, customerName, customerMobile);
                        customerDatabase.put(customerId, customer);
                        System.out.println("✅ New customer added.");
                    }
                    int productChoice;
                    do {
                        System.out.print("Enter Product ID: ");
                        String productId = sc.nextLine();


                        ProductInfo product = productCatalog.get(productId);
                        if (product == null) {
                            System.out.println("⚠️ Product not found!");
                            continue;
                        }

                        System.out.print("Enter Quantity: ");
                        int quantity = sc.nextInt();
                        sc.nextLine();
                        ProductInfo billedProduct = new ProductInfo(
                                product.getProductId(),
                                product.getProductDescription(),
                                product.getUnitPrice(),
                                product.getTax()
                        );
                        billedProduct.setQuantity(quantity);
                        products.add(billedProduct);
                        System.out.println("Product added successfully!");

                        System.out.println("Do you want to add another product? (Y/N): ");
                        String addMore = sc.nextLine();
                        if (!addMore.equalsIgnoreCase("Y")) {
                            break;
                        }

                    } while (true);


                    float subTotal = 0.0f;
                    for (ProductInfo p : products) {
                        subTotal += p.getTotalAmount();
                    }
                    float gst = subTotal * 0.18f;
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
                    invoice.printInvoice();
                    break;

                case 3:
                    System.out.println("\n📦 Product Catalog:");
                    System.out.println("ID        Description               Price     Tax(%)");
                    System.out.println("-------------------------------------------------------");
                    for (ProductInfo p : productCatalog.values()) {
                        String line = p.getProductId() + "    " +
                                String.format("%-25s", p.getProductDescription()) +
                                "₹" + String.format("%.2f", p.getUnitPrice()) + "   " +
                                String.format("%.2f", p.getTax()) + "%";
                        System.out.println(line);
                    }
                    break;

                case 4:
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        } while (choice != 4);

        sc.close();
    }
}
