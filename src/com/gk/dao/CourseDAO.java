package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.gk.model.Course;
import com.gk.model.Department;
import com.gk.model.Employee;

public class CourseDAO extends BasicDAO {

	public CourseDAO() {
		super();
	}
	
	private boolean addCourseTeachers(Course course) {
		boolean addedCourseTeachers = true;
		
		try {
			int courseID = course.getCourseID();
			
			List<Employee> courseTeachers = course.getTeachers();
			if (courseTeachers != null) {
				for (int i=0; i<courseTeachers.size(); i++) {
					PreparedStatement statement = connection.prepareStatement("INSERT INTO course_teachers(COURSE_ID, EMPLOYEE_ID) VALUES(?, ?);");
					statement.setInt(1, courseID);
					statement.setInt(2, courseTeachers.get(i).getEmployeeID());
					int insertedCount2 = statement.executeUpdate();
					if (insertedCount2 == 0) {
						addedCourseTeachers = false;
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			addedCourseTeachers = false;
		}
		
		return addedCourseTeachers;
	}
	
	private boolean deleteCourseTeachers(int courseID) {
		boolean deletedCourse = true;
		
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM course_teachers WHERE COURSE_ID=?;");
			statement.setInt(1, courseID);
			statement.executeUpdate();
		} catch (SQLException e) {
			deletedCourse = false;
			e.printStackTrace();
		}
		
		return deletedCourse;
	}
	
	public boolean addCourse(Course course) {
		boolean addedCourse = true;
		
		try {
			connection.setAutoCommit(false);
			
			PreparedStatement statement1 = connection.prepareStatement("INSERT INTO courses(TITLE, DEPARTMENT_ID, SEMESTER) VALUES(?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			statement1.setString(1, course.getTitle());
			statement1.setInt(2, course.getDepartment().getDepartmentID());
			statement1.setString(3, course.getSemester());
			int insertCount1 = statement1.executeUpdate();
			if (insertCount1 == 1) {
				ResultSet resultSet = statement1.getGeneratedKeys();
				if (resultSet.next()) {
					int courseID = resultSet.getInt(1);
					course.setCourseID(courseID);
					addedCourse = addCourseTeachers(course);
				}
			} else {
				addedCourse = false;
			}
			
			if (addedCourse)
				connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			addedCourse = false;
		} finally {
			try {
				if (!addedCourse)
					connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return addedCourse;
	}
	
	public boolean updateCourse(Course course) {
		boolean updatedCourse = true;
		
		try {
			connection.setAutoCommit(false);
			PreparedStatement statement1 = connection.prepareStatement("UPDATE courses SET TITLE=?, DEPARTMENT_ID=?, SEMESTER=? WHERE COURSE_ID=?;");
			statement1.setString(1, course.getTitle());
			statement1.setInt(2, course.getDepartment().getDepartmentID());
			statement1.setString(3, course.getSemester());
			statement1.setInt(4, course.getCourseID());
			int rowsAffected = statement1.executeUpdate();
			if (rowsAffected == 1) {
				boolean deletedCourseTeachers = deleteCourseTeachers(course.getCourseID());
				if (deletedCourseTeachers) {
					boolean addedCourseTeachers = addCourseTeachers(course);
					if (addedCourseTeachers)
						connection.commit();
					else
						updatedCourse = false;
				} else {
					updatedCourse = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			updatedCourse = false;
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return updatedCourse;
	}
	
	public boolean deleteCourse(int courseID) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM courses WHERE COURSE_ID=?;");
			statement.setInt(1, courseID);
			statement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Course createCourse(ResultSet resultSet) {
		Course course = new Course();
		try {
			int courseID = resultSet.getInt("COURSE_ID");
			course.setCourseID(courseID);
			course.setTitle(resultSet.getString("TITLE"));
			course.setSemester(resultSet.getString("SEMESTER"));
			Department department = new Department();
			department.setDepartmentID(resultSet.getInt("DEPARTMENT_ID"));
			if (resultSetContainsColumn(resultSet, "DEPARTMENT_TITLE"))
				department.setTitle(resultSet.getString("DEPARTMENT_TITLE"));
			course.setDepartment(department);
			course.setRequired(resultSet.getBoolean("REQUIRED"));
			course.setDidacticUnits(resultSet.getInt("DIDACTIC_UNITS"));
			
			PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM course_teachers INNER JOIN employees ON course_teachers.EMPLOYEE_ID=employees.EMPLOYEE_ID WHERE COURSE_ID=?");
			statement2.setInt(1, courseID);
			List<Employee> teachers = new ArrayList<Employee>();
			ResultSet resultSet2 = statement2.executeQuery();
			while (resultSet2.next()) {
				Employee teacher = new Employee();
				teacher.setEmployeeID(resultSet2.getInt("EMPLOYEE_ID"));
				teacher.setFirstName(resultSet2.getString("FIRST_NAME"));
				teacher.setLastName(resultSet2.getString("LAST_NAME"));
				teachers.add(teacher);
			}
			course.setTeachers(teachers);
			
			resultSet2.close();
			statement2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return course;
	}
	
	public Course getCourse(int courseID) {
		Course course = new Course();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT courses.*, departments.TITLE as DEPARTMENT_TITLE FROM courses INNER JOIN departments ON courses.DEPARTMENT_ID=departments.DEPARTMENT_ID WHERE COURSE_ID=?;");
			statement.setInt(1, courseID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				course = createCourse(resultSet);
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return course;
	}
	
	public List<Course> getDepartmentCourses(int departmentID) {
		List<Course> courses = new ArrayList<Course>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses WHERE DEPARTMENT_ID=?;");
			statement.setInt(1, departmentID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Course course = createCourse(resultSet);
				courses.add(course);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
	
	public List<Course> getDepartmentRequiredCourses(int departmentID) {
		List<Course> courses = new ArrayList<Course>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses WHERE REQUIRED=1 AND DEPARTMENT_ID=?;");
			statement.setInt(1, departmentID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				courses.add(createCourse(resultSet));
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
	
	public List<Course> findCourses(String searchTerm) {
		List<Course> courses = new ArrayList<Course>();
		
		try {
			PreparedStatement statement1 = connection.prepareStatement("SELECT COURSE_ID, courses.TITLE as COURSE_TITLE, SEMESTER, courses.DEPARTMENT_ID, departments.TITLE as DEPARTMENT_TITLE FROM courses INNER JOIN departments ON courses.DEPARTMENT_ID=departments.DEPARTMENT_ID WHERE courses.COURSE_ID IN (SELECT c1.COURSE_ID FROM courses as c1 LEFT JOIN departments as d1 ON c1.DEPARTMENT_ID=d1.DEPARTMENT_ID LEFT JOIN course_teachers ON c1.COURSE_ID=course_teachers.COURSE_ID LEFT JOIN employees ON course_teachers.EMPLOYEE_ID=employees.EMPLOYEE_ID WHERE (c1.TITLE LIKE ? OR c1.SEMESTER LIKE ? OR d1.TITLE LIKE ? OR employees.FIRST_NAME LIKE ? OR employees.LAST_NAME LIKE ? OR employees.DESIGNATION LIKE ?));");
			String likeSearchTerm = searchTerm + "%";
			for (int i=1; i<=6; i++)
				statement1.setString(i, likeSearchTerm);
			
			ResultSet resultSet1 = statement1.executeQuery();
			while (resultSet1.next()) {
				Course course = new Course();
				course.setCourseID(resultSet1.getInt("COURSE_ID"));
				course.setTitle(resultSet1.getString("COURSE_TITLE"));
				course.setSemester(resultSet1.getString("SEMESTER"));
				Department department = new Department();
				department.setDepartmentID(resultSet1.getInt("DEPARTMENT_ID"));
				department.setTitle(resultSet1.getString("DEPARTMENT_TITLE"));
				course.setDepartment(department);
				courses.add(course);
			}
			
			resultSet1.close();
			statement1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return courses;
	}
}
