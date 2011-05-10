<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="./prototype.js"> </script>
</head>
<body>

<form action="/myweb/myServlet" method="get">
enter your name:
<input type="text" name="myname" id="mynameId" value="999">
<input type="submit"/>
</form>

<script>
    function getHTML()
    {    
        alert($('mynameId').value);
        var pars = 'myname=' + $('mynameId').value;
        alert(pars);
        new Ajax.Updater('out', '/myweb/myServlet', { method: 'get', parameters: pars });
        
    }
</script>
<input type=button value=GetHtml onclick="getHTML()">
<div id="out"></div>
</body>
</html>