package com.gk.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gk.dao.GradeDAO;
import com.google.gson.Gson;

public class CourseExamPeriodsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private GradeDAO gradeDAO;
	
    public CourseExamPeriodsController() {
        super();
        gradeDAO = new GradeDAO();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String courseID = request.getParameter("courseID");
		List<String> examPeriods = new ArrayList<String>();
		if (ControllerUtilities.isInteger(courseID)) {
			List<Date> courseExamPeriods = gradeDAO.getCourseExamPeriods(Integer.parseInt(courseID));
			if (courseExamPeriods != null) {
				for (int i=0; i<courseExamPeriods.size(); i++) {
					DateFormat df = new SimpleDateFormat("yyyy/MM");
					examPeriods.add(df.format(courseExamPeriods.get(i)));
				}
			}
		}
		
		String json = new Gson().toJson(examPeriods);
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	}
}
