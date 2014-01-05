JDON FRAMEWORK
===================================  
JdonFramework is a java reactive framework that you can use to build your Domain Driven Design + CQRS + EventSourcing  applications with asynchronous concurrency and higher throughput.

JdonFramework = DDD + Event Sourcing + CQRS + asynchronous + concurrency + higher throughput.

JdonFramework = Ioc/DI/AOP + reactive Actors model


Why Reactive?
===================================  

Reactive Programming is a hot topic , especially with such things as the [Reactive Manifesto](http://www.reactivemanifesto.org/). 
Reactive architecture allows developers to build systems that are event-driven(EDA), scalable, resilient and responsive: delivering highly responsive user experiences with a real-time feel, backed by a scalable and resilient application stack, ready to be deployed on multicore and cloud computing architectures.
a reactive application is non-blocking that is under heavy load can thus have lower latency and higher throughput than a traditional application based on blocking synchronization and communication primitives.

Why DDD?
===================================  
Domain-driven design (DDD) is an approach to developing software for complex needs by deeply connecting the implementation to an evolving model of the core business concepts,

DDD's servral concepts are the Heart and Soul of OOD:  

		Entities and Identity, Value Objects 
		Aggregate Root
		Bounded context
		
Why CQRS?
===================================  
CQRS: Command-query Responsibility Segregation, at its heart is a simple notion that you can use a different model to update information than the model you use to read information.

Why Jdon?
===================================  
Jdon introduces reactive and event-driven into domain, using jdon, a aggregate root can act as a mailbox(like scala's Actors) that is a asynchronous and non-blocking event-sending and event-recipient metaphor.
Event is a better interactive way for aggregate root with each other, instead of directly exposing behavior and hold references to others. 
and it can better protect root entity's internal state not expose. and can safely update root's state in non-blocking way [Single Writer Principle](http://www.javacodegeeks.com/2012/08/single-writer-principle.html).

Jdon moves mutable state from database to memory, and uses Aggregate Root to guard it, traditional database's data operations (by SQL or JPA/ORM) not need any more, only need send a Command or Event to drive Aggregate Root to change its mutable state by its behaviours


	@Model
	public class AggregateRootA {

		private int state = 100;
	
		@OnCommand("CommandtoEventA")  
		public Object save(ParameterVO parameterVO) {
		
			//update root's state in non-blocking single thread way (Single Writer)
			this.state = parameterVO.getValue() + state;
		
	
		}
		
	}
	
Jdonframework work mode is Producer/Consumer, A producer emits to its Consumer by a asynchronous and non-blocking queue made by LMAX Disruptor's RingBuffer.
such as:

@Send("CommandtoEventA") ---> @OnCommand("CommandtoEventA") 

There are two kinds of Consumer in jdon:

	@Send --> @OnCommand  (1:1 Queue)
	@Send --> @OnEvent    (1:N topic)

Difference between 'OnCommand' and 'OnEvent' is:

When a event happend otherwhere comes in a aggregate root we regard this event
as a command, and it will action a method annotated with @OnCommand, 
and in this method some events will happen. and they will action those methods annotated with @OnEvent.

full example: [click here](https://github.com/banq/jdonframework/blob/master/src/test/java/com/jdon/sample/test/cqrs/a/AggregateRootA.java)

GETTING STARTED
===================================  
online documents :
	English: http://en.jdon.com/
	Chinese: http://www.jdon.com/jdonframework/

  
In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.

In the "example" directory there are serveral examples that show how to use jdon, in these examples directory run 'mvn package' to get war file.

In the "JdonAccessory" directory there are struts1.x MVC and jdbc templates.

DEMO
------------------
online: http://www.jdon.com/testWeb/

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


Communication
------------------
twiiter: [@jdonframework](http://twitter.com/jdonframework)

Bugs and Feedback
------------------
For bugs, questions and discussions please use the [Github Issues](https://github.com/banq/jdonframework/issues).

LICENSE
------------------
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.