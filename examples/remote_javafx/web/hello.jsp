<%@ page contentType="application/x-java-jnlp-file" %><%

response.setContentType("application/x-java-jnlp-file");
response.setHeader("Cache-Control", "must-revalidate");
response.setHeader("Pragma", "no-cache");

long now = System.currentTimeMillis();
response.setDateHeader("Date", now);
response.setDateHeader("Last-Modified", now);
response.setDateHeader("Expires", 0);

//Force IE to recognize MIME type
String fileName = request.getServletPath();
fileName = fileName.substring(fileName.lastIndexOf("/") + 1 );
fileName = fileName.substring(0, fileName.indexOf(".")) + ".jnlp";
response.setHeader("Content-Disposition", "Inline; fileName=" + fileName);

String uri = request.getScheme() + "://" + 
request.getServerName() + ":" +
request.getServerPort() + 
request.getContextPath();

%><?xml version="1.0" encoding="UTF-8"?>
<jnlp spec="1.0+" codebase="<%= uri %>" href="hello.jsp">
    <information>
        <title> JavaFX HelloWorld jdon Sample</title>   
         <vendor>jdon.com</vendor>     
    </information>
    
    <security> 
        <all-permissions/>
    </security> 
    
    <resources>
        <j2se version="1.5+" href="http://java.sun.com/products/autodl/j2se" 
              java-vm-args="-Xss1M -Xmx256M"/>
        <jar href="jnlp/javafxrt.jar" main="true"/>
        <jar href="jnlp/swing-layout.jar"/>
        <jar href="jnlp/hessian-3.1.6.jar"/>
        <jar href="hello.jar"/>
    </resources>
    
    <application-desc main-class="net.java.javafx.FXShell">
        <argument>sample.FXClient</argument>
        <argument>--</argument>
        <argument><%= uri + "/remote/helloService" %></argument>
    </application-desc>
</jnlp>
