<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Sms - Login</title>
		<style>
		input[type="submit"].loginButton {
		background-color:#4CAF50;
	    border:none;
	    color:white;
	    padding:10px;
	    min-width:100px;
	    text-align:center;
	    text-decoration:none;
	    display:inline-block;
	    margin-top:10px;
		}
		</style>
	</head>
	<body>
		<h2>Login to Student Management System</h2>
		<form action="j_security_check" method="post">
			<table>
				<tr>
					<td><label for="username">Username: </label></td>
					<td><input type="text" id="username" name="j_username" autofocus style="width:150px"/></td>
				</tr>
				<tr>
					<td><label for="password">Password: </label></td>
					<td><input type="password" id="password" name="j_password" style="width:150px" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="Go" class="loginButton" /></td>
				</tr>
			</table>
		</form>
	</body>
</html>