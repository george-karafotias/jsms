<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="header">
	<h1>Student Management System</h1>
	
	<% 
	request.setAttribute("isAdmin", request.isUserInRole("admin"));
	request.setAttribute("isClerk", request.isUserInRole("clerk"));
	request.setAttribute("isTeacher", request.isUserInRole("teacher"));
	request.setAttribute("isStudent", request.isUserInRole("student"));
	%>
	
	<c:if test="${requestScope.isAdmin || requestScope.isClerk}">
	<div class="menu">
		<a href="dashboard.jsp">Home</a>
		<div class="dropdown">
		  <button class="dropbtn">Students</button>
		  <div class="dropdown-content">
		    <a href="StudentController">Add Student</a>
		    <a href="StudentController?action=find">Find Student</a>
		  </div>
		</div>
		<div class="dropdown">
		  <button class="dropbtn">Courses</button>
		  <div class="dropdown-content">
		    <a href="CourseController">Add Course</a>
		    <a href="CourseController?action=find">Find Course</a>
		  </div>
		</div>
		<div class="dropdown">
		  <button class="dropbtn">Employees</button>
		  <div class="dropdown-content">
		    <a href="EmployeeController">Add Employee</a>
		    <a href="EmployeeController?action=find">Find Employee</a>
		  </div>
		</div>
		<div class="dropdown">
		  <button class="dropbtn">Departments</button>
		  <div class="dropdown-content">
		    <a href="DepartmentController">Add Department</a>
		    <a href="DepartmentController?action=list">Department List</a>
		  </div>
		</div>
		<a href="GradeController">Grades</a>
		<div class="dropdown">
		  <button class="dropbtn">Users</button>
		  <div class="dropdown-content">
		    <a href="UsersController">Add User</a>
		    <a href="UsersController?action=find">Find User</a>
		  </div>
		</div>
		<a href="logout.jsp">Logout</a>
	</div>
	</c:if>

	<c:if test="${requestScope.isTeacher}">
	<div class="menu">
		<a href="dashboard.jsp">Home</a>
		<a href="GradeController">Grades</a>
		<a href="logout.jsp">Logout</a>	
	</div>
	</c:if>
	
	<c:if test="${requestScope.isStudent}">
	<div class="menu">
		<a href="dashboard.jsp">Home</a>
		<a href="CourseEnrollmentController">Course Enrollments</a>
		<a href="StudentController">Grades</a>
		<a href="logout.jsp">Logout</a>	
	</div>
	</c:if>
</div>