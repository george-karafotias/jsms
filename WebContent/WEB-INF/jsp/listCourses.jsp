<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Find Course</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Find Course</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="get" action="CourseController">
			<input type="text" name="s" value="<c:out value="${param.s}" />" autofocus/>
			<input type="hidden" name="action" value="find" />
			<input type="submit" value="Search" />
		</form>
		
		<c:if test="${not empty courses}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>Title</th>
						<th>Department</th>
						<th>Semester</th>
						<th colspan="2">Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${courses}" var="course">
						<tr>
							<td><c:out value="${course.title}" /></td>
							<td><c:out value="${course.department.title}" /></td>
							<td><c:out value="${course.semester}" /></td>
							<td><a href="CourseController?courseID=<c:out value="${course.courseID}"/>">Update</a></td>
							<td><a href="CourseController?action=delete&courseID=<c:out value="${course.courseID}"/>">Delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
		</c:if>
		
		<c:if test="${empty courses && not empty param.s}">
			<p>No results</p>
		</c:if>
	</body>
</html>