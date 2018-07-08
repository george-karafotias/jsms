package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.gk.model.Department;

public class DepartmentDAO extends BasicDAO {
	
	public DepartmentDAO() {
		super();
	}
	
	public boolean addDepartment(Department department) {
		boolean addedDepartment = false;
		
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO departments(TITLE, SEMESTERS, GRADUATION_UNITS) VALUES(?, ?, ?);");
			statement.setString(1, department.getTitle());
			statement.setInt(2, department.getSemesters());
			statement.setInt(3, department.getGraduationUnits());
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated == 1)
				addedDepartment = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return addedDepartment;
	}
	
	public boolean updateDepartment(Department department) {
		boolean updatedDepartment = false;
		
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE departments SET TITLE=?, SEMESTERS=?, GRADUATION_UNITS=? WHERE DEPARTMENT_ID=?;");
			statement.setString(1, department.getTitle());
			statement.setInt(2, department.getSemesters());
			statement.setInt(3, department.getGraduationUnits());
			statement.setInt(4, department.getDepartmentID());
			int rowsUpdated = statement.executeUpdate();
			if (rowsUpdated == 1)
				updatedDepartment = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return updatedDepartment;
	}
	
	public Department getDepartment(int departmentID) {
		Department department = new Department();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM departments WHERE DEPARTMENT_ID=?;");
			statement.setInt(1, departmentID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				department = createDepartment(resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return department;
	}
	
	public List<Department> getDepartments() {
		List<Department> departments = new ArrayList<Department>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM departments;");
			while (resultSet.next()) {
				departments.add(createDepartment(resultSet));
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return departments;
	}

	private Department createDepartment(ResultSet resultSet) throws SQLException {
		Department department = new Department();
		department.setDepartmentID(resultSet.getInt("DEPARTMENT_ID"));
		department.setTitle(resultSet.getString("TITLE"));
		department.setSemesters(resultSet.getInt("SEMESTERS"));
		department.setGraduationUnits(resultSet.getInt("GRADUATION_UNITS"));
		return department;
	}
}
