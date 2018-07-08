package com.gk.model;

public class Department {
	
	private int departmentID;
	private String title;
	private int semesters;
	private int graduationUnits;
	
	public int getDepartmentID() {
		return departmentID;
	}
	
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public int getSemesters() {
		return semesters;
	}

	public void setSemesters(int semesters) {
		this.semesters = semesters;
	}

	public int getGraduationUnits() {
		return graduationUnits;
	}

	public void setGraduationUnits(int graduationUnits) {
		this.graduationUnits = graduationUnits;
	}
}
