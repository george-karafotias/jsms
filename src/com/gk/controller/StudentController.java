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
import com.gk.dao.GradeDAO;
import com.gk.dao.StudentDAO;
import com.gk.dao.UsersDAO;
import com.gk.model.Department;
import com.gk.model.Grade;
import com.gk.model.Student;
import com.gk.model.User;

public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private StudentDAO studentDAO;
	private DepartmentDAO departmentDAO;
	
	private static String WEBINF = "/WEB-INF/jsp/";
	private static String DASHBOARD = "/dashboard.jsp";
	private static String STUDENT = WEBINF + "student.jsp";
	private static String LISTSTUDENTS = WEBINF + "listStudents.jsp";
	private static String STUDENTGRADES = WEBINF + "studentGrades.jsp";
       
    public StudentController() {
        super();
        studentDAO = new StudentDAO();
        departmentDAO = new DepartmentDAO();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole)) {
			
			String studentID = request.getParameter("studentID");
			String action = request.getParameter("action");
			
			if (action == null || action.trim().length() == 0) {
				List<Department> departments = departmentDAO.getDepartments();
				if (studentID == null || studentID.length() == 0) {
					forward = STUDENT;
				} else {
					Student student = studentDAO.getStudent(Integer.parseInt(studentID));
					request.setAttribute("student", student);
					forward = STUDENT;
				}
				request.setAttribute("departments", departments);
			} else if (action.equals("find")) { 
				if (request.getParameter("s") != null && request.getParameter("s").trim().length() > 0) {
					request.setAttribute("students", studentDAO.findStudents(request.getParameter("s").trim()));
				}
				forward = LISTSTUDENTS;
			} else if (action.equals("delete")) {
				boolean studentDeleted = studentDAO.deleteStudent(Integer.parseInt(studentID));
				if (!studentDeleted) {
					List<String> errors = new ArrayList<String>();
					errors.add("A database error occured. Cannot delete student at this time. Please try again later.");
					request.setAttribute("errors", errors);
				}
				forward = LISTSTUDENTS;
			}
		} else if (request.isUserInRole(UsersDAO.studentRole)) {
			if (request.getUserPrincipal().getName() != null) {
				User user = new UsersDAO().getUser(request.getUserPrincipal().getName());
				List<Grade> grades = new GradeDAO().getGrades(user.getPersonID());
				if (grades != null && grades.size() > 0) {
					Student student = grades.get(0).getStudent();
					request.setAttribute("student", student);
					request.setAttribute("departmentTitle", student.getDepartmentTitle());
					request.setAttribute("grades", grades);
					request.setAttribute("gbp", calculateGBP(grades));
					forward = STUDENTGRADES;
				}
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private double calculateGBP(List<Grade> grades) {
		double gbp = 0;
		for (Grade grade: grades) {
			if (grade.getGrade() >= 5)
				gbp+=grade.getGrade();
		}
		return gbp/grades.size();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (request.isUserInRole(UsersDAO.adminRole) || request.isUserInRole(UsersDAO.clerkRole)) {
			
			String studentID = request.getParameter("studentID");
			
			List<String> errors = validateStudent(request);
			List<Department> departments = departmentDAO.getDepartments();
			Student student = createStudent(request);
			
			if (studentID == null || studentID.length() == 0) {
				if (!errors.isEmpty()) {
					forward = STUDENT;
				} else {
					boolean studentAdded = studentDAO.addStudent(student);
					if (!studentAdded) {
						errors.add("A database error occured. Cannot add a student at this time. Please try again later.");
						forward = STUDENT;
					} else {
						forward = DASHBOARD;
					}
				}
			} else {
				String action = request.getParameter("action");
				
				if (action == null || action.trim().length() == 0) {
					if (!errors.isEmpty()) {
						forward = STUDENT;
					} else {
						boolean studentUpdated = studentDAO.updateStudent(student);
						if (!studentUpdated) {
							errors.add("A database error occured. Cannot update student at this time. Please try again later.");
						}
						forward = STUDENT;
					}
				}
			}
			
			request.setAttribute("errors", errors);
			request.setAttribute("departments", departments);
			request.setAttribute("student", student);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private List<String> validateStudent(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		
		String code = request.getParameter("code");
		if (code == null || code.trim().length() != 4)
			errors.add("Code should be exactly 4 characters.");
		
		String firstName = request.getParameter("firstName");
		if (firstName == null || firstName.trim().length() == 0)
			errors.add("First name should not be empty.");
		
		String lastName = request.getParameter("lastName");
		if (lastName == null || lastName.trim().length() == 0)
			errors.add("Last name should not be empty.");
		
		String genderCode = request.getParameter("genderCode");
		if (genderCode == null || (!genderCode.trim().equalsIgnoreCase("m") && !genderCode.trim().equalsIgnoreCase("f")))
			errors.add("Gender code should be either m (for male) or f (for female).");
		
		Date entryDate = ControllerUtilities.stringToDate(request.getParameter("entryDate"));
		if (entryDate == null)
			errors.add("Entry date should not be empty. Entry date should be in yyyy/MM/dd format.");
		
		if (!ControllerUtilities.isInteger(request.getParameter("departmentID")) || request.getParameter("departmentID").equals("0"))
			errors.add("Department ID value is not valid.");
		
		return errors;
	}
	
	private Student createStudent(HttpServletRequest request) {
		String studentID = request.getParameter("studentID");
		String code = request.getParameter("code");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String genderCode = request.getParameter("genderCode");
		Date entryDate = ControllerUtilities.stringToDate(request.getParameter("entryDate"));
		boolean activeFlag = false;
		if (request.getParameter("activeFlag") != null && request.getParameter("activeFlag").equals("true"))
			activeFlag = true;
		Date birthDate = ControllerUtilities.stringToDate(request.getParameter("birthDate"));
		String birthCityName = request.getParameter("birthCityName");
		String birthCountryCode = request.getParameter("birthCountryCode");
		String mobileNumber = request.getParameter("mobileNumber");
		String address = request.getParameter("address");
		String motherName = request.getParameter("motherName");
		String fatherName = request.getParameter("fatherName");
		
		Student student = new Student();
		if (studentID != null && studentID.trim().length() > 0 && ControllerUtilities.isInteger(studentID)) {
			student.setStudentID(Integer.parseInt(studentID));
		}
		student.setCode(code);
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setGenderCode(genderCode);
		student.setActiveFlag(activeFlag);
		if (ControllerUtilities.isInteger(request.getParameter("departmentID")))
			student.setDepartmentID(Integer.parseInt(request.getParameter("departmentID")));
		student.setBirthDate(birthDate);
		student.setEntryDate(entryDate);
		student.setBirthCityName(birthCityName);
		student.setBirthCountryCode(birthCountryCode);
		student.setMobileNumber(mobileNumber);
		student.setAddress(address);
		student.setMotherName(motherName);
		student.setFatherName(fatherName);
		
		return student;
	}

}
