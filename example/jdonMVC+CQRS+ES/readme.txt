CRUD for a Model
JdonMVC + Jdon + JDBC 


this is a  simple application Demo for Jdon framework: struts + jdon + jdbc
deploy the testApplication.ear to JBoss server, 
run it in browser url: http://localhost:8080/testWeb/


the applicationlog.txt is a server.log of jboss that running a complication j2ee system with Jdonframework!
you can find all function are active;


intall Details (http://www.jdon.com/jdonframework/demo-en.htm)

1. download J2SE1.5 or above, setup Windows/Linux ENV : JAVA_HOME=your j2se1.4 directory

2. download JBoss or Tomcat

3. startup your MySQL database.
   open database.sql , and populate the table testuser into your database.
   remember your database name, here is "test", if change it, you must
   change mysql-ds.xml's <connection-url> value.

4. if your database is MYSQL, below is configure for jboss, if
   you use tomcat. reference this artilce: http://www.7880.com/Info/Article-37f05fa0.html
   
   copy  lib/jeelib/mysql-connector-java-3.0.14-production-bin.jar to jboss/server/default/lib, and restart jboss
   copy mysql-ds.xml to jboss/server/default/deploy,
   open mysql-ds.xml:
   <datasources>
  <local-tx-datasource>
    <jndi-name>TestDS</jndi-name>
    <connection-url>jdbc:mysql://localhost:3306/test</connection-url>
    <driver-class>com.mysql.jdbc.Driver</driver-class>
    <user-name>root</user-name>
    <password></password>
  </local-tx-datasource>
  </datasources>
  if your mysql database's root password changed ,change the <password> value
  if your database is not MYSQL, check jboss reference, know how to 
  edit other db such as Oracle deploy file;
  note: the jndi name must be TestDS, it has configured in application jboss-web.xml (no need modify)
<jboss-web>
    <resource-ref>
        <res-ref-name>jdbc/TestDS</res-ref-name>
        <res-type>javax.sql.DataSource</res-type>
        <jndi-name>java:/TestDS</jndi-name>
    </resource-ref>    
</jboss-web>
   
 if you use Tomcat,
(1).copy lib/jeelib/mysql-connector-java-3.0.14-production-bin.jar to tomcat/lib or tomcat/commons/lib
(2).add below to your tomcat/conf/context.xml
<Resource name="jdbc/TestDS" 
auth="Container" 
type="javax.sql.DataSource" 
maxActive="100" 
maxIdle="30" 
maxWait="10000" 
username="root" 
password="" 
driverClassName="com.mysql.jdbc.Driver"
url="jdbc:mysql://localhost/test" />

username="root" is youre database's user name 
password=""     is user name's password


5. startup jboss/bin/startup.bat  or tomcat/bin/startup.bat
   check the console, if there are many errors, open jboss/server/default/log/server.log
   and will find it maybe caused by database connection, carefully check step 7/8
   
6. open browser, http://localhost:8080/jdonmvc_cqrs_es/  
   DEMO:  http://www.jdon.com/testWeb/
