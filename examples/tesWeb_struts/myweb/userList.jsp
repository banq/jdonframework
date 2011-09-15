<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/MultiPages.tld" prefix="MultiPages" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<head>
<title>
User
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<link href="jdon.css" rel="stylesheet" type="text/css">
<body>
<html:errors/>
<center>
<h3>JdonFramework Application DEMO
  <br />note: add above 5 line, you will find other pages (Auto Pagination).  
</h3>

<form action="" method="POST" name="listForm" >



<table width="550" cellpadding=6 cellspacing=0 border=1  align="center">
  <tr  bgcolor="#C3C3C3">


   <tr>

    <td bgcolor="#D9D9D9">userId</td>
    <td bgcolor="#D9D9D9">name</td>
    <td bgcolor="#D9D9D9">age</td>
    <td bgcolor="#D9D9D9">operation</td>
</tr>
<logic:iterate indexId="i"   id="user" name="listForm" property="list" >
<tr bgcolor="#ffffff">

    <td><bean:write name="user" property="userId" /></td>
    <td ><bean:write name="user" property="name" /></td>
     <td ><bean:write name="user" property="age" /></td>
     <td>
         <input type="radio" name="userId" value="<bean:write name="user" property="userId" />" >
     </td>
</tr>

</logic:iterate>

<tr><td colspan="3">

<html:link page="/userAction.do">Add</html:link>


</td>
  <td >
       <input type="button" name="edit" value="Edit" onclick="editAction('userId')" >
       <input type="button" name="delete" value="Delete" onclick="delAction('userId')" >
  </td>
</tr>

</table>



</form>
<center>
<script type="text/JavaScript">
function editAction(radioName){
    var isChecked = false;

   if (eval("document.listForm."+radioName).checked){
          isChecked = true;
    }else{
      for (i=0;i<eval("document.listForm."+radioName).length;i++){
         if (eval("document.listForm."+radioName+ "["+i+"]").checked){
           isChecked = true;
           break;
          }
      }
    }
    if (!isChecked){
      alert("请选择一个条目");
      return;
    }else{
      document.listForm.action="<%=request.getContextPath()%>/userAction.do?action=edit"
      document.listForm.submit();
    }
}

function delAction(radioName){
    var isChecked = false;

   if (eval("document.listForm."+radioName).checked){
          isChecked = true;
    }else{
      for (i=0;i<eval("document.listForm."+radioName).length;i++){
         if (eval("document.listForm."+radioName+ "["+i+"]").checked){
           isChecked = true;
           break;
          }
      }
    }
    if (!isChecked){
      alert("请选择一个条目");
      return;
    }else{
       if (confirm( 'Delete this order ! \n\nAre you sure ? '))
        {
              document.listForm.action="<%=request.getContextPath()%>/userSaveAction.do?action=delete"
              document.listForm.submit();
              return true;
         }else{
              return false;
         }
    }
}

</script>
<div class="yahoo2">    
<MultiPages:pager actionFormName="listForm" page="/userListAction.do">

<MultiPages:prev name="[Prev ]" />
<MultiPages:index displayCount="1" />
<MultiPages:next  name="[Next ]" />
</MultiPages:pager>
</div>
<br>

</center>
</body>
</html:html>
