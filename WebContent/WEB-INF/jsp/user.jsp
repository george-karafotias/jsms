<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Add User</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/jquery-3.2.1.min.js"></script>
		<script>
		$(document).ready(function() {
			$("#roleName").change(function(){
				$.get("RolePeopleController", {roleName : $("#roleName").val()}, function(responseJson) {
					var $personSelect = $("#personID");
					$personSelect.find("option").remove();
					$.each(responseJson, function(index, person) { 
						$("<option>").val(person.modelID).text(person.modelTitle).appendTo($personSelect);
					});
				});
			});
		});
		</script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Add User</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="post" action="UsersController">
			<table>
				<tr>
					<td><label for="userName">Username *</label></td>
					<td><input type="text" id="userName" name="userName" value='<c:out value="${user.userName}" />' /></td>
				</tr>
				<tr>
					<td><label for="password1">Password *</label></td>
					<td><input type="password" id="password1" name="password1" /></td>
				</tr>
				<tr>
					<td><label for="password2">Re-enter password *</label></td>
					<td><input type="password" id="password2" name="password2" /></td>
				</tr>
				<tr>
					<td><label for="roleName">Role *</label></td>
					<td>
						<select id="roleName" name="roleName">
							<option value="admin" ${user.userRole == 'admin' ? 'selected' : ''}>admin</option>
							<option value="clerk" ${user.userRole == 'clerk' ? 'selected' : ''}>clerk</option>
							<option value="teacher" ${user.userRole == 'teacher' ? 'selected' : ''}>teacher</option>
							<option value="student" ${user.userRole == 'student' ? 'selected' : ''}>student</option>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="personID">Person </label></td>
					<td>
						<select id="personID" name="personID"></select>
					</td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Add User" /></td>
				</tr>
			</table>
		</form>
	</body>
</html>