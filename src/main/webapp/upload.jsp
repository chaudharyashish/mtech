<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>

<h1>Spring Boot file upload example</h1>

<form method="POST" action="upload" enctype="multipart/form-data">
    <input type="file" name="file" /><br/><br/>
    <input type="text" name="email" />
    <input type="submit" value="Submit" />
</form>

<br/>
<br/>

<c:if test="${message}">
<div>
    <h2>
    	${message}
    </h2>
</div>
</c:if>
</body>
</html>