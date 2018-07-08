<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Add/Edit Department</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Add/Edit Department</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<c:choose>
			<c:when test="${empty department.semesters}">
				<c:set var="semesters" value="8" />
			</c:when>
			<c:otherwise>
				<c:set var="semesters" value="${department.semesters}" />
			</c:otherwise>
		</c:choose>
		
		<c:choose>
			<c:when test="${empty department.graduationUnits}">
				<c:set var="graduationUnits" value="58" />
			</c:when>
			<c:otherwise>
				<c:set var="graduationUnits" value="${department.graduationUnits}" />
			</c:otherwise>
		</c:choose>
		
		<form method="post" action="DepartmentController">
			<input type="hidden" name="departmentID" value='<c:out value="${department.departmentID}" />' /> 
		
			<table>
				<tr>
					<td><label for="title">Title *</label></td>
					<td><input type="text" id="title" name="title" value='<c:out value="${department.title}" />' /></td>
				</tr>
				<tr>
					<td><label for="title">Semesters *</label></td>
					<td><input type="text" id="semesters" name="semesters" value='<c:out value="${semesters}" />' /></td>
				</tr>
				<tr>
					<td><label for="title">Graduation Units *</label></td>
					<td><input type="text" id="graduationUnits" name="graduationUnits" value='<c:out value="${graduationUnits}" />' /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Save" /></td>
				</tr>
			</table>
		</form>
	</body>
</html>