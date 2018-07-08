package com.gk.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.gk.model.DisplayableModel;
import com.gk.model.Employee;
import com.gk.model.Student;
import com.gk.model.User;

public class UsersDAO extends BasicDAO {

	public static String adminRole = "admin";
	public static String clerkRole = "clerk";
	public static String teacherRole = "teacher";
	public static String studentRole = "student";
	
	public UsersDAO() {
		super();
	}
	
	public User getUser(String userName) {
		User user = new User();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT users.USER_NAME, users.PERSON_ID, ROLE_NAME FROM users INNER JOIN user_roles ON users.USER_NAME=user_roles.USER_NAME WHERE users.USER_NAME=?;");
			statement.setString(1, userName);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user.setUserName(resultSet.getString("USER_NAME"));
				user.setUserRole(resultSet.getString("ROLE_NAME"));
				user.setPersonID(resultSet.getInt("PERSON_ID"));
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public String md5(String md5) {
	   try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    
	    }
	    return null;
	}
	
	public boolean addUser(User user) {
		boolean addedUser = false;
		
		try {
			connection.setAutoCommit(false);
			
			PreparedStatement statement1 = connection.prepareStatement("INSERT INTO users(USER_NAME, USER_PASS, PERSON_ID) VALUES(?,?,?);");
			statement1.setString(1, user.getUserName());
			statement1.setString(2, md5(user.getUserPass()));
			statement1.setInt(3, user.getPersonID());
			int usersInserted1 = statement1.executeUpdate();
			if (usersInserted1 == 1) {
				PreparedStatement statement2 = connection.prepareStatement("INSERT INTO user_roles(USER_NAME, ROLE_NAME) VALUES(?,?);");
				statement2.setString(1, user.getUserName());
				statement2.setString(2, user.getUserRole());
				int usersInserted2 = statement2.executeUpdate();
				if (usersInserted2 == 1)
					addedUser = true;
				
				statement2.close();
			}
			
			statement1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!addedUser)
					connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return addedUser;
	}
	
	public boolean deleteUser(String userName) {
		boolean deletedUser = false;
		
		try {
			connection.setAutoCommit(false);
			PreparedStatement statement1 = connection.prepareStatement("DELETE FROM users WHERE USER_NAME=?;");
			statement1.setString(1, userName);
			int deletedUser1 = statement1.executeUpdate();
			
			if (deletedUser1 == 1) {
				PreparedStatement statement2 = connection.prepareStatement("DELETE FROM user_roles WHERE USER_NAME=?;");
				statement2.setString(1, userName);
				int deletedUser2 = statement2.executeUpdate();
				if (deletedUser2 == 1)
					deletedUser = true;
				
				statement2.close();
			}
			
			statement1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (!deletedUser)
					connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return deletedUser;
	}
	
	public List<DisplayableModel> getUserRolePeopleIDs(String roleName) {
		List<DisplayableModel> dms = new ArrayList<DisplayableModel>();
		
		if (roleName != null) {
			if (roleName.equals(studentRole)) {
				List<Student> students = new StudentDAO().getStudents();
				for (Student student : students) {
					DisplayableModel dm = new DisplayableModel();
					dm.setModelID(student.getStudentID());
					dm.setModelTitle(student.getFirstName() + " " + student.getLastName());
					dms.add(dm);
				}
			} else if (roleName.equals(clerkRole)) {
				List<Employee> clerks = new EmployeeDAO().getClerks();
				for (Employee clerk : clerks) {
					DisplayableModel dm = new DisplayableModel();
					dm.setModelID(clerk.getEmployeeID());
					dm.setModelTitle(clerk.getFirstName() + " " + clerk.getLastName());
					dms.add(dm);
				}
			} else if (roleName.equals(teacherRole)) {
				List<Employee> teachers = new EmployeeDAO().getTeachers();
				for (Employee teacher : teachers) {
					DisplayableModel dm = new DisplayableModel();
					dm.setModelID(teacher.getEmployeeID());
					dm.setModelTitle(teacher.getFirstName() + " " + teacher.getLastName());
					dms.add(dm);
				}
			}
		}
		
		return dms;
	}
	
 	public List<User> findUsers(String searchTerm) {
		List<User> users = new ArrayList<User>();
		
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT users.USER_NAME, user_roles.ROLE_NAME FROM users INNER JOIN user_roles ON users.USER_NAME=user_roles.USER_NAME WHERE users.USER_NAME LIKE ? OR ROLE_NAME LIKE ?;");
			String likeSearchTerm = searchTerm + "%";
			statement.setString(1, likeSearchTerm);
			statement.setString(2, likeSearchTerm);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				user.setUserName(resultSet.getString("USER_NAME"));
				user.setUserRole(resultSet.getString("ROLE_NAME"));
				users.add(user);
			}
			
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
}
