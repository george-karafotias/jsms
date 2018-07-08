<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Find User</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Find User</h2>
		
		<c:if test="${not empty errors}">
	        <c:forEach items="${errors}" var="error">
	            ${error}<br/>
	        </c:forEach>
	        <br/>
		</c:if>
		
		<form method="get" action="UsersController">
			<input type="text" name="s" value="<c:out value="${param.s}" />" autofocus/>
			<input type="hidden" name="action" value="find" />
			<input type="submit" value="Search" />
		</form>
		
		<c:if test="${not empty users}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>Username</th>
						<th>User Role</th>
						<th>Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${users}" var="user">
						<tr>
							<td><c:out value="${user.userName}" /></td>
							<td><c:out value="${user.userRole}" /></td>
							<td><a href="UsersController?action=delete&userName=<c:out value="${user.userName}"/>">Delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
	
		</c:if>
		
		<c:if test="${empty users && not empty param.s}">
			<p>No results</p>
		</c:if>
	</body>
</html>