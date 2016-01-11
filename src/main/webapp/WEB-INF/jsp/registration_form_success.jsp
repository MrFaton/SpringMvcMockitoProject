<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration successful</title>
</head>
<body style="text-align: center;">
	<h3>
		Congratulations ${userLogin}! You are successfully registered. Now you
		can <a href="<c:url value="/login"/>">login.</a>
	</h3>
</body>
</html>