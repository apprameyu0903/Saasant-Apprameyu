package com.fita.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fita.model.StudentDetails;

public class StudentDao {

	String url="jdbc:mysql://localhost:3306/maridb";
	String username="root";
	String pass="root";
	
	Connection con;
	
	
	public int insertStudent(StudentDetails student) 
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con= DriverManager.getConnection(url,username,pass);
			
			//insert into student values(1233, 'Mari Raja','9597491550','mmariraja22@gmail.com', 'Java','2019-02-01','2019-03-01');
			
			String query="insert into student values (?,?,?,?,?,?,?)";
			
			PreparedStatement stmt= con.prepareStatement(query);
			stmt.setInt(1, student.getStudentId());
			stmt.setString(2, student.getStudentName());
			stmt.setString(3, student.getMobileNo());
			stmt.setString(4, student.getEmail());
			stmt.setString(5, student.getCourse());
			stmt.setDate(6, Date.valueOf(student.getStartDate()) );
			stmt.setDate(7, Date.valueOf(student.getEndDate()) );
			
			int returnStu =stmt.executeUpdate();
			
			return returnStu;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	
	//create table student (student_id int primary key not null, name varchar(50), 
	//mobileno varchar(15), email varchar(50), course varchar(40), 
	//start_date date, end_date date);
	
	public List<StudentDetails> fetchStudentRecords()
	{
		List<StudentDetails> stuList = new ArrayList<StudentDetails>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con= DriverManager.getConnection(url,username,pass);
			String query="select * from student";
			PreparedStatement stmt= con.prepareStatement(query);
			ResultSet rs =stmt.executeQuery();
			
			while(rs.next())
			{
				StudentDetails stu = new StudentDetails();
				stu.setStudentId(rs.getInt("student_id"));
				stu.setStudentName(rs.getString("name"));
				stu.setEmail(rs.getString("email"));
				stu.setMobileNo(rs.getString("mobileno"));
				stu.setCourse(rs.getString("course"));
				stu.setStartDate( rs.getDate("start_date").toLocalDate());
				stuList.add(stu);
			}
			
			
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return stuList;
	}
	
	
}
 