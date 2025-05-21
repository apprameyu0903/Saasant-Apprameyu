package com.thirdspring1905.controller;

import com.thirdspring1905.exception.BusinessException;
import com.thirdspring1905.exception.DataNotFoundException;
import com.thirdspring1905.model.Employee;
import com.thirdspring1905.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
//import java.util.UUID;


import org.slf4j.MDC;
import java.util.UUID;


@Controller
@RequestMapping("/employees")
public class EmployeeController {
	
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    
    private final EmployeeService employeeService;
    

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(Model model) {
      //  String txId = UUID.randomUUID().toString();
    	String txId   = UUID.randomUUID().toString();
        long   start  = System.currentTimeMillis();
        MDC.put("txId", txId);
 
        logger.info("Listing employees in the database");
        
        model.addAttribute("employees", employeeService.getAll());
        String view = "employee/list";
     // — end tracking
        long elapsed = System.currentTimeMillis() - start;
        logger.info("[{}] Completed listEmployees in {} ms", txId, elapsed);
        MDC.clear();
        return view;
    }

    @GetMapping({"/add", "/edit/{id}"})
    public String showForm(@PathVariable(required = false) String id, Model model) {
    //    String txId = UUID.randomUUID().toString();
        logger.info("Showing form for Add new Employee", id != null ? id : "new");
        
        if (id != null) {
            model.addAttribute("employee", employeeService.getById(id));
        } else {
            model.addAttribute("employee", new Employee());
        }
        model.addAttribute("positions", List.of("Manager", "Developer", "Analyst"));
        return "employee/form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
       // String txId = UUID.randomUUID().toString();
        logger.info("Saving employee id: [{}]", employee.getEmpId());
        
        try {
            employeeService.create(employee);
            redirectAttributes.addFlashAttribute("employee", employee);
            return "redirect:/employees/success";
        } catch (BusinessException e) {
            logger.error(" Error saving employee:[{}]", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employees/add";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        //String txId = UUID.randomUUID().toString();
        logger.info("Showing success page after adding new employee");
        return "employee/success";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        //String txId = UUID.randomUUID().toString();
        logger.info("Updating employee id [{}] ", employee.getEmpId());
        
        try {
            boolean updated = employeeService.update(employee);
            if (updated) {
                redirectAttributes.addFlashAttribute("message", "Employee updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Update failed");
            }
        } catch (DataNotFoundException e) {
            logger.error("Error updating employee: [{}]", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable String id, RedirectAttributes redirectAttributes) {
       // String txId = UUID.randomUUID().toString();
        logger.info("[{}] Deleting employee: {}", id);
        
        try {
            boolean deleted = employeeService.delete(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("message", "Employee deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Delete failed");
            }
        } catch (DataNotFoundException e) {
            logger.error("Error deleting employee: [{}]", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employees";
    }

    @GetMapping("/check-id")
    @ResponseBody
    public boolean checkEmployeeId(@RequestParam String empId) {
      //  String txId = UUID.randomUUID().toString();
        logger.info("Checking employee ID: [{}]", empId);
        return employeeService.existsById(empId);
    }
}