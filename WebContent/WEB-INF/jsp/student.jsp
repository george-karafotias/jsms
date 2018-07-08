<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Add/Edit Student</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Add/Edit Student</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="post" action="StudentController">
			<input type="hidden" name="studentID" value='<c:out value="${student.studentID}" />' />
			<table>
				<tr>
					<td><label for="code">Code *</label></td>
					<td><input type="text" id="code" name="code" value='<c:out value="${student.code}" />' autofocus autocomplete="off" /></td>
				</tr>
				<tr>
					<td><label for="firstName">First Name *</label></td>
					<td><input type="text" id="firstName" name="firstName" value='<c:out value="${student.firstName}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="lastName">Last Name *</label></td>
					<td><input type="text" id="lastName" name="lastName" value='<c:out value="${student.lastName}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td>Gender *</td>
					<td>
						<label for="male">Male </label><input type="radio" id="male" name="genderCode" value="m" <c:if test="${student.genderCode=='m'}">checked="checked"</c:if> />
						<label for="female">Female </label><input type="radio" id="female" name="genderCode" value="f" <c:if test="${student.genderCode=='f'}">checked="checked"</c:if> />
					</td>
				</tr>
				<tr>
					<td><label for="activeFlag">Active *</label></td>
					<td><input type="checkbox" id="activeFlag" name="activeFlag" <c:if test="${student.activeFlag==true}">checked="checked"</c:if> value="true"/></td>
				</tr>
				<tr>
					<td><label for="departmentID">Department *</label></td>
					<td>
						<select name="departmentID" id="departmentID">
							<c:forEach items="${departments}" var="department">
							    <option value="${department.departmentID}" ${department.departmentID == student.departmentID ? 'selected' : ''}>${department.title}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="entryDate">Entry Date *</label></td>
					<td><input type="text" id="entryDate" name="entryDate" value="<fmt:formatDate value='${student.entryDate}' type='date' pattern='yyyy/MM/dd' />" autocomplete="off" placeholder="yyyy/MM/dd"/></td>
				</tr>
				<tr>
					<td><label for="mobileNumber">Mobile Number</label></td>
					<td><input type="text" id="mobileNumber" name="mobileNumber" value='<c:out value="${student.mobileNumber}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="address">Address</label></td>
					<td><input type="text" id="address" name="address" value='<c:out value="${student.address}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="birthDate">Birth Date</label></td>
					<td><input type="text" id="birthDate" name="birthDate" value="<fmt:formatDate value='${student.birthDate}' type='date' pattern='yyyy/MM/dd' />" autocomplete="off" placeholder="yyyy/MM/dd"/></td>
				</tr>
				<tr>
					<td><label for="birthCityName">Birth City</label></td>
					<td><input type="text" id="birthCityName" name="birthCityName" value='<c:out value="${student.birthCityName}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="birthCountryCode">Birth Country Code</label></td>
					<td><input type="text" id="birthCountryCode" name="birthCountryCode" value='<c:out value="${student.birthCountryCode}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="fatherName">Father Name</label></td>
					<td><input type="text" id="fatherName" name="fatherName" value='<c:out value="${student.fatherName}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="motherName">Mother Name</label></td>
					<td><input type="text" id="motherName" name="motherName" value='<c:out value="${student.motherName}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Save" /></td>
				</tr>
			</table>
		</form>
	</body>
</html>