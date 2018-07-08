package com.gk.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.GradeDAO;

public class SetGradeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SetGradeController() {
        super();
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String recordID = request.getParameter("recordID");
		String grade = request.getParameter("grade");
		
		boolean updatedGrade = false;
		
		if (ControllerUtilities.isInteger(recordID) && ControllerUtilities.isDouble(grade)) {
			double gradeDouble = Double.parseDouble(grade);
			if (gradeDouble >= 0 && gradeDouble <= 10) {
				updatedGrade = new GradeDAO().setGrade(Integer.parseInt(recordID), gradeDouble);
			}
		}
		
		String responseText = "";
		if (!updatedGrade)
			responseText = "Cannot update grade at this time.";
		
		response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(responseText);
	}
}
