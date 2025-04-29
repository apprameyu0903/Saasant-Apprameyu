package com.billing;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class BillingSystem {

    public static void main(String[] args) {
        MerchantDetails merchant = new MerchantDetails("SuperMart");

        CustomerDetails customer = new CustomerDetails(
                "C001", "Alice Johnson", "9876543210"
        );

        List<ProductInfo> products = new ArrayList<>();
        products.add(new ProductInfo("P101", "Organic Apples - 1kg",
                3.50f, 7.00f));
        products.add(new ProductInfo("P102", "Bananas - 1 dozen",
                2.00f, 2.00f));

        float subTotal = products.get(0).getTotalAmount() + products.get(1).getTotalAmount();
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
        System.out.print(merchant);
        System.out.println(products);
        System.out.println(customer);
        System.out.println(invoice);
    }
}
