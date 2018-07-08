package com.gk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.DepartmentDAO;
import com.gk.dao.UsersDAO;
import com.gk.model.Department;

public class DepartmentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DepartmentDAO departmentDAO;
	
	private static String WEBINF = "/WEB-INF/jsp/";
	private static String DASHBOARD = "/dashboard.jsp";
	private static String DEPARTMENT = WEBINF + "department.jsp";
	private static String LISTDEPARTMENTS = WEBINF + "listDepartments.jsp";
	
    public DepartmentController() {
        super();
        departmentDAO = new DepartmentDAO();
    }
    
    private boolean userCanAccessController(HttpServletRequest request) {
    	return (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole));
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			
			String departmentID = request.getParameter("departmentID");
			String action = request.getParameter("action");
			
			if (action == null || action.trim().length() == 0) {;
				if (departmentID == null || departmentID.length() == 0) {
					forward = DEPARTMENT;
				} else {
					Department department = departmentDAO.getDepartment(Integer.parseInt(departmentID));
					request.setAttribute("department", department);
					forward = DEPARTMENT;
				}
			} else if (action.equals("list")) {
				request.setAttribute("departments", departmentDAO.getDepartments());
				forward = LISTDEPARTMENTS;
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private List<String> validateDepartment(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		
		String title = request.getParameter("title");
		if (title == null || title.trim().length() == 0)
			errors.add("Title should not be empty.");
		
		String semesters = request.getParameter("semesters");
		if (semesters == null || semesters.trim().length() == 0)
			semesters = "8";
		if (!ControllerUtilities.isInteger(semesters))
			errors.add("Semesters should be a number. Default is 8.");
		else {
			int semestersInteger = Integer.parseInt(semesters);
			if (semestersInteger < 1 || semestersInteger > 20)
				errors.add("Semesters value is invalid. Minimum value is 1 and maximum value is 20.");
		}
		
		String graduationUnits = request.getParameter("graduationUnits");
		if (graduationUnits == null || graduationUnits.trim().length() == 0)
			graduationUnits = "58";
		if (!ControllerUtilities.isInteger(graduationUnits))
			errors.add("Graduation units should be a number. Default is 58.");
		else {
			int graduationUnitsInteger = Integer.parseInt(graduationUnits);
			if (graduationUnitsInteger < 1 || graduationUnitsInteger > 1000)
				errors.add("Graduation units value is invalid. Minimum value is 1 and maximum value is 1000.");
		}
		
		return errors;
	}
	
	private Department createDepartment(HttpServletRequest request) {
		Department department = new Department();
		String departmentID = request.getParameter("departmentID");
		if (departmentID != null && departmentID.trim().length() > 0 && ControllerUtilities.isInteger(departmentID))
			department.setDepartmentID(Integer.parseInt(departmentID));
		
		String title = request.getParameter("title");
		int semesters = Integer.parseInt(request.getParameter("semesters"));
		int graduationUnits = Integer.parseInt(request.getParameter("graduationUnits"));
		department.setTitle(title);
		department.setSemesters(semesters);
		department.setGraduationUnits(graduationUnits);
		
		return department;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			
			String departmentID = request.getParameter("departmentID");
			
			List<String> errors = validateDepartment(request);
			Department department = createDepartment(request);
			
			if (departmentID == null || departmentID.length() == 0) {
				if (!errors.isEmpty()) {
					forward = DEPARTMENT;
				} else {
					boolean departmentAdded = departmentDAO.addDepartment(department);
					if (!departmentAdded) {
						errors.add("A database error occured. Cannot add a department at this time. Please try again later.");
						forward = DEPARTMENT;
					} else {
						forward = DASHBOARD;
					}
				}
			} else {
				String action = request.getParameter("action");
				
				if (action == null || action.trim().length() == 0) {
					if (!errors.isEmpty()) {
						forward = DEPARTMENT;
					} else {
						boolean departmentUpdated = departmentDAO.updateDepartment(department);
						if (!departmentUpdated) {
							errors.add("A database error occured. Cannot update department at this time. Please try again later.");
						}
						forward = DEPARTMENT;
					}
				}
			}
			
			request.setAttribute("errors", errors);
			request.setAttribute("department", department);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
}
