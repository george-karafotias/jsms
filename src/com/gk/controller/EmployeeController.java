package com.gk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.DepartmentDAO;
import com.gk.dao.EmployeeDAO;
import com.gk.dao.UsersDAO;
import com.gk.model.Department;
import com.gk.model.Employee;

public class EmployeeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private EmployeeDAO employeeDAO;
	private DepartmentDAO departmentDAO;

	private static String WEBINF = "/WEB-INF/jsp/";
	private static String DASHBOARD = "/dashboard.jsp";
	private static String EMPLOYEE = WEBINF + "employee.jsp";
	private static String LISTEMPLOYEES = WEBINF + "listEmployees.jsp";

	public EmployeeController() {
		super();
		employeeDAO = new EmployeeDAO();
		departmentDAO = new DepartmentDAO();
	}
	
	private boolean userCanAccessController(HttpServletRequest request) {
    	return (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole));
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			
			String employeeID = request.getParameter("employeeID");
			String action = request.getParameter("action");

			if (action == null || action.trim().length() == 0) {
				List<Department> departments = departmentDAO.getDepartments();
				if (employeeID == null || employeeID.length() == 0) {
					forward = EMPLOYEE;
				} else {
					Employee employee = employeeDAO.getEmployee(Integer.parseInt(employeeID));
					request.setAttribute("employee", employee);
					forward = EMPLOYEE;
				}
				request.setAttribute("departments", departments);
			} else if (action.equals("find")) {
				if (request.getParameter("s") != null && request.getParameter("s").trim().length() > 0) {
					request.setAttribute("employees", employeeDAO.findEmployees(request.getParameter("s").trim()));
				}
				forward = LISTEMPLOYEES;
			} else if (action.equals("delete")) {
				boolean employeeDeleted = employeeDAO.deleteEmployee(Integer.parseInt(employeeID));
				if (!employeeDeleted) {
					List<String> errors = new ArrayList<String>();
					errors.add("A database error occured. Cannot delete employee at this time. Please try again later.");
					request.setAttribute("errors", errors);
				}
				forward = LISTEMPLOYEES;
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (userCanAccessController(request)) {
			
			String employeeID = request.getParameter("employeeID");

			List<String> errors = validateEmployee(request);
			List<Department> departments = departmentDAO.getDepartments();
			Employee employee = createEmployee(request);
			
			if (employeeID == null || employeeID.length() == 0) {
				if (!errors.isEmpty()) {
					forward = EMPLOYEE;
				} else {
					boolean employeeAdded = employeeDAO.addEmployee(employee);
					if (!employeeAdded) {
						errors.add("A database error occured. Cannot add an employee at this time. Please try again later.");
						forward = EMPLOYEE;
					} else {
						forward = DASHBOARD;
					}
				}
			} else {
				String action = request.getParameter("action");
				
				if (action == null || action.trim().length() == 0) {
					if (!errors.isEmpty()) {
						forward = EMPLOYEE;
					} else {
						boolean employeeUpdated = employeeDAO.updateEmployee(employee);
						if (!employeeUpdated) {
							errors.add("A database error occured. Cannot update employee at this time. Please try again later.");
						}
						forward = EMPLOYEE;
					}
				}
			}
			
			request.setAttribute("errors", errors);
			request.setAttribute("departments", departments);
			request.setAttribute("employee", employee);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private Employee createEmployee(HttpServletRequest request) {
		String employeeID = request.getParameter("employeeID");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String genderCode = request.getParameter("genderCode");
		String designation = request.getParameter("designation");
		boolean activeFlag = false;
		if (request.getParameter("activeFlag") != null && request.getParameter("activeFlag").equals("true"))
			activeFlag = true;
		Date hireDate = ControllerUtilities.stringToDate(request.getParameter("hireDate"));
		Date birthDate = ControllerUtilities.stringToDate(request.getParameter("birthDate"));
		String officePhoneNumber = request.getParameter("officePhoneNumber");
		String mobileNumber = request.getParameter("mobileNumber");
		String address = request.getParameter("address");
		
		Employee employee = new Employee();
		if (employeeID != null && employeeID.trim().length() > 0 && ControllerUtilities.isInteger(employeeID)) {
			employee.setEmployeeID(Integer.parseInt(employeeID));
		}
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setGenderCode(genderCode);
		employee.setDesignation(designation);
		if (ControllerUtilities.isInteger(request.getParameter("departmentID")))
			employee.setDepartmentID(Integer.parseInt(request.getParameter("departmentID")));
		employee.setActiveFlag(activeFlag);
		employee.setHireDate(hireDate);
		employee.setBirthDate(birthDate);
		employee.setOfficePhoneNumber(officePhoneNumber);
		employee.setMobileNumber(mobileNumber);
		employee.setAddress(address);
		
		return employee;
	}

	private List<String> validateEmployee(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
	
		String firstName = request.getParameter("firstName");
		if (firstName == null || firstName.trim().length() == 0)
			errors.add("First name should not be empty.");
		
		String lastName = request.getParameter("lastName");
		if (lastName == null || lastName.trim().length() == 0)
			errors.add("Last name should not be empty.");
		
		String designation = request.getParameter("designation");
		if (designation == null || designation.trim().length() == 0)
			errors.add("Employee's designation should not be empty.");
		
		String genderCode = request.getParameter("genderCode");
		if (genderCode == null || (!genderCode.trim().equalsIgnoreCase("m") && !genderCode.trim().equalsIgnoreCase("f")))
			errors.add("Gender code should be either m (for male) or f (for female).");
		
		if (!ControllerUtilities.isInteger(request.getParameter("departmentID")) || request.getParameter("departmentID").equals("0"))
			errors.add("Department ID value is not valid.");
		
		return errors;
	}
}
