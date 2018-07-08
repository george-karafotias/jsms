package com.gk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.CourseEnrollmentDAO;
import com.gk.dao.DepartmentDAO;
import com.gk.dao.StudentDAO;
import com.gk.dao.UsersDAO;
import com.gk.model.Course;
import com.gk.model.Department;
import com.gk.model.Student;
import com.gk.model.User;

public class CourseEnrollmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static String WEBINF = "/WEB-INF/jsp/";
	private static String DASHBOARD = "/dashboard.jsp";
	private static String ENROLLSTUDENT = WEBINF + "enrollStudent.jsp";
	
    public CourseEnrollmentController() {
        super();
    }
    
    private boolean userCanAccessController(HttpServletRequest request) {
    	if (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole)) {
    		return true;
    	} else if (request.isUserInRole(UsersDAO.studentRole)) {
			if (request.getUserPrincipal().getName() != null) {
				User user = new UsersDAO().getUser(request.getUserPrincipal().getName());
				int personID = user.getPersonID();
				String studentID = request.getParameter("studentID");
				if (ControllerUtilities.isInteger(studentID) && Integer.parseInt(studentID) != personID)
					return false;
				return true;
			}
    		return false;
    	} else {
    		return false;
    	}
    }
    
    private String getStudentID(HttpServletRequest request) {
    	if (request.isUserInRole(UsersDAO.studentRole)) {
    		User user = new UsersDAO().getUser(request.getUserPrincipal().getName());
			int personID = user.getPersonID();
			if (personID == 0) return "";
			return String.valueOf(personID);
    	} else {
    		return request.getParameter("studentID");
    	}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			forward = ENROLLSTUDENT;
			request = updateRequest(request);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private HttpServletRequest updateRequest(HttpServletRequest request) {
		String studentID = getStudentID(request);
		
		if (ControllerUtilities.isInteger(studentID)) {
			StudentDAO studentDAO = new StudentDAO();
			Student student = studentDAO.getStudent(Integer.parseInt(studentID));
			request.setAttribute("student", student);
			
			DepartmentDAO departmentDAO = new DepartmentDAO();
			Department department = departmentDAO.getDepartment(student.getDepartmentID());
			request.setAttribute("department", department);
			
			CourseEnrollmentDAO courseEnrollmentDAO = new CourseEnrollmentDAO();
			
			List<Course> coursesForSelection = courseEnrollmentDAO.getCoursesForSelection(student);
			request.setAttribute("courses", coursesForSelection);
		
			List<Course> studentSelectedCourses = courseEnrollmentDAO.getSelectedCourses(Integer.parseInt(studentID));
			List<Boolean> selected = new ArrayList<Boolean>(coursesForSelection.size());
			for (int i=0; i<coursesForSelection.size(); i++) {
				boolean isCourseSelected = false;
				int j = 0;
				while (!isCourseSelected && j < studentSelectedCourses.size()) {
					if (studentSelectedCourses.get(j).getCourseID() == coursesForSelection.get(i).getCourseID()) {
						isCourseSelected = true;
					}
					j++;
				}
				selected.add(isCourseSelected);
			}
			
			request.setAttribute("isCourseSelected", selected);
		}
		
		return request;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = ENROLLSTUDENT;
		boolean enrolledStudent = false;
		
		String studentID = getStudentID(request);
		if (ControllerUtilities.isInteger(studentID)) {
			Enumeration<String> parameterNames = request.getParameterNames();
			List<Integer> selectedCoursesID = new ArrayList<Integer>();
			while (parameterNames.hasMoreElements()) {
				String parameterName = parameterNames.nextElement();
				if (parameterName.startsWith("selected")) {
					String courseID = parameterName.replace("selected", "");
					if (ControllerUtilities.isInteger(courseID))
						selectedCoursesID.add(Integer.parseInt(courseID));
				}
			}
			
			StudentDAO studentDAO = new StudentDAO();
			Student student = studentDAO.getStudent(Integer.parseInt(studentID));
			CourseEnrollmentDAO courseEnrollmentDAO = new CourseEnrollmentDAO();
			
			if (selectedCoursesID.size() > 0) {
				enrolledStudent = courseEnrollmentDAO.enrollStudent(student, selectedCoursesID);
			} else {
				enrolledStudent = courseEnrollmentDAO.deleteStudentEnrollments(student);
			}
		}
		
		request = updateRequest(request);
		
		if (!enrolledStudent) {
			request.setAttribute("error", "Unable to enroll student. Please try again later.");
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}

}
