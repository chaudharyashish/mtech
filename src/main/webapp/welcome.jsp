<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create an account</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
		<!-- ${pageContext.request.userPrincipal} -->
        <h2>Welcome <sec:authentication property="principal.username" /> | <a href="http://localhost:8080/sso/logout?token=<%= session.getAttribute("token") %>">Logout</a><!-- <a onclick="document.forms['logoutForm'].submit()">Logout</a> --></h2>
		<br/>
		<h3>Below is the list of modules you are authorized for:</h3><br/>
		<hr/>
		<table style="border-color: black; border-width: 2px; border-style: dashed; width: 100%; text-align: center; height: 5em;">
		<tr>
		
		
		
		<c:forEach items="${user.modules}" var="module">
		
			<c:if test="${module.name eq 'timesheet'}">
				<c:set var="port" value="8081" scope="session" />
			</c:if>
			<c:if test="${module.name eq 'attendance'}">
				<c:set var="port" value="8082" scope="session" />  <!-- page,request,Global session -->
			</c:if>
			<c:url value="http://localhost:${port}/${module.name}/welcome${module.name}" var="url" />
			<td style="border-color: black; border-width: 2px; border-style: dashed;"><a href="http://localhost:${port}/${module.name}/welcome${module.name}?token=<%= session.getAttribute("token") %>"> ${module.name} </a></td>
			<!-- &username=${pageContext.request.userPrincipal.name}&role=<sec:authentication property="principal.authorities" /> -->
		</c:forEach>
		</tr>
		</table>		   
		     
		
    </c:if>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
