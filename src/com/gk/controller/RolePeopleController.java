package com.gk.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.UsersDAO;
import com.gk.model.DisplayableModel;
import com.google.gson.Gson;

public class RolePeopleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public RolePeopleController() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.isUserInRole(UsersDAO.adminRole)) {
			String roleName = request.getParameter("roleName");
			if (roleName != null && roleName.trim().length() > 0) {
				UsersDAO usersDAO = new UsersDAO();
				List<DisplayableModel> people = usersDAO.getUserRolePeopleIDs(roleName);
				
				String json = new Gson().toJson(people);
				response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write(json);
			}
		}
	}
}
