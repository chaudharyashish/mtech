
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1>Hello <sec:authentication property="principal.username" /></h1><h3>This is attendance module welcome page.</h3>

<h4>To Go back from this module </h4> <h2><a href="${contextPath}/sso">Click here</a></h2>