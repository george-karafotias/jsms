package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.gk.model.Employee;

public class EmployeeDAO extends BasicDAO {
	
	public EmployeeDAO() {
		super();
	}
	
	public Employee getEmployee(int employeeID) {
		Employee employee = new Employee();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE EMPLOYEE_ID=?;");
			statement.setInt(1, employeeID);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				employee = createEmployee(resultSet);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employee;
	}
	
	private Employee createEmployee(ResultSet resultSet) throws SQLException {
		Employee employee = new Employee();
		
		employee.setEmployeeID(resultSet.getInt("EMPLOYEE_ID"));
		employee.setFirstName(resultSet.getString("FIRST_NAME"));
		employee.setLastName(resultSet.getString("LAST_NAME"));
		employee.setGenderCode(resultSet.getString("GENDER_CODE"));
		employee.setDesignation(resultSet.getString("DESIGNATION"));
		employee.setActiveFlag(resultSet.getBoolean("ACTIVE_FLAG"));
		employee.setDepartmentID(resultSet.getInt("DEPARTMENT_ID"));
		employee.setHireDate(toUtilDate(resultSet.getDate("HIRE_DATE")));
		employee.setBirthDate(toUtilDate(resultSet.getDate("BIRTH_DATE")));
		employee.setOfficePhoneNumber(resultSet.getString("OFFICE_PHONE_NUMBER"));
		employee.setMobileNumber(resultSet.getString("MOBILE_NUMBER"));
		employee.setAddress(resultSet.getString("ADDRESS"));
		if (resultSetContainsColumn(resultSet, "TITLE"))
			employee.setDepartmentTitle(resultSet.getString("TITLE"));
		
		return employee;
	}
	
	public boolean addEmployee(Employee employee) {
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO employees(FIRST_NAME, LAST_NAME, GENDER_CODE, DESIGNATION, ACTIVE_FLAG, DEPARTMENT_ID, HIRE_DATE, BIRTH_DATE, OFFICE_PHONE_NUMBER, MOBILE_NUMBER, ADDRESS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			statement.setString(1, employee.getFirstName());
			statement.setString(2, employee.getLastName());
			statement.setString(3, employee.getGenderCode());
			statement.setString(4, employee.getDesignation());
			statement.setBoolean(5, employee.isActiveFlag());
			statement.setInt(6, employee.getDepartmentID());
			statement.setDate(7, toSqlDate(employee.getHireDate()));
			statement.setDate(8, toSqlDate(employee.getBirthDate()));
			statement.setString(9, employee.getOfficePhoneNumber());
			statement.setString(10, employee.getMobileNumber());
			statement.setString(11, employee.getAddress());
			statement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateEmployee(Employee employee) {
		try {
			PreparedStatement statement = connection.prepareStatement("UPDATE employees SET FIRST_NAME=?, LAST_NAME=?, GENDER_CODE=?, DESIGNATION=?, ACTIVE_FLAG=?, DEPARTMENT_ID=?, HIRE_DATE=?, BIRTH_DATE=?, OFFICE_PHONE_NUMBER=?, MOBILE_NUMBER=?, ADDRESS=? WHERE EMPLOYEE_ID=?;");
			statement.setString(1, employee.getFirstName());
			statement.setString(2, employee.getLastName());
			statement.setString(3, employee.getGenderCode());
			statement.setString(4, employee.getDesignation());
			statement.setBoolean(5, employee.isActiveFlag());
			statement.setInt(6, employee.getDepartmentID());
			statement.setDate(7, toSqlDate(employee.getHireDate()));
			statement.setDate(8, toSqlDate(employee.getBirthDate()));
			statement.setString(9, employee.getOfficePhoneNumber());
			statement.setString(10, employee.getMobileNumber());
			statement.setString(11, employee.getAddress());
			statement.setInt(12, employee.getEmployeeID());
			statement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean deleteEmployee(int employeeID) {
		try {
			PreparedStatement statement = connection.prepareStatement("DELETE FROM employees WHERE EMPLOYEE_ID=?;");
			statement.setInt(1, employeeID);
			statement.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Employee> findEmployees(String searchTerm) {
		List<Employee> employees = new ArrayList<Employee>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees INNER JOIN departments ON employees.DEPARTMENT_ID=departments.DEPARTMENT_ID WHERE FIRST_NAME LIKE ? OR LAST_NAME LIKE ? OR DESIGNATION LIKE ? OR TITLE LIKE ? OR OFFICE_PHONE_NUMBER LIKE ? OR MOBILE_NUMBER LIKE ? OR ADDRESS LIKE ?;");
			String likeSearchTerm = searchTerm + "%";
			for (int i=1; i<=7; i++)
				statement.setString(i, likeSearchTerm);
			
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				employees.add(createEmployee(resultSet));
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}
	
	public List<Employee> getClerks() {
		List<Employee> employees = new ArrayList<Employee>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE designation<>? AND designation<>?;");
			statement.setString(1, Employee.PROFESSOR);
			statement.setString(2, Employee.ASSISTANT_PROFESSOR);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				employees.add(createEmployee(resultSet));
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}
	
	public List<Employee> getTeachers() {
		List<Employee> teachers = new ArrayList<Employee>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM employees WHERE designation=? OR designation=?;");
			statement.setString(1, Employee.PROFESSOR);
			statement.setString(2, Employee.ASSISTANT_PROFESSOR);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Employee teacher = createEmployee(resultSet);
				teachers.add(teacher);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return teachers;
	}
}
