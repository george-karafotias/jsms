package com.gk.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.CourseDAO;
import com.gk.dao.DepartmentDAO;
import com.gk.dao.GradeDAO;
import com.gk.model.Course;
import com.gk.model.Department;

public class GradeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CourseDAO courseDAO;
	private GradeDAO gradeDAO;
	private DepartmentDAO departmentDAO;
	
	private static String WEBINF = "/WEB-INF/jsp/";
	private static String GRADES = WEBINF + "grades.jsp";
       
    public GradeController() {
        super();
        courseDAO = new CourseDAO();
        gradeDAO = new GradeDAO();
        departmentDAO = new DepartmentDAO();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = GRADES;
		String departmentID = request.getParameter("departmentID");
		String courseID = request.getParameter("courseID");
		String examPeriod = request.getParameter("examPeriod");
		String action = request.getParameter("action");
		
		List<Department> departments = departmentDAO.getDepartments();
		List<Course> courses = new ArrayList<Course>();
		List<java.util.Date> examPeriods = new ArrayList<java.util.Date>();
		
		if (departmentID == null && departments != null && departments.size() > 0)
			departmentID = String.valueOf(departments.get(0).getDepartmentID());
		
		if (ControllerUtilities.isInteger(departmentID))
			courses = courseDAO.getDepartmentCourses(Integer.parseInt(departmentID));
		
		if (courseID == null && courses != null && courses.size() > 0)
			courseID = String.valueOf(courses.get(0).getCourseID());
		
		if (ControllerUtilities.isInteger(courseID))
			examPeriods = gradeDAO.getCourseExamPeriods(Integer.parseInt(courseID));
		
		if (action!= null && action.equals("find")) {
			if (ControllerUtilities.isInteger(courseID)) {
				if (examPeriod != null) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
					try {
						java.util.Date parsedExamPeriod = formatter.parse(examPeriod);
						request.setAttribute("grades", gradeDAO.getGrades(Integer.parseInt(courseID), parsedExamPeriod));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		request.setAttribute("departments", departments);
		request.setAttribute("courses", courses);
		request.setAttribute("examPeriods", examPeriods);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
