package com.gk.model;

import java.util.Date;

public class Student {
	
	private int studentID;
	private String code;
	private String firstName;
	private String lastName;
	private String genderCode;
	private Date entryDate;
	private boolean activeFlag;
	private int departmentID;
	private String departmentTitle;
	private Date birthDate;
	private String birthCityName;
	private String birthCountryCode;
	private String mobileNumber;
	private String address;
	private String motherName;
	private String fatherName;
	
	public int getStudentID() {
		return studentID;
	}
	
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getGenderCode() {
		return genderCode;
	}
	
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	
	public Date getEntryDate() {
		return entryDate;
	}
	
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	public boolean isActiveFlag() {
		return activeFlag;
	}
	
	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	public int getDepartmentID() {
		return departmentID;
	}
	
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getBirthCityName() {
		return birthCityName;
	}
	
	public void setBirthCityName(String birthCityName) {
		this.birthCityName = birthCityName;
	}
	
	public String getBirthCountryCode() {
		return birthCountryCode;
	}
	
	public void setBirthCountryCode(String birthCountryCode) {
		this.birthCountryCode = birthCountryCode;
	}
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getMotherName() {
		return motherName;
	}
	
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	
	public String getFatherName() {
		return fatherName;
	}
	
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getDepartmentTitle() {
		return departmentTitle;
	}

	public void setDepartmentTitle(String departmentTitle) {
		this.departmentTitle = departmentTitle;
	}
}
