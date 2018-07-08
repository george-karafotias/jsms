<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Departments</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Departments</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<c:if test="${not empty departments}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>Title</th>
						<th>Semesters</th>
						<th>Graduation Units</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${departments}" var="department">
						<tr>
							<td><c:out value="${department.title}" /></td>
							<td><c:out value="${department.semesters}" /></td>
							<td><c:out value="${department.graduationUnits}" /></td>
							<td><a href="DepartmentController?departmentID=<c:out value="${department.departmentID}"/>">Update</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
		</c:if>
		
		<c:if test="${empty departments}">
			<p>No results</p>
		</c:if>
	</body>
</html>