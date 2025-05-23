<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.vo.CustomerDetails" %>

<%!
    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Customer</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <%
        CustomerDetails customer = (CustomerDetails) request.getAttribute("customer");
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage == null) { errorMessage = request.getParameter("error"); }
    %>

    <div class="container">
         <div class="page-header">
             <h1 class="header-title">Billing System - Edit Customer</h1>
         </div>
         <div class="content-wrapper">
            <div class="title-section">
                 <h2 class="page-title">Edit Customer:
                    <% if (customer != null) { %>
                         <%= escapeHtml(customer.getCustomerName()) %>
                    <% } else { %>
                         [Not Found]
                    <% } %>
                 </h2>
            </div>

            <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
               <p style="color: red; margin-bottom: 15px;">Error: <%= escapeHtml(errorMessage) %></p>
            <% } %>
            <% if (customer != null) { %>
                <form action="CustomerServlet" method="post" class="customer-form">
                    <input type="hidden" name="action" value="update"/>
                    <input type="hidden" name="customerId" value="<%= escapeHtml(customer.getCustomerId()) %>"/>

                    <div class="form-group">
                        <label for="customerName" class="form-label">Customer Name:</label>
                        <input type="text" id="customerName" name="customerName" value="<%= escapeHtml(customer.getCustomerName()) %>" class="form-input" required>
                    </div>
                    <div class="form-group">
                        <label for="mobileNumber" class="form-label">Mobile Number:</label>
                        <input type="tel" id="mobileNumber" name="mobileNumber" value="<%= escapeHtml(customer.getMobileNumber()) %>" class="form-input" required pattern="[0-9]{10}" title="Enter a 10-digit mobile number">
                    </div>
                    <div class="form-group">
                        <label for="customerLocation" class="form-label">Location:</label>
                        <input type="text" id="customerLocation" name="customerLocation" value="<%= escapeHtml(customer.getCustomerLocation()) %>" class="form-input">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="submit-btn">Update Customer</button>
                        <a href="CustomerServlet" class="cancel-btn-link"><button type="button" class="cancel-btn">Cancel</button></a>
                    </div>
                </form>
             <% } else { %>
                 <p>Customer details could not be loaded for editing.</p>
                 <a href="CustomerServlet">Back to List</a>
             <% } %>
        </div>
        <footer class="page-footer">
             &copy; 2025 Billing System
        </footer>
    </div>
</body>
</html>
