JDON FRAMEWORK
---------------------------------------
English: http://www.jdon.org

         http://htmlpreview.github.com/?https://github.com/banq/jdonframework/blob/master/doc/english/index.html

Chinese: http://www.jdon.com/jdonframework/


1. INTRODUCTION
---------------
JdonFramework is a java reactive framework that you can use to build your Domain Driven Design + CQRS + EventSourcing  applications with asynchronous concurrency and higher throughput.

JdonFramework = DDD + Event Sourcing + CQRS + asynchronous + concurrency + higher throughput.

JdonFramework = Ioc/DI/AOP + reactive Actors model

2. RELEASE NOTES
----------------

6.6.8 version.  

add command of CQRS:
UI ---Command---> a aggregate root ---DomainEvents---> another aggregate root/Component

A aggregate root in Jdon acts like Actors of AKKA or Erlang.

stable version download: 
http://sourceforge.net/projects/jdon/files/JdonFramework/6.X_DDD/


3. DISTRIBUTION JAR FILES
-------------------------

apply jdonframework.jar to your project:
  MAVEN:  
    <dependency>
        <groupId>org.jdon</groupId>
        <artifactId>jdonframework</artifactId>
        <version>6.6.8</version>
     </dependency>            


4. GETTING STARTED
------------------
  
In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.

In the "example" directory there are serveral examples that show how to use jdon, in these examples directory run 'mvn package' to get war file.

In the "JdonAccessory" directory there are struts1.x MVC and jdbc templates.
