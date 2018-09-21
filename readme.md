JDON FRAMEWORK
===================================
JdonFramework is a java Reactive/Actor framework that you can use to build your Domain Driven Design + CQRS + EventSourcing  applications with asynchronous concurrency and higher throughput.

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

Actor Model
===================================
Jdon can make your Domain Model as Actor(erlang/akka) concurrent model, no any lock. this is a transaction example about transferring money from one bank account to another:


	@Model
	public class BankAccount {

	....

	@OnCommand("depositCommand")
	public Object deposit(TransferEvent transferEvent) {
		int amount2 = amount + transferEvent.getValue();
		if (amount2 > 1000) {
			TransferEvent transferEventNew = new ResultEvent(
					transferEvent.getId(), transferEvent.getValue(),
					this.getId());
			return domainEventProducer.failure(transferEventNew);
		}

		if (!(transferEvent instanceof WithdrawEvent)) {// first step
			DepositEvent transferEventNew = new DepositEvent(
					transferEvent.getId(), transferEvent.getValue(),
					transferEvent.getNextId(), this.getId());
			eventsourcesD.put(transferEventNew.getId(), transferEventNew);
			return domainEventProducer.nextStep(transferEventNew);
		}

		WithdrawEvent de = (WithdrawEvent) transferEvent;
		amount = amount + transferEvent.getValue();
		TransferEvent transferEventNew = new ResultEvent(transferEvent.getId(),
				transferEvent.getValue(), de.getPreId());
		return domainEventProducer.finish(transferEventNew);

	}

	@OnCommand("withdrawCommand")
	public Object withdraw(TransferEvent transferEvent) {
		int amount2 = amount - transferEvent.getValue();
		if (amount2 < 0) {
			String rootId = (transferEvent instanceof DepositEvent) ? ((DepositEvent) transferEvent)
					.getPreId() : this.getId();
			TransferEvent transferEventNew = new ResultEvent(
					transferEvent.getId(), transferEvent.getValue(), rootId);
			return domainEventProducer.failure(transferEventNew);
		}

		if (!(transferEvent instanceof DepositEvent)) {// first step
			WithdrawEvent transferEventNew = new WithdrawEvent(
					transferEvent.getId(), transferEvent.getValue(),
					transferEvent.getNextId(), this.getId());
			eventsourcesW.put(transferEventNew.getId(), transferEventNew);
			return domainEventProducer.nextStep(transferEventNew);

		}

		DepositEvent de = (DepositEvent) transferEvent;
		amount = amount - transferEvent.getValue();
		TransferEvent transferEventNew = new ResultEvent(transferEvent.getId(),
				transferEvent.getValue(), de.getPreId());
		return domainEventProducer.finish(transferEventNew);

	}

	@OnCommand("finishCommand")
	public Object finish(TransferEvent transferEvent) {
		if (eventsourcesW.containsKey(transferEvent.getId())){
			eventsourcesW.remove(transferEvent.getId());
			amount = amount - transferEvent.getValue();
		}else if (eventsourcesD.containsKey(transferEvent.getId())){
			eventsourcesD.remove(transferEvent.getId());
			amount = amount + transferEvent.getValue();
		}
		return transferEvent;
	}

	@OnCommand("failureCommand")
	public Object fail(TransferEvent transferEvent) {
		eventsourcesD.remove(transferEvent.getId());
		return transferEvent;
	}

	....

	}

transfer money client code:

		AppUtil appUtil = new AppUtil();
		AccountService accountService = (AccountService) appUtil
				.getComponentInstance("accountService");
		BankAccount bankAccountA = accountService.getBankAccount("11", 100);
		BankAccount bankAccountB = accountService.getBankAccount("22", 0);
		DomainMessage res = accountService.transfer(bankAccountA, bankAccountB,
				100);

		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		DomainMessage result = (DomainMessage) res1.getBlockEventResult();
		Object o = result.getBlockEventResult();// block until all transfer ok;

		Assert.assertEquals(0, bankAccountA.getAmount());
		Assert.assertEquals(100, bankAccountB.getAmount());


detail: [click here](https://github.com/banq/jdonframework/tree/master/src/test/java/com/jdon/sample/test/bankaccount)

Apache Kafka + Eventsourcing
===================================
Apache Kafka supports Exactly-once delivery, Jdon Actor + Kafka can implement distributed transaction.

detail: [click here](https://github.com/banq/jdon-kafka)

About workflow?
===================================
DDD/ES:  command ---> aggregates ---> domain events
BPMN:     startEvent ---> Task ----> endEvent
join with them:
command/startEvent   ---> task/aggregates --->domain events/endEvent

next version will join with them.....

GETTING STARTED
===================================
online documents :
	English: http://en.jdon.com/
	Chinese: http://www.jdon.com/jdonframework/


In the "doc\english" directory there are all documents about how to use.

In the "doc\chinese" directory there are chinese documents about how to use.

In the "example" directory there are serveral examples that show how to use jdon, in these examples directory run 'mvn package' to get war file.

In the "JdonAccessory" directory there are struts1.x MVC and jdbc templates.

Example
------------------
[jivejdon](https://github.com/banq/jivejdon) is a discussion forum/blog/CMS platform powered by 
jdonframework


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