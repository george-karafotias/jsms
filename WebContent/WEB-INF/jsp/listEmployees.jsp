<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Find Employee</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Find Employee</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="get" action="EmployeeController">
			<input type="text" name="s" value="<c:out value="${param.s}" />" autofocus/>
			<input type="hidden" name="action" value="find" />
			<input type="submit" value="Search" />
		</form>
		
		<c:if test="${not empty employees}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Designation</th>
						<th>Department</th>
						<th colspan="2">Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${employees}" var="employee">
						<tr>
							<td><c:out value="${employee.firstName}" /></td>
							<td><c:out value="${employee.lastName}" /></td>
							<td><c:out value="${employee.designation}" /></td>
							<td><c:out value="${employee.departmentTitle}" /></td>
							<td><a href="EmployeeController?employeeID=<c:out value="${employee.employeeID}"/>">Update</a></td>
							<td><a href="EmployeeController?action=delete&employeeID=<c:out value="${employee.employeeID}"/>">Delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
		</c:if>
		
		<c:if test="${empty employees && not empty param.s}">
			<p>No results</p>
		</c:if>
	</body>
</html>