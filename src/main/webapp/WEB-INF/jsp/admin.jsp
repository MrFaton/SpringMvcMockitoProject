<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="mytag" uri="/WEB-INF/tld/allUsers.tld"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
form, form div {
	display: inline;
	padding: 2px;
}

table, th, td {
	border-collapse: collapse;
	border: 2px solid black;
}
</style>
<title>Hello ${personLogin}</title>
</head>
<body>
	<p align="right">
		Admin ${personLogin} (<a
			href="<c:url value="j_spring_security_logout"/>">Logout</a>)
	</p>
	<p>
		<a href="<c:url value="/admin/create"/>">Add new user</a>
	</p>
	<div align="center">
		<mytag:all_users_table userList="${userList}" />
	</div>
</body>
</html>