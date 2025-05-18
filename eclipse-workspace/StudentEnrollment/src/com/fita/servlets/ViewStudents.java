package com.fita.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fita.dao.StudentDao;
import com.fita.model.StudentDetails;

/**
 * Servlet implementation class ViewStudents
 */
@WebServlet("/ViewStudents")
public class ViewStudents extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewStudents() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		StudentDao stdao= new StudentDao();
		List<StudentDetails> stList = stdao.fetchStudentRecords();
		
		PrintWriter out = response.getWriter();
		out.print("<table style='border:2px solid'>");
		
		for(StudentDetails st :stList)
		{
		out.print("<tr>");
		
		out.print("<td>");
		out.print(st.getStudentId());
		out.print("</td>");
		
		out.print("<td>");
		out.print(st.getStudentName());
		out.print("</td>");
		out.print("<td>");
		out.print(st.getCourse());
		out.print("</td>");
		
		out.print("</tr>");
		}
		out.print("</table>");
		
		
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
