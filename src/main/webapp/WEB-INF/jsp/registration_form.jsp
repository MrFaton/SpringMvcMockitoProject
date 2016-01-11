<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="captcha" uri="/WEB-INF/tld/captcha.tld"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
<style>
.error {
	color: red;
}
</style>
</head>
<body>
	<h2 align="center">
		Hi new user, here you can register yourself or you can try to <a
			href="<c:url value="/login"/>">login</a>
	</h2>
	<br>
	<form:form modelAttribute="userForm" method="POST" enctype="utf8">
		<table>
			<tr>
				<td><label>Login</label></td>
				<td><form:input path="login" value="" /></td>
				<td><form:errors path="login" cssClass="error" element="div" /></td>
			</tr>
			<tr>
				<td><label>Password </label></td>
				<td><form:input path="password" value="" type="password" /></td>
				<td><form:errors path="password" cssClass="error" element="div" /></td>
			</tr>
			<tr>
				<td><label>Confirm password </label></td>
				<td><form:input path="matchingPassword" value=""
						type="password" /></td>
				<td><form:errors element="div" cssClass="error" /></td>
			</tr>
			<tr>
				<td><label>First name </label></td>
				<td><form:input path="firstName" value="" /></td>
				<td><form:errors path="firstName" cssClass="error"
						element="div" /></td>
			</tr>
			<tr>
				<td><label>Last name </label></td>
				<td><form:input path="lastName" value="" /></td>
				<td><form:errors path="lastName" cssClass="error" element="div" /></td>
			</tr>
			<tr>
				<td><label>Email </label></td>
				<td><form:input path="email" value="" /></td>
				<td><form:errors path="email" cssClass="error" element="div" /></td>
			</tr>
			<tr>
				<td><label>Birthday</label></td>
				<td><form:input path="birthDay" placeholder="25-12-1990" /></td>
				<td><form:errors path="birthDay" cssClass="error" element="div" /></td>
			</tr>
			<tr>
				<td colspan='3'><captcha:captcha themeName="clean"
						publickey="6LeD4BQTAAAAAPJmHF5lKV-Se_-tJ9nvRSrYZfhk"
						privatekey="6LeD4BQTAAAAAPRpVTFZbmv17K_YqjVtRig6cwme" /></td>
				<td class="error">${invalidCaptcha}</td>
			</tr>
			<tr>
				<td>
					<button type="submit">Submit</button>
					<button type="reset">Cancel</button>
				</td>
			</tr>
		</table>
	</form:form>
</body>
</html>