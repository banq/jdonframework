<%@ page pageEncoding="UTF-8"%>
<head>
<title>
user
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta   http-equiv="pragma"   content="no-cache">
<link href="jdon.css" rel="stylesheet" type="text/css">
</head>
<body>
<h3>Jdon Framework APP DEMO</h3>
<p>
<div>
    <form action="users" method="post">
        <table width="500" border="1">
            <tr>
                <td align="right">name</td>
                <td>
                    <input name="user.username" type="text" value="${user.username}">
                    <span><font color="#FF0000"> ${errorsMap.name} </font></span>
                </td>
            </tr>
            <tr>
                <td align="right">password</td>
                <td>
                    <input name="user.password" type="text" value="${user.password}">
                    <span><font color="#FF0000"></font></span>
                </td>
            </tr>
            <tr>
                <td align="right">confirm password</td>
                <td>
                    <input name="user.verifypassword" type="text">
                    <span><font color="#FF0000"> ${errorsMap.password} </font></span>
                </td>
            </tr>
            </tr>
            <td align="right">email</td>
            <td>
                <input name="user.email" type="text" value="${user.email}">
                <span><font color="#FF0000"> ${errorsMap.email} </font></span>
            </td>
            </tr>
            <td align="right">pic</td>
            <td>
              <a href="single_upload.jsp" target="_blank">upload single-file</a>
            </td>
            </tr>

            <tr>
                <td><input type="submit" value="submit"/></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
