<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Add/Edit Employee</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Add/Edit Employee</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="post" action="EmployeeController">
			<input type="hidden" name="employeeID" value='<c:out value="${employee.employeeID}" />' /> 
		
			<table>
				<tr>
					<td><label for="firstName">First Name *</label></td>
					<td><input type="text" id="firstName" name="firstName" value='<c:out value="${employee.firstName}" />' autocomplete="off" autofocus/></td>
				</tr>
				<tr>
					<td><label for="lastName">Last Name *</label></td>
					<td><input type="text" id="lastName" name="lastName" value='<c:out value="${employee.lastName}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td>Gender *</td>
					<td>
						<label for="male">Male </label><input type="radio" id="male" name="genderCode" value="m" <c:if test="${employee.genderCode=='m'}">checked="checked"</c:if> />
						<label for="female">Female </label><input type="radio" id="female" name="genderCode" value="f" <c:if test="${employee.genderCode=='f'}">checked="checked"</c:if> />
					</td>
				</tr>
				<tr>
					<td><label for="activeFlag">Active *</label></td>
					<td><input type="checkbox" id="activeFlag" name="activeFlag" <c:if test="${employee.activeFlag==true}">checked="checked"</c:if> value="true"/></td>
				</tr>
				<tr>
					<td><label for="designation">Designation *</label></td>
					<td>
						<select id="designation" name="designation">
							<option value="Professor" ${employee.designation == 'Professor' ? 'selected' : ''}>Professor</option>
							<option value="Assistant Professor" ${employee.designation == 'Assistant Professor' ? 'selected' : ''}>Assistant Professor</option>
							<option value="Clerk" ${employee.designation == 'Clerk' ? 'selected' : ''}>Clerk</option>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="departmentID">Department *</label></td>
					<td>
						<select name="departmentID" id="departmentID">
							<c:forEach items="${departments}" var="department">
							    <option value="${department.departmentID}" ${department.departmentID == employee.departmentID ? 'selected' : ''}>${department.title}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="birthDate">Birth Date</label></td>
					<td><input type="text" id="birthDate" name="birthDate" value="<fmt:formatDate value='${employee.birthDate}' type='date' pattern='yyyy/MM/dd' />" autocomplete="off" placeholder="yyyy/MM/dd"/></td>
				</tr>
				<tr>
					<td><label for="hireDate">Hire Date</label></td>
					<td><input type="text" id="hireDate" name="hireDate" value="<fmt:formatDate value='${employee.hireDate}' type='date' pattern='yyyy/MM/dd' />" autocomplete="off" placeholder="yyyy/MM/dd"/></td>
				</tr>
				<tr>
					<td><label for="officePhoneNumber">Office Phone Number</label></td>
					<td><input type="text" id="officePhoneNumber" name="officePhoneNumber" value='<c:out value="${employee.officePhoneNumber}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="mobileNumber">Mobile Number</label></td>
					<td><input type="text" id="mobileNumber" name="mobileNumber" value='<c:out value="${employee.mobileNumber}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="address">Address</label></td>
					<td><input type="text" id="address" name="address" value='<c:out value="${employee.address}" />' autocomplete="off"/></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Save" /></td>
				</tr>
			</table>
		</form>
	</body>
</html>