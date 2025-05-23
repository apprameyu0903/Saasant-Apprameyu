<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Output Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
            box-sizing: border-box;
        }
        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
            text-align: left;
        }
        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 25px;
        }
        p {
            font-size: 1.1em;
            color: #555;
            margin-bottom: 12px;
            line-height: 1.6;
        }
        strong {
            color: #007bff;
        }
        .output-item {
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        .output-item:last-child {
            border-bottom: none;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 18px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            transition: background-color 0.3s;
        }
        a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Submitted Information</h2>

        <%
            String name = (String) request.getAttribute("submittedName");
            String email = (String) request.getAttribute("submittedEmail");
            Integer age = (Integer) request.getAttribute("submittedAge"); // Retrieve as Integer

            // Handle null values gracefully for display
            name = (name != null) ? name : "Not provided";
            email = (email != null) ? email : "Not provided";
            String ageDisplay = (age != null && age > 0) ? String.valueOf(age) : "Not provided";
        %>

        <div class="output-item">
            <p><strong>Name:</strong> <%= name %></p>
        </div>
        <div class="output-item">
            <p><strong>Email:</strong> <%= email %></p>
        </div>
        <div class="output-item">
            <p><strong>Age:</strong> <%= ageDisplay %></p>
        </div>

        <div style="text-align:center;">
             <a href="index.html">Go Back to Form</a>
        </div>
    </div>
</body>
</html>
