<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Dashboard</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		
		<% 
		request.setAttribute("isAdmin", request.isUserInRole("admin"));
		request.setAttribute("isClerk", request.isUserInRole("clerk"));
		request.setAttribute("isTeacher", request.isUserInRole("teacher"));
		request.setAttribute("isStudent", request.isUserInRole("student"));
		%>
		
		<h2>Dashboard</h2>
		
		<c:if test="${requestScope.isAdmin || requestScope.isClerk}">
		<jsp:include page="/WEB-INF/jsp/adminDashboard.jsp"></jsp:include>
		</c:if>
		
		<c:if test="${requestScope.isTeacher}">
		<jsp:include page="/WEB-INF/jsp/teacherDashboard.jsp"></jsp:include>
		</c:if>
		
		<c:if test="${requestScope.isStudent}">
		<jsp:include page="/WEB-INF/jsp/studentDashboard.jsp"></jsp:include>
		</c:if>
	</body>
</html>