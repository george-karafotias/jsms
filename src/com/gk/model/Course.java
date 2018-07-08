package com.gk.model;

import java.util.List;

public class Course {
	
	private int courseID;
	private String title;
	private Department department;
	private String semester;
	private boolean required;
	private int didacticUnits;
	private List<Employee> teachers;
	
	public int getCourseID() {
		return courseID;
	}
	
	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSemester() {
		return semester;
	}
	
	public void setSemester(String semester) {
		this.semester = semester;
	}
	
	public List<Employee> getTeachers() {
		return teachers;
	}
	
	public void setTeachers(List<Employee> teachers) {
		this.teachers = teachers;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getDidacticUnits() {
		return didacticUnits;
	}

	public void setDidacticUnits(int didacticUnits) {
		this.didacticUnits = didacticUnits;
	}
}
