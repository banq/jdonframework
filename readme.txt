JDON FRAMEWORK
---------------------------------------
English: http://www.jdon.org

         http://htmlpreview.github.com/?https://github.com/banq/jdonframework/blob/master/doc/english/index.html

Chinese: http://www.jdon.com/jdonframework/


1. INTRODUCTION
---------------
JdonFramework is a java framework that you can use to build your Domain Driven Design + CQRS + EventSourcing  applications with asynchronous concurrency and higher throughput.

JdonFramework = DDD + Event Sourcing + CQRS + asynchronous + concurrency + higher throughput.

JdonFramework = Spring(Ioc/DI/AOP) + Ruby(DCI) + Scala(Actor)

2. RELEASE NOTES
----------------

6.6.6 version.  

add command of CQRS:
UI ---Command---> a aggregate root ---DomainEvents---> another aggregate root/Component

A aggregate root in Jdon acts like Actors of AKKA or Erlang.


3. DISTRIBUTION JAR FILES
-------------------------

The Jdon Framework module jar files can be found in the 'dist' directory. 
jdonFramework.jar is the core of Jdon Framework.

struts and jdbc template supports in JdonAccessory directory.

4. GETTING STARTED
------------------

compilr source:
  MAVEN: press enter "mvn package" to compile and package a .jar file
  
apply jdonframework.jar to your project:
  MAVEN:  
    <dependency>
        <groupId>org.jdon</groupId>
        <artifactId>jdonframework</artifactId>
        <version>6.6.6</version>
     </dependency>            

full pom.xml: https://github.com/banq/jdonframework/blob/master/doc/english/pom.xml

In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.
