<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.parentDiv {
	width: 100%;
	height: 100%;
}

.childDivUpper {
	margin-top: 50px;
	width: 100%;
	height: 50%;
}

.childUpperHeader {
	width: 100%;
	height: 30%;
	text-align: center;
	vertical-align: middle;
	font-size: 2em;
	font-weight: bold;
	background-color: gray;
	color: white;
}

.childUpperForm {
	width: 100%;
	height: 40%;
	margin-top: 10px;
}

.childUpperForm table {
	width: 100%;
	height: 100%;
	border-top: 1px solid;
	border-bottom: 1px solid;
	border-right: 1px solid;
	border-left: 1px solid;	  
}

.childUpperForm table tr {
	height: 50px;
}

.childUpperForm .colInfo{
	width: 50%;
	text-align: right !important;
}

.childUpperForm .colField{
	padding-left: 30px;
}

.childDivLower {
	font-size: 1em;
	font-style: italic;
	font-weight: bold;	
}

.error {
	color: red;
}

.success {
	color: green;
}
</style>
</head>
<body>

	<div class="parentDiv">
		<div class="childDivUpper">
			<div class="childUpperHeader">Upload your file and share it</div>
			<div class="childUpperForm">
				<form method="POST" action="upload" enctype="multipart/form-data">
					<table>
						<tr>
							<td class="colInfo">Browse your file :</td>
							<td class="colField"><input type="file" name="file" /></td>
						</tr>
						<tr>
							<td class="colInfo">Enter email to share:</td>
							<td class="colField"><input type="text" name="email"/></td>
						</tr>
						<tr>
							<td colspan="2" style="text-align: center;"><input type="submit" value="Share & Email" /></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<br/>
		<div class="childDivLower">
			<c:choose>
			<c:when test="${errorFlag}"> 
				<span class="error">${message}</span>
			</c:when>
			<c:otherwise>
				<span class="success">${message}</span>
			</c:otherwise>
			</c:choose>
				
		</div>
	</div>
</body>
</html>