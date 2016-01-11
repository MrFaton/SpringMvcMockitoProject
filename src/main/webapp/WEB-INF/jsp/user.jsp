<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello ${personLogin}</title>
</head>
<body style="text-align: center;">
	<h1>Hello, ${personLogin}!</h1>
	<br>
	<p>
		Click <a href="<c:url value="j_spring_security_logout"/>">here</a> to
		logout
	</p>
</body>
</html>