<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>   
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>User</title>
</head>
<body>

<div style="text-align: center;">
    <h1>User Edit</h1>
    <hr>

    <form action="${pageContext.request.contextPath}/user" method="post">
        <input type="hidden" name="_method" value="PUT"/>
        <input type="hidden" name="user.userId" value="${user.userId}"/>
        username:<input type="text" name="user.name" value="${user.name}"/>
        email:<input type="text" name="user.email" value="${user.email}"/>
        <br>
        <c:if test="${not empty  user.uploadFile}">
        	  pic :<img src="<%=request.getContextPath() %>/showUpload?pid=${user.userId}"  border='0'/>	
        </c:if>

        <input type="submit" value="submit"/>
    </form>
</div>
</body>
</html>