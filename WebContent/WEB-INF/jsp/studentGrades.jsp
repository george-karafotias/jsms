<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Grades</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Grades</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<p>
			<div><b>Name: </b>${student.firstName} ${student.lastName}</div>
			<div><b>Code: </b>${student.code}</div>
			<div><b>Entry Date: </b><fmt:formatDate value='${student.entryDate}' type='date' pattern='yyyy/MM/dd' /></div>
			<div><b>Department: </b>${departmentTitle}</div>
			<div><b>GBP: </b>${gbp}</div>
		</p>
		
		<c:if test="${not empty grades}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>Course</th>
						<th>Semester</th>
						<th>Grade</th>
						<th>Didactic Units</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${grades}" var="grade">
						<tr>
							<td><c:out value="${grade.course.title}" /></td>
							<td><c:out value="${grade.course.semester}" /></td>
							<td><c:out value="${grade.grade}" /></td>
							<td><c:out value="${grade.course.didacticUnits}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
		</c:if>
		
		<c:if test="${empty grades}">
			<p>No results</p>
		</c:if>
	</body>
</html>