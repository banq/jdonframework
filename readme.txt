JDON FRAMEWORK
---------------------------------------
English: http://www.jdon.org

         http://htmlpreview.github.com/?https://github.com/banq/jdonframework/blob/master/doc/english/index.html

Chinese: http://www.jdon.com/jdonframework/


1. INTRODUCTION
---------------
JdonFramework is a java opensource framework that supports Domain-Driven Design  DCI CQRS and Domain Events/Event Source development.

JdonFramework = DDD + DCI + Event Sourcing/CQRS.
in JdonFramework, A Role played by a Domain Model triggers a interaction event, such as sending a event or message to other consumers. 


2. RELEASE NOTES
----------------

6.6.1 version.  

(1) in-memeory cache switchs to Guava cache insteadof ehcache.

3. DISTRIBUTION JAR FILES
-------------------------

The Jdon Framework module jar files can be found in the 'dist' directory. 
jdonFramework.jar is the core of Jdon Framework.

struts and jdbc template supports in JdonAccessory directory.

4. GETTING STARTED
------------------
In the "example" directory there are several examples for web application.

You can run runTest.bat in this directory to see how to play JdonFramework in Application.

com.jdon.sample.* show how to fully using JdonFramework.

In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.
