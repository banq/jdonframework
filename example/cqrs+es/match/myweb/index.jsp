<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Match</title>
</head>
<body>

this is a CQRS + Eventsourcing demo.

<form action="<%=request.getContextPath() %>/commandServlet" method="get">
home name:
<input type="text" name="homename"  value="jdonOne">
ayway name:
<input type="text" name="awayname"  value="jdonTwo">

<input type="submit"/>
</form>

<a href="<%=request.getContextPath() %>/queryServlet">query all matchs</a>
</body>
</html>