<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html:html>
<head>
<title>
user
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
<h3>Jdon Framework APP DEMO</h3>
<p>
<logic:present name="errors">
  <logic:iterate id="error" name="errors">
    <B><FONT color=RED>
      <BR><bean:write name="error" />
    </FONT></B>
  </logic:iterate>
</logic:present>
<script language="JavaScript" type="text/javascript">
<!--

function checkPost() {
      var check = false;
      if ((document.userActionForm.userId.value != "")
          && (document.userActionForm.name.value != "")){
          check = true;
      }else{
          alert("you must input something!");
      }
      return check;
}
//-->
</script>

<html:form action="/userSaveAction.do" method="POST" onsubmit="return checkPost();">
<html:hidden property="action"/>

UserId:<html:text property="userId"/>
<br>
Name:<html:text property="name"/>
<br>
<html:submit property="submit" value="Submit"/>
</html:form>
</body>
</html:html>
