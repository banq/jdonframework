JDON FRAMEWORK
---------------------------------------
English: http://www.jdon.org

         http://htmlpreview.github.com/?https://github.com/banq/jdonframework/blob/master/doc/english/index.html

Chinese: http://www.jdon.com/jdonframework/


1. INTRODUCTION
---------------
JdonFramework is a java framework that you can use to build your Domain Driven Design + CQRS + EventSource  applications with asynchronous concurrency and higher throughput.

JdonFramework = DDD + Event Sourcing + CQRS + asynchronous + concurrency + higher throughput.

JdonFramework = Spring(Ioc/DI/AOP) + Ruby(DCI) + Scala(Actor)

2. RELEASE NOTES
----------------

6.6.2 version.  

(1) in-memeory cache switchs to Guava cache insteadof ehcache.

3. DISTRIBUTION JAR FILES
-------------------------

The Jdon Framework module jar files can be found in the 'dist' directory. 
jdonFramework.jar is the core of Jdon Framework.

struts and jdbc template supports in JdonAccessory directory.

4. GETTING STARTED
------------------

compilr source:
  ANT: press enter "ant" to compile and package a .jar file under "dist"  directory . 
  MAVEN: press enter "mvn package" to compile and package a .jar file
  
apply jdonframework.jar to your project:
  ANT: copy all .jar files "dist"  directory to your project classpath.
  MAVEN:
   <repository>
      <id>jdonframework</id>
      <url>https://github.com/banq/jdon-mvn-repo/raw/master/releases</url>
  </repository>

    <dependency>
        <groupId>com.jdon</groupId>
        <artifactId>jdonframework</artifactId>
        <version>6.6.4</version>
     </dependency>            

In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.

The "lib" directory only supports ANT compile and package, if you use maven, you can igonre or del it.