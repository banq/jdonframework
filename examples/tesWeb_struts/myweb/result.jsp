<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>result</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body bgcolor="#ffffff">
<logic:messagesPresent>
  <html:errors/>
</logic:messagesPresent>
<logic:messagesNotPresent>
  <bean:write name="userActionForm" property="name"/> <br>
  Operate Successfully!

</logic:messagesNotPresent>
return <html:link page="/index.jsp">index</html:link>
</body>
</html>
