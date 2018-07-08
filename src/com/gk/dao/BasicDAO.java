package com.gk.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.gk.util.DBUtil;

public class BasicDAO {
	
	protected Connection connection;
	
	public BasicDAO() {
		connection = DBUtil.getConnection();
	}
	
	protected static java.sql.Date toSqlDate(java.util.Date date) {
		if (date == null) return null;
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	protected static java.util.Date toUtilDate(java.sql.Date date) {
		if (date == null) return null;
		java.util.Date utilDate = new java.sql.Date(date.getTime());
		return utilDate;
	}
	
	protected static boolean resultSetContainsColumn(ResultSet resultSet, String columnName) {
		ResultSetMetaData resultSetMetaData;
		try {
			resultSetMetaData = resultSet.getMetaData();
			int numberOfColumns = resultSetMetaData.getColumnCount();
			
			for (int i = 1; i < numberOfColumns + 1; i++) {
			    String currentColumnName = resultSetMetaData.getColumnName(i);
			    if (columnName.equals(currentColumnName)) {
			        return true;
			    }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
