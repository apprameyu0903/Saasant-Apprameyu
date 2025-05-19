package com.saasant.firstSpringProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {

    @GetMapping({"/", "/customers"})
    public String showCustomerDetailsPage(){
        return "customer_details";
    }

    @GetMapping("/customers/add")
    public String showAddCustomerPage() {
        return "add_customer";
    }
}  