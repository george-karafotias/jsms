package com.gk.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.GradeDAO;
import com.gk.dao.UsersDAO;

public class AddNewExamPeriodController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private GradeDAO gradeDAO;
	
    public AddNewExamPeriodController() {
        super();
        gradeDAO = new GradeDAO();
    }
    
    private boolean userCanAccessController(HttpServletRequest request) {
    	return (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole) || (request.isUserInRole(UsersDAO.teacherRole)));
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (userCanAccessController(request)) {
			String courseID = request.getParameter("courseID");
			String newExamPeriodYear = request.getParameter("newExamPeriodYear");
			String newExamPeriodMonth = request.getParameter("newExamPeriodMonth");
			
			boolean addedNewExam = false;
			String responseString = "";
			
			if (ControllerUtilities.isInteger(courseID)) {
				if (ControllerUtilities.isYear(newExamPeriodYear)) {
					if (ControllerUtilities.isMonth(newExamPeriodMonth)) {
						java.util.Date newExamPeriod = ControllerUtilities.createDate(Integer.parseInt(newExamPeriodYear), Integer.parseInt(newExamPeriodMonth));
						if (gradeDAO.CourseExamPeriodExists(Integer.parseInt(courseID), newExamPeriod)) {
							responseString = "Exam period already exists.";
						} else {
							addedNewExam = gradeDAO.addNewExamPeriod(Integer.parseInt(courseID), newExamPeriod);
						}
					}
				}
			}
			
			if (!addedNewExam && responseString == "") {
				responseString = "Failed to add a new exam period.";
			}
			
			response.setContentType("text/plain");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(responseString);
		}
	}
}
