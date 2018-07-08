package com.gk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.CourseDAO;
import com.gk.dao.DepartmentDAO;
import com.gk.dao.EmployeeDAO;
import com.gk.dao.UsersDAO;
import com.gk.model.Course;
import com.gk.model.Department;
import com.gk.model.Employee;

public class CourseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private CourseDAO courseDAO;
	private EmployeeDAO employeeDAO;
	private DepartmentDAO departmentDAO;
	
	private static String WEBINF = "/WEB-INF/jsp/";
	private static String DASHBOARD = "/dashboard.jsp";
	private static String COURSE = WEBINF + "course.jsp";
	private static String LISTCOURSES = WEBINF + "listCourses.jsp";
	
    public CourseController() {
        super();
        courseDAO = new CourseDAO();
        employeeDAO = new EmployeeDAO();
        departmentDAO = new DepartmentDAO();
    }
    
    private boolean userCanAccessController(HttpServletRequest request) {
    	return (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole));
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			
			String courseID = request.getParameter("courseID");
			String action = request.getParameter("action");

			if (action == null || action.trim().length() == 0) {
				List<Department> departments = departmentDAO.getDepartments();
				List<Employee> teachers = employeeDAO.getTeachers();
				if (courseID == null || courseID.length() == 0) {
					forward = COURSE;
				} else {
					Course course = courseDAO.getCourse(Integer.parseInt(courseID));
					request.setAttribute("course", course);
					forward = COURSE;
				}
				
				request.setAttribute("departments", departments);
				request.setAttribute("teachers", teachers);
			} else if (action.equals("find")) {
				if (request.getParameter("s") != null && request.getParameter("s").trim().length() > 0) {
					request.setAttribute("courses", courseDAO.findCourses(request.getParameter("s").trim()));
				}
				forward = LISTCOURSES;
			} else if (action.equals("delete")) {
				boolean courseDeleted = courseDAO.deleteCourse(Integer.parseInt(courseID));
				if (!courseDeleted) {
					List<String> errors = new ArrayList<String>();
					errors.add("A database error occured. Cannot delete course at this time. Please try again later.");
					request.setAttribute("errors", errors);
				}
				forward = LISTCOURSES;
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			
			String courseID = request.getParameter("courseID");
			
			List<String> errors = validateCourse(request);
			List<Department> departments = departmentDAO.getDepartments();
			List<Employee> teachers = employeeDAO.getTeachers();
			Course course = createCourse(request);
			
			if (courseID == null || courseID.length() == 0) {
				if (!errors.isEmpty()) {
					forward = COURSE;
				} else {
					boolean courseAdded = courseDAO.addCourse(course);
					if (!courseAdded) {
						errors.add("A database error occured. Cannot add a course at this time. Please try again later.");
						forward = COURSE;
					} else {
						forward = DASHBOARD;
					}
				}
			} else {
				String action = request.getParameter("action");
				
				if (action == null || action.trim().length() == 0) {
					if (!errors.isEmpty()) {
						forward = COURSE;
					} else {
						boolean courseUpdated = courseDAO.updateCourse(course);
						if (!courseUpdated) {
							errors.add("A database error occured. Cannot update course at this time. Please try again later.");
						}
						forward = COURSE;
					}
				}
			}
			
			request.setAttribute("errors", errors);
			request.setAttribute("departments", departments);
			request.setAttribute("teachers", teachers);
			request.setAttribute("course", course);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private List<String> validateCourse(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		
		String title = request.getParameter("title");
		if (title == null || title.trim().length() == 0)
			errors.add("Title should not be empty.");
		
		if (!ControllerUtilities.isInteger(request.getParameter("departmentID")) || request.getParameter("departmentID").equals("0"))
			errors.add("Department ID value is not valid.");
		
		return errors;
	}
	
	private Course createCourse(HttpServletRequest request) {
		Course course = new Course();
		
		String courseID = request.getParameter("courseID");
		if (courseID != null && courseID.trim().length() > 0 && ControllerUtilities.isInteger(courseID)) {
			course.setCourseID(Integer.parseInt(courseID));
		}
	
		course.setTitle(request.getParameter("title"));
		course.setSemester(request.getParameter("semester"));
		
		if (ControllerUtilities.isInteger(request.getParameter("departmentID"))) {
			Department department = new Department();
			department.setDepartmentID(Integer.parseInt(request.getParameter("departmentID")));
			course.setDepartment(department);
		}
		
		String[] teacherIDs = request.getParameterValues("teachers");
		if (teacherIDs != null && teacherIDs.length > 0) {
			List<Employee> teachers = new ArrayList<Employee>();
			for (int i=0; i<teacherIDs.length; i++) {
				Employee teacher = new Employee();
				teacher.setEmployeeID(Integer.parseInt(teacherIDs[i]));
				teachers.add(teacher);
			}
			course.setTeachers(teachers);
		}
		
		return course;
	}

}
