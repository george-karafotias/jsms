package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gk.model.Course;
import com.gk.model.Grade;
import com.gk.model.Student;

public class GradeDAO extends BasicDAO {

	public GradeDAO() {
		super();
	}
	
	public boolean addNewExamPeriod(int courseID, java.util.Date examPeriod) {
		boolean addedNewExamPeriod = true;
		
		try {
			connection.setAutoCommit(false);
			
			java.sql.Date sqlExamPeriod = toSqlDate(examPeriod);
			
			PreparedStatement statement1 = connection.prepareStatement("DELETE FROM grades WHERE COURSE_ID=? AND GRADE IS NULL;");
			statement1.setInt(1, courseID);
			statement1.executeUpdate();
			statement1.close();
			
			PreparedStatement statement2 = connection.prepareStatement("SELECT STUDENT_ID FROM course_enrollments WHERE COURSE_ID=? AND NOT EXISTS(SELECT * FROM grades WHERE grades.STUDENT_ID=course_enrollments.STUDENT_ID AND grades.COURSE_ID=? AND grade>=5);");
			statement2.setInt(1, courseID);
			statement2.setInt(2, courseID);
			ResultSet resultSet2 = statement2.executeQuery();
			List<Integer> studentIDs = new ArrayList<Integer>();
			while (resultSet2.next()) {
				studentIDs.add(resultSet2.getInt("STUDENT_ID"));
			}
			resultSet2.close();
			statement2.close();
			
			for (int i=0; i<studentIDs.size(); i++) {
				PreparedStatement statement = connection.prepareStatement("INSERT INTO grades(STUDENT_ID, COURSE_ID, EXAM_PERIOD) VALUES(?, ?, ?);");
				statement.setInt(1, studentIDs.get(i));
				statement.setInt(2, courseID);
				statement.setDate(3, sqlExamPeriod);
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected != 1) {
					addedNewExamPeriod = false;
					break;
				}
			}
		} catch (SQLException e) {
			addedNewExamPeriod = false;
			e.printStackTrace();
		} finally {
			try {
				if (!addedNewExamPeriod)
					connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return addedNewExamPeriod;
	}
	
	public List<java.util.Date> getCourseExamPeriods(int courseID) {
		List<java.util.Date> examPeriods = new ArrayList<java.util.Date>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT EXAM_PERIOD FROM grades WHERE COURSE_ID=? AND EXAM_PERIOD IS NOT NULL;");
			statement.setInt(1, courseID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				examPeriods.add(toUtilDate(resultSet.getDate("EXAM_PERIOD")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return examPeriods;
	}
	
	public boolean CourseExamPeriodExists(int courseID, java.util.Date examPeriod) {
		boolean examPeriodExists = false;
		
		try {
			java.sql.Date examPeriodSql = toSqlDate(examPeriod);
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM grades WHERE COURSE_ID=? AND EXAM_PERIOD=?;");
			statement.setInt(1, courseID);
			statement.setDate(2, examPeriodSql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				examPeriodExists = true;
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return examPeriodExists;
	}
	
	public List<Grade> getGrades(int courseID, java.util.Date examPeriod) {
		List<Grade> grades = new ArrayList<Grade>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT grades.*, students.* FROM grades INNER JOIN students ON grades.STUDENT_ID=students.STUDENT_ID WHERE COURSE_ID=? AND EXAM_PERIOD=?;");
			statement.setInt(1, courseID);
			statement.setDate(2, toSqlDate(examPeriod));
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Grade grade = new Grade();
				grade.setRecordID(resultSet.getInt("RECORD_ID"));
				grade.setExamPeriod(toUtilDate(resultSet.getDate("EXAM_PERIOD")));
				grade.setGrade(resultSet.getDouble("GRADE"));
				Course course = new Course();
				course.setCourseID(resultSet.getInt("COURSE_ID"));
				grade.setCourse(course);
				Student student = new Student();
				student.setStudentID(resultSet.getInt("STUDENT_ID"));
				student.setCode(resultSet.getString("CODE"));
				student.setFirstName(resultSet.getString("FIRST_NAME"));
				student.setLastName(resultSet.getString("LAST_NAME"));
				grade.setStudent(student);
				
				grades.add(grade);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return grades;
	}
	
	public boolean setGrade(int recordID, double grade) {
		boolean updatedGrade = false;
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE grades SET GRADE=? WHERE RECORD_ID=?;");
			statement.setDouble(1, grade);
			statement.setInt(2, recordID);
			int updatedRows = statement.executeUpdate();
			if (updatedRows == 1)
				updatedGrade = true;
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return updatedGrade;
	}
	
	public List<Grade> getGrades(int studentID) {
		List<Grade> grades = new ArrayList<Grade>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT grades.RECORD_ID, grades.EXAM_PERIOD, grades.GRADE, students.*, courses.*, departments.TITLE as DEPARTMENT_TITLE, departments.SEMESTERS as DEPARTMENT_SEMESTERS, departments.GRADUATION_UNITS FROM grades INNER JOIN students ON grades.STUDENT_ID=students.STUDENT_ID INNER JOIN courses ON grades.COURSE_ID=courses.COURSE_ID INNER JOIN departments ON students.DEPARTMENT_ID=departments.DEPARTMENT_ID WHERE grades.STUDENT_ID=?;");
			statement.setInt(1, studentID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Grade grade = new Grade();
				grade.setRecordID(resultSet.getInt("RECORD_ID"));
				grade.setExamPeriod(toUtilDate(resultSet.getDate("EXAM_PERIOD")));
				grade.setGrade(resultSet.getDouble("GRADE"));
				Course course = new Course();
				course.setCourseID(resultSet.getInt("COURSE_ID"));
				course.setTitle(resultSet.getString("TITLE"));
				course.setSemester(resultSet.getString("SEMESTER"));
				course.setDidacticUnits(resultSet.getInt("DIDACTIC_UNITS"));
				grade.setCourse(course);
				Student student = new Student();
				student.setStudentID(resultSet.getInt("STUDENT_ID"));
				student.setFirstName(resultSet.getString("FIRST_NAME"));
				student.setLastName(resultSet.getString("LAST_NAME"));
				student.setCode(resultSet.getString("CODE"));
				student.setEntryDate(toUtilDate(resultSet.getDate("ENTRY_DATE")));
				student.setDepartmentID(resultSet.getInt("DEPARTMENT_ID"));
				student.setDepartmentTitle(resultSet.getString("DEPARTMENT_TITLE"));
				grade.setStudent(student);
				
				grades.add(grade);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return grades;
	}
}
