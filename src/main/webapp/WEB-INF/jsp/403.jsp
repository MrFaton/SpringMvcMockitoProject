<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hello ${personLogin}</title>
</head>
<body>
    <h1>HTTP Status 403 - Access is denied</h1>
    <h2>You do not have permission to access this page!</h2>
    <h3>Go to <a href="<c:url value="/"/>">main</a> page</h3>
</body>
</html>