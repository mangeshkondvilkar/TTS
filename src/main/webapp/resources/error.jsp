<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TTS Error Page</title>

</head>
<body>
	<h1>An application error has been occurred. Please contact your
		administrator support for more help...</h1>
	<br>
	<p>You may additionally view page source for error details: Right click -> View page source</p>

	<!--
    Failed URL: ${url}
    Exception:  ${exception.message}
	    <c:forEach items="${exception.stackTrace}" var="ste">    ${ste} 
	    </c:forEach>
  	-->
</body>
</html>