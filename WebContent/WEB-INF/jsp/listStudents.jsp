<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Find Student</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Find Student</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="get" action="StudentController">
			<input type="text" name="s" value="<c:out value="${param.s}" />" autofocus/>
			<input type="hidden" name="action" value="find" />
			<input type="submit" value="Search" />
		</form>
		
		<c:if test="${not empty students}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>Code</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Department</th>
						<th colspan="3">Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${students}" var="student">
						<tr>
							<td><c:out value="${student.code}" /></td>
							<td><c:out value="${student.firstName}" /></td>
							<td><c:out value="${student.lastName}" /></td>
							<td><c:out value="${student.departmentTitle}" /></td>
							<td><a href="StudentController?studentID=<c:out value="${student.studentID}"/>">Update</a></td>
							<td><a href="CourseEnrollmentController?studentID=<c:out value="${student.studentID}"/>">Enroll</a></td>
							<td><a href="StudentController?action=delete&studentID=<c:out value="${student.studentID}"/>">Delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
		</c:if>
		
		<c:if test="${empty students && not empty param.s}">
			<p>No results</p>
		</c:if>
	</body>
</html>