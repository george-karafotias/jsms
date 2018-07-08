package com.gk.model;

public class Grade {
	
	private int recordID;
	private Student student;
	private Course course;
	private java.util.Date examPeriod;
	private double grade;
	
	public int getRecordID() {
		return recordID;
	}
	
	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public java.util.Date getExamPeriod() {
		return examPeriod;
	}
	
	public void setExamPeriod(java.util.Date examPeriod) {
		this.examPeriod = examPeriod;
	}
	
	public double getGrade() {
		return grade;
	}
	
	public void setGrade(double grade) {
		this.grade = grade;
	}
}
