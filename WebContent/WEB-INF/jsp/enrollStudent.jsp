<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Enroll Student</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Enroll Student</h2>
		
		<p>
			<div><b>Student: </b>${student.firstName} ${student.lastName}</div>
			<div><b>Department: </b>${department.title}</div>
		</p>
		
		<c:if test="${not empty error}">
			<c:out value="${error}" />
		</c:if>
		
		<c:if test="${not empty courses}">
		
		<form action="CourseEnrollmentController" method="post">
			<table class="searchResults">
				<thead>
					<tr>
						<th>Course</th>
						<th>Semester</th>
						<th>Didactic Units</th>
						<th>Select</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${courses}" var="course" varStatus="loop">
						<tr>
							<td><c:out value="${course.title}" /></td>
							<td><c:out value="${course.semester}" /></td>
							<td><c:out value="${course.didacticUnits}" /></td>
							<td><input type="checkbox" name="selected${course.courseID}" <c:if test="${isCourseSelected[loop.index]}">checked="checked"</c:if> /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="hidden" name="studentID" value="${student.studentID}" />
			<input type="submit" value="Enroll" />
		</form>
		
		</c:if>
		
		<c:if test="${empty courses}">
		<p>No courses available for selection</p>
		</c:if>
	</body>
</html>