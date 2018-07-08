package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.gk.model.Course;
import com.gk.model.Student;

public class CourseEnrollmentDAO extends BasicDAO {
	
	public CourseEnrollmentDAO() {
		super();
	}
	
	public List<Course> getSelectedCourses(int studentID) {
		List<Course> courses = new ArrayList<Course>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM course_enrollments WHERE STUDENT_ID=? AND COURSE_ID NOT IN(SELECT COURSE_ID FROM grades WHERE STUDENT_ID=? AND GRADE>=5);");
			statement.setInt(1, studentID);
			statement.setInt(2, studentID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Course course = new Course();
				course.setCourseID(resultSet.getInt("COURSE_ID"));
				courses.add(course);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
	
	public List<Course> getCoursesForSelection(Student student) {
		List<Course> courses = new ArrayList<Course>();
		
		if (student != null) {
			try {
				CourseDAO courseDAO = new CourseDAO();
				
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURSES AS c1 WHERE c1.DEPARTMENT_ID=? AND c1.REQUIRED=0 AND c1.COURSE_ID NOT IN (SELECT COURSE_ID FROM grades WHERE STUDENT_ID=? AND GRADE>=5);");
				statement.setInt(1, student.getDepartmentID());
				statement.setInt(2, student.getStudentID());
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					courses.add(courseDAO.createCourse(resultSet));
				}
				
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return courses;
	}
	
	public boolean deleteStudentEnrollments(Student student) {
		boolean deletedEnrollments = true;
		List<Course> coursesForSelection = getCoursesForSelection(student);
		
		if (coursesForSelection != null && coursesForSelection.size() > 0) {
			String[] courseIDsForSelection = new String[coursesForSelection.size()];
			for (int i=0; i<coursesForSelection.size(); i++) {
				courseIDsForSelection[i] = String.valueOf(coursesForSelection.get(i).getCourseID());
			}
			String courseIDsForSelectionString = String.join(",", courseIDsForSelection);
			
			try {
				PreparedStatement statement1 = connection.prepareStatement("DELETE FROM course_enrollments WHERE STUDENT_ID=? AND COURSE_ID IN (?);");
				statement1.setInt(1, student.getStudentID());
				statement1.setString(2, courseIDsForSelectionString);
				statement1.executeUpdate();
				statement1.close();
			} catch (SQLException e) {
				e.printStackTrace();
				deletedEnrollments = false;
			}
		}
		
		return deletedEnrollments;
	}
	
	public boolean enrollStudent(Student student, List<Integer> courseIDs) {
		boolean enrolledStudent = true;
		
		try {
			connection.setAutoCommit(false);
			
			boolean deletedStudentEnrollments = deleteStudentEnrollments(student);
			
			if (deletedStudentEnrollments) {
				PreparedStatement statement2 = connection.prepareStatement("INSERT INTO course_enrollments(STUDENT_ID, COURSE_ID) VALUES(?, ?);");
				for (int i=0; i<courseIDs.size(); i++) {
					int courseID = courseIDs.get(i);
					statement2.setInt(1, student.getStudentID());
					statement2.setInt(2, courseID);
					int insertedRows = statement2.executeUpdate();
					if (insertedRows != 1) {
						enrolledStudent = false;
						break;
					}
				statement2.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			enrolledStudent = false;
		} finally {
			try {
				if (!enrolledStudent)
					connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return enrolledStudent;
	}

}
