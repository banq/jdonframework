<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta   http-equiv="pragma"   content="no-cache">
  <title>JdonMVC demo</title>
</head>


<body>
<div style="text-align: center;">
<h3>JdonFramework Application DEMO
  <br />note: add above 5 line, you will find other pages (Auto Pagination).
  <br/>Source Download: <a href="sourceforge.net/projects/jdon/files/JdonFramework-App/samples-6.6/">click here</a>
</h3>


<table width="550" cellpadding=6 cellspacing=0 border=1  align="center">
  <tr  bgcolor="#C3C3C3">
    <td bgcolor="#D9D9D9">image</td>
    <td bgcolor="#D9D9D9">userId</td>    
    <td bgcolor="#D9D9D9">name</td>
    <td bgcolor="#D9D9D9">operation</td>
  </tr>

<c:forEach var="user" items="${userList}">
<tr bgcolor="#ffffff">
<td><c:if test="${not empty  user.uploadFile}">
        	  pic :<img src="<%=request.getContextPath() %>/showUpload?pid=${user.userId}"  border='0' width="20" height="20"/>	
        </c:if>
</td>
<td>${user.userId}</td><td>${user.name}</td>
<td><a href="user/${user.userId}">edit</a>&nbsp;
<form action="user/${user.userId}" method="post">
<input type="hidden" name="_method" value="DELETE" />
<input type="submit" value="delete" />
</form>
</tr>
</c:forEach>
</table>

<a href="newUser.jsp">add</a>

</div>

</body>
</html>
