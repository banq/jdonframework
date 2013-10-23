JDON FRAMEWORK
===================================  
JdonFramework is a java reactive framework that you can use to build your Domain Driven Design + CQRS + EventSourcing  applications with asynchronous concurrency and higher throughput.

JdonFramework = DDD + Event Sourcing + CQRS + asynchronous + concurrency + higher throughput.

JdonFramework = Ioc/DI/AOP + reactive Actors model


The Need to go Reactive
===================================  

Reactive Programming is a hot topic as of late, especially with such things as the [Reactive Manifesto](http://www.reactivemanifesto.org/). 
Applications, especially on the web have changed over the years from being a simple static page, to DHTML with animations, to the Ajax revolution. Each time, we're adding more complexity, more data, and asynchronous behavior to our applications. How do we manage it all? How do we scale it? By moving towards "Reactive Architectures" which are event-driven, resilient and responsive. With the Reactive Extensions, you have all the tools you need to help build these systems.

INTRODUCTION
===================================  


RELEASE NOTES
===================================  

6.8 version.  

add command of CQRS:
UI ---Command---> a aggregate root ---DomainEvents---> another aggregate root/Component

A aggregate root in Jdon acts like Actors of AKKA or Erlang.

stable version download: 
http://sourceforge.net/projects/jdon/files/JdonFramework/6.X_DDD/


DISTRIBUTION JAR FILES
===================================  

apply jdonframework.jar to your project:
  MAVEN:  
    <dependency>
        <groupId>org.jdon</groupId>
        <artifactId>jdonframework</artifactId>
        <version>6.8</version>
     </dependency>            


GETTING STARTED
===================================  
  
In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.

In the "example" directory there are serveral examples that show how to use jdon, in these examples directory run 'mvn package' to get war file.

In the "JdonAccessory" directory there are struts1.x MVC and jdbc templates.

DEMO
------------------
online: http://www.jdon.com/testWeb/

Document
===================================  
English: http://www.jdon.org         
Chinese: http://www.jdon.com/jdonframework/


Contact
------------------
twiiter: @jdonframework 
 