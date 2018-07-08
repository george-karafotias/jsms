package com.gk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.UsersDAO;
import com.gk.model.User;

public class UsersController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UsersDAO usersDAO;
	
	private static String WEBINF = "/WEB-INF/jsp/";
	private static String DASHBOARD = "/dashboard.jsp";
	private static String USER = WEBINF + "user.jsp";
	private static String LISTUSERS = WEBINF + "listUsers.jsp";
       
    public UsersController() {
        super();
        usersDAO = new UsersDAO();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (request.isUserInRole(UsersDAO.adminRole)) {
			String action = request.getParameter("action");
			
			if (action == null || action.trim().length() == 0) {
				forward = USER;
			} else if (action.equals("find")) { 
				if (request.getParameter("s") != null && request.getParameter("s").trim().length() > 0) {
					request.setAttribute("users", usersDAO.findUsers(request.getParameter("s").trim()));
				}
				forward = LISTUSERS;
			} else if (action.equals("delete")) {
				String userName = request.getParameter("userName");
				boolean userDeleted = usersDAO.deleteUser(userName);
				if (!userDeleted) {
					List<String> errors = new ArrayList<String>();
					errors.add("A database error occured. Cannot delete user at this time. Please try again later.");
					request.setAttribute("errors", errors);
				}
				forward = LISTUSERS;
			}
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forward = DASHBOARD;
		
		if (request.isUserInRole(UsersDAO.adminRole)) {
			List<String> errors = validateUser(request);
			User user = createUser(request);
			if (!errors.isEmpty()) {
				forward = USER;
			} else {
				boolean userAdded = usersDAO.addUser(user);
				if (userAdded) {
					forward = DASHBOARD;
				} else {
					errors.add("A database error occured. Cannot add a user at this time. Please try again later.");
					forward = USER;
				}
			}
			
			request.setAttribute("errors", errors);
			request.setAttribute("user", user);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
		dispatcher.forward(request, response);
	}
	
	private List<String> validateUser(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		
		String userName = request.getParameter("userName");
		if (userName == null || userName.trim().length() < 4)
			errors.add("Username should be at least 4 characters.");
		
		String password1 = request.getParameter("password1");
		if (password1 == null || password1.trim().length() < 4)
			errors.add("Password should be at least 4 characters");
		
		String password2 = request.getParameter("password2");
		if (password2 != null) {
			if (!password1.equals(password2))
				errors.add("Passwords do not match.");
		}
		
		String roleName = request.getParameter("roleName");
		if (roleName == null || (!roleName.equals(UsersDAO.adminRole) && !roleName.equals(UsersDAO.clerkRole)) && !roleName.equals(UsersDAO.teacherRole) && !!roleName.equals(UsersDAO.studentRole)) {
			errors.add("User role should be one of the predefined values.");
		}
		
		if (roleName.equals(UsersDAO.studentRole) || roleName.equals(UsersDAO.clerkRole) || roleName.equals(UsersDAO.teacherRole)) {
			if (!ControllerUtilities.isInteger(request.getParameter("personID")))
				errors.add("Person identifier should not be empty.");
		}
		
		return errors;
	}
	
	private User createUser(HttpServletRequest request) {
		String username = request.getParameter("userName");
		String password = request.getParameter("password1");
		String roleName = request.getParameter("roleName");
		User user = new User();
		if (ControllerUtilities.isInteger(request.getParameter("personID"))) {
			int personID = Integer.parseInt(request.getParameter("personID"));
			user.setPersonID(personID);
		}
		user.setUserName(username);
		user.setUserPass(password);
		user.setUserRole(roleName);
		
		return user;
	}
}
