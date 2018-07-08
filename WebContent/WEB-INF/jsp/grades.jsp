<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Calendar" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Grades</title>
		<link rel="stylesheet" type="text/css" href="css/style.css">
		<script src="js/jquery-3.2.1.min.js"></script>
		<script>
		function refreshExamPeriods() {
			$.get("CourseExamPeriodsController", {courseID : $("#courseID").val()}, function(responseJson) {
				var $examPeriodsSelect = $("#examPeriod");
				$examPeriodsSelect.find("option").remove();
				$.each(responseJson, function(index, item) { 
					$("<option>").val(item).text(item).appendTo($examPeriodsSelect);
				});
			});	
		}
		
		function gradeChanged(element) {
			var id = $(element).attr("id");
			var value = $(element).val();
			$.post("SetGradeController", {recordID : id, grade : value}, function(responseText){
				if (responseText.length > 0) {
					$("#grade" + id).text(responseText);				
				}
			});
		}
		
		$(document).ready(function() {
			$("#departmentID").change(function(){
				$.get("DepartmentCoursesController", {departmentID : $("#departmentID").val()}, function(responseJson) {
					var $courseSelect = $("#courseID");
					$courseSelect.find("option").remove();
					$.each(responseJson, function(index, course) { 
						$("<option>").val(course.courseID).text(course.title).appendTo($courseSelect);
					});
				});
			});
			
			$("#courseID").change(function(){
				refreshExamPeriods();
			});
			
			$("#addNewExamPeriod").click(function(){
				$.post("AddNewExamPeriodController", {courseID : $("#courseID").val(), newExamPeriodYear: $("#newExamPeriodYear").val(), newExamPeriodMonth: $("#newExamPeriodMonth").val()}, function(responseText){
					if (responseText.length == 0) {
						refreshExamPeriods();				
					} else {
						$("#addNewExamErrorSpan").text(responseText);
					}
				});
			});
		});
		</script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/jsp/header.jsp"></jsp:include>
		<h2>Grades</h2>
		
		<form method="get" action="GradeController">
			<input type="hidden" name="action" value="find" />
			<table>
				<tr>
					<td><label for="departmentID">Department</label></td>
					<td>
						<select name="departmentID" id="departmentID">
							<c:forEach items="${departments}" var="department">
							    <option value="${department.departmentID}" ${department.departmentID == param.departmentID ? 'selected' : ''}>${department.title}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="courseID">Course</label></td>
					<td>
						<select name="courseID" id="courseID">
							<c:forEach items="${courses}" var="course">
							    <option value="${course.courseID}" ${course.courseID == param.courseID ? 'selected' : ''}>${course.title}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label for="examPeriod">Exam Period</label></td>
					<td>
						<select name="examPeriod" id="examPeriod">
							<c:forEach items="${examPeriods}" var="examPeriod">
								<c:set var="selectedExamPeriod"><fmt:formatDate value='${examPeriod}' type='date' pattern='yyyy/MM' /></c:set>
								<option value="${selectedExamPeriod}" ${selectedExamPeriod == param.examPeriod ? 'selected' : ''}><c:out value="${selectedExamPeriod}" /></option>
							</c:forEach>
						</select>
					</td>		
				</tr>
				<tr>
					<td><label>New Exam Period</label></td>
					<td>
						<select id="newExamPeriodYear" style="width:100px;">
						<%
							int currentYear = Calendar.getInstance().get(Calendar.YEAR);
							for (int i=currentYear; i>=currentYear-1; i--) {
								out.println("<option value=\"" + i + "\">" + i +  "</option>");
							}
						%>
						</select>
						<select id="newExamPeriodMonth" style="width:100px;">
							<option value="1">JAN</option>
							<option value="2">FEB</option>
							<option value="3">MAR</option>
							<option value="4">APR</option>
							<option value="5">MAY</option>
							<option value="6">JUN</option>
							<option value="7">JUL</option>
							<option value="8">AUG</option>
							<option value="9">SEP</option>
							<option value="10">OCT</option>
							<option value="11">NOV</option>
							<option value="12">DEC</option>
						</select>
						<input type="button" id="addNewExamPeriod" value="Add" />
						<span id="addNewExamErrorSpan"></span>
					</td>
				</tr>
			</table>
			<input type="submit" value="Go" />
		</form>
		
		<c:if test="${not empty grades}">
		
			<table class="searchResults">
				<thead>
					<tr>
						<th>Code</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Grade</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${grades}" var="grade">
						<tr>
							<td><c:out value="${grade.student.code}" /></td>
							<td><c:out value="${grade.student.firstName}" /></td>
							<td><c:out value="${grade.student.lastName}" /></td>
							<td><input type="number" min="0" max="10" id="${grade.recordID}" value="${grade.grade}" onchange="gradeChanged(this)"/><span id="grade${grade.recordID}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
		</c:if>
		
		<c:if test="${empty grades && not empty param.courseID && not empty param.examPeriod}">
			<p>No results</p>
		</c:if>
	</body>
</html>