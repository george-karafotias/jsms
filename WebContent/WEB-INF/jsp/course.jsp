<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Add/Edit Course</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Add/Edit Course</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="post" action="CourseController">
			<input type="hidden" name="courseID" value='<c:out value="${course.courseID}" />' />
			<table>
				<tr>
					<td><label for="title">Title *</label>
					<td><input type="text" id="title" name="title" value="<c:out value="${course.title}" />" autofocus autocomplete="off"/></td>
				</tr>
				<tr>
					<td><label for="departmentID">Department *</label></td>
					<td>
						<select name="departmentID" id="departmentID">
							<c:forEach items="${departments}" var="department">
							    <option value="${department.departmentID}" ${department.departmentID == course.department.departmentID ? 'selected' : ''}>${department.title}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="semester">Semester *</label>
					<td>
						<select id="semester" name="semester">
							<option value="A" ${course.semester == 'A' ? 'selected' : ''}>A</option>
							<option value="B" ${course.semester == 'B' ? 'selected' : ''}>B</option>
							<option value="C" ${course.semester == 'C' ? 'selected' : ''}>C</option>
							<option value="D" ${course.semester == 'D' ? 'selected' : ''}>D</option>
							<option value="E" ${course.semester == 'E' ? 'selected' : ''}>E</option>
							<option value="F" ${course.semester == 'F' ? 'selected' : ''}>F</option>
							<option value="G" ${course.semester == 'G' ? 'selected' : ''}>G</option>
							<option value="H" ${course.semester == 'H' ? 'selected' : ''}>H</option>
							<option value="I" ${course.semester == 'I' ? 'selected' : ''}>I</option>
							<option value="J" ${course.semester == 'J' ? 'selected' : ''}>J</option>
							<option value="K" ${course.semester == 'K' ? 'selected' : ''}>K</option>
						</select>
					</td>
				</tr>
				<tr>
					<td valign="top"><label for="teachers">Teachers</label>
					<td>
						<select id="teachers" name="teachers" multiple="multiple" size="3">
							<c:forEach var="teacher" items="${teachers}">
        						<option value="${teacher.employeeID}">${teacher.firstName} ${teacher.lastName}</option>
    						</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Save" /></td>
				</tr>
			</table>
		</form>
	</body>
</html>