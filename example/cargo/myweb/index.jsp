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
<input type="hidden" name="mode"  value="yy">
enter your name:
trackingId:<input type="text" name="trackingId" id="mynameId" value="XYZ">
<br>
carrierMovementId:<input type="text" name="carrierMovementId" id="carrierMovementId" value="SESTO_DEHAM">
<br>
unlocode:<input type="text" name="unlocode" id="unlocode" value="CNHKG">

<input type="submit" value=" 预约 "/>
</form>

<script>
    function getHTML()
    {    
        alert($('mynameId').value);
        var pars = 'trackingId=' + $('mynameId').value;
        alert(pars);
        new Ajax.Updater('out', '/myweb/myServlet', { method: 'get', parameters: pars });
        
    }
</script>
<input type=button value=GetHtml onclick="getHTML()">
<div id="out"></div>

<hr/>

<form action="/myweb/myServlet" method="get">
<input type="hidden" name="mode"  value="unload">
enter cargo:
trackingId:<input type="text" name="trackingId" id="mynameId" value="XYZ">
<br>
carrierMovementId:<input type="text" name="carrierMovementId" id="carrierMovementId" value="SESTO_DEHAM">
<br>
unlocode:<input type="text" name="unlocode" id="unlocode" value="CNHKG">

<input type="submit" value="unload"/>
</form>


</body>
</html>