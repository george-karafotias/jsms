package com.gk.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.CourseDAO;
import com.gk.model.Course;
import com.google.gson.Gson;

public class DepartmentCoursesController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public DepartmentCoursesController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String departmentID = request.getParameter("departmentID");
		if (ControllerUtilities.isInteger(departmentID)) {
			CourseDAO courseDAO = new CourseDAO();
			List<Course> courses = courseDAO.getDepartmentCourses(Integer.parseInt(departmentID));
			String json = new Gson().toJson(courses);
			
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(json);
		}
	}
}
