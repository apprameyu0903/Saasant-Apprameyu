package com.fita.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fita.dao.StudentDao;
import com.fita.model.StudentDetails;

/**
 * Servlet implementation class StudentServlet
 */
@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StudentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		StudentDetails student = new StudentDetails();
		student.setStudentId((int) (Math.random()* ((9999 - 1000) + 1) + 1000));
		student.setStudentName(request.getParameter("Firstname"));
		student.setCourse(request.getParameter("Courses"));
		student.setEmail(request.getParameter("emailid"));
		student.setMobileNo(request.getParameter("mobileno"));
		student.setStartDate(LocalDate.parse(request.getParameter("startDate")));
		student.setEndDate(LocalDate.parse(request.getParameter("endDate")));
		
		StudentDao stDao = new StudentDao();///
		int insertStatus= stDao.insertStudent( student) ;
		
		response.sendRedirect("ViewStudents");
		
		/*
		 * RequestDispatcher rd =
		 * getServletContext().getRequestDispatcher("/index.html"); PrintWriter out=
		 * response.getWriter();
		 * out.println("<font color=red>Insert status "+insertStatus+".</font>");
		 * rd.include(request, response);
		 */
		
		
		
	}

}
