package com.billing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BillingSystemMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("helloworld");
		
		String hostname = "jdbc:mysql://localhost:3306/saasant_billing";
		String user = "root";
		String password = "@ppr@meY24";

		
		// mysql connecter
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // to access library from java program

			Connection con = DriverManager.getConnection(hostname, user, password);
			
			Statement st = con.createStatement();
			
			ResultSet rs = st.executeQuery("select * from products");
					
			while(rs.next()) {
				System.out.println(rs.getString(2) + "| " + rs.getString(3));
			}
			
			
			
			
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
