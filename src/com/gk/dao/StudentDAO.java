package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.gk.model.Course;
import com.gk.model.Student;

public class StudentDAO extends BasicDAO {

	public StudentDAO() {
		super();
	}
	
	public List<Student> getStudents() {
		List<Student> students = new ArrayList<Student>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");
			while (resultSet.next()) {
				students.add(createStudent(resultSet));
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return students;
	}
	
	public Student getStudent(int studentID) {
		Student student = new Student();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE STUDENT_ID=?;");
			statement.setInt(1, studentID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				student = createStudent(resultSet);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return student;
	}
	
	public List<Student> findStudents(String searchTerm) {
		List<Student> students = new ArrayList<Student>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM students INNER JOIN departments ON students.DEPARTMENT_ID=departments.DEPARTMENT_ID WHERE CODE LIKE ? OR FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR TITLE LIKE ? OR BIRTH_CITY_NAME LIKE ? OR BIRTH_COUNTRY_CODE LIKE ? OR MOBILE_NUMBER LIKE ? OR ADDRESS LIKE ? OR MOTHER_NAME LIKE ? OR FATHER_NAME LIKE ?;");
			String likeSearchTerm = searchTerm + "%";
			for (int i=1; i<=10; i++)
				statement.setString(i, likeSearchTerm);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				students.add(createStudent(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return students;
	}
	
	public boolean addStudent(Student student) {
		boolean insertedStudent = true;
		try {
			connection.setAutoCommit(false);
			
			PreparedStatement statement1 = connection.prepareStatement("INSERT INTO students(CODE, FIRST_NAME, LAST_NAME, GENDER_CODE, ENTRY_DATE, ACTIVE_FLAG, DEPARTMENT_ID, BIRTH_DATE, BIRTH_CITY_NAME, BIRTH_COUNTRY_CODE, MOBILE_NUMBER, ADDRESS, MOTHER_NAME, FATHER_NAME) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
			statement1.setString(1, student.getCode());
			statement1.setString(2, student.getFirstName());
			statement1.setString(3, student.getLastName());
			statement1.setString(4, student.getGenderCode());
			statement1.setDate(5, toSqlDate(student.getEntryDate()));
			statement1.setBoolean(6, student.isActiveFlag());
			statement1.setInt(7, student.getDepartmentID());
			statement1.setDate(8, toSqlDate(student.getBirthDate()));
			statement1.setString(9, student.getBirthCityName());
			statement1.setString(10, student.getBirthCountryCode());
			statement1.setString(11, student.getMobileNumber());
			statement1.setString(12, student.getAddress());
			statement1.setString(13, student.getMotherName());
			statement1.setString(14, student.getFatherName());
			int insertedRows1 = statement1.executeUpdate();
			
			if (insertedRows1 == 1) {
				ResultSet resultSet2 = statement1.getGeneratedKeys();
				if (resultSet2.next()) {
					int studentID = resultSet2.getInt(1);
					
					CourseDAO courseDAO = new CourseDAO();
					List<Course> requiredCourses = courseDAO.getDepartmentRequiredCourses(student.getDepartmentID());
					if (requiredCourses != null) {
						for (Course requiredCourse : requiredCourses) {
							PreparedStatement statement3 = connection.prepareStatement("INSERT INTO course_enrollments(STUDENT_ID, COURSE_ID) VALUES(?, ?);");
							statement3.setInt(1, studentID);
							statement3.setInt(2, requiredCourse.getCourseID());
							int insertedRows3 = statement3.executeUpdate();
							if (insertedRows3 != 1) {
								insertedStudent = false;
								break;
							}
						}
					}
				} else {
					insertedStudent = false;
				}
			}
			
			if (insertedStudent) {
				connection.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			insertedStudent = false;
		} finally {
			try {
				if (!insertedStudent)
					connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return insertedStudent;
	}
	
	public boolean updateStudent(Student student) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE STUDENTS SET CODE=?, FIRST_NAME=?, LAST_NAME=?, GENDER_CODE=?, ENTRY_DATE=?, ACTIVE_FLAG=?, DEPARTMENT_ID=?, BIRTH_DATE=?, BIRTH_CITY_NAME=?, BIRTH_COUNTRY_CODE=?, MOBILE_NUMBER=?, ADDRESS=?, MOTHER_NAME=?, FATHER_NAME=? WHERE STUDENT_ID=?;");
			statement.setString(1, student.getCode());
			statement.setString(2, student.getFirstName());
			statement.setString(3, student.getLastName());
			statement.setString(4, student.getGenderCode());
			statement.setDate(5, toSqlDate(student.getEntryDate()));
			statement.setBoolean(6, student.isActiveFlag());
			statement.setInt(7, student.getDepartmentID());
			statement.setDate(8, toSqlDate(student.getBirthDate()));
			statement.setString(9, student.getBirthCityName());
			statement.setString(10, student.getBirthCountryCode());
			statement.setString(11, student.getMobileNumber());
			statement.setString(12, student.getAddress());
			statement.setString(13, student.getMotherName());
			statement.setString(14, student.getFatherName());
			statement.setInt(15, student.getStudentID());
			statement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public boolean deleteStudent(int studentID) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM students WHERE STUDENT_ID=?;");
			statement.setInt(1, studentID);
			statement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Student createStudent(ResultSet resultSet) throws SQLException {
		Student student = new Student();
		
		student.setStudentID(resultSet.getInt("STUDENT_ID"));
		student.setCode(resultSet.getString("CODE"));
		student.setFirstName(resultSet.getString("FIRST_NAME"));
		student.setLastName(resultSet.getString("LAST_NAME"));
		student.setGenderCode(resultSet.getString("GENDER_CODE"));
		student.setActiveFlag(resultSet.getBoolean("ACTIVE_FLAG"));
		student.setEntryDate(toUtilDate(resultSet.getDate("ENTRY_DATE")));
		student.setDepartmentID(resultSet.getInt("DEPARTMENT_ID"));
		student.setBirthDate(toUtilDate(resultSet.getDate("BIRTH_DATE")));
		student.setBirthCityName(resultSet.getString("BIRTH_CITY_NAME"));
		student.setBirthCountryCode(resultSet.getString("BIRTH_COUNTRY_CODE"));
		student.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
		student.setAddress(resultSet.getString("ADDRESS"));
		student.setFatherName(resultSet.getString("FATHER_NAME"));
		student.setMotherName(resultSet.getString("MOTHER_NAME"));
		if (resultSetContainsColumn(resultSet, "TITLE"))
			student.setDepartmentTitle(resultSet.getString("TITLE"));
		
		return student;
	}
}
