package com.jdon.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * 
 * Domain Model should normal live in memory not in database. so cache in memory
 * is very important for domain model life cycle.
 * 
 * Example producer: com.jdon.sample.test.domain.onecase.DomainEvent
 * Consumer:com.jdon.sample.test.domain.onecase.DomainListener
 * 
 * Domain Model producer /Consumer:
 * 
 * 1. annotate the producer class with @Model and @Introduce("message")
 *
 * <br> @Model
 * <br> @Introduce("message") public class DomainEvent {}
 * 
 *                       the value "message" of @@Introduce("message") is the
 *                       om.jdon.domain.message.MessageInterceptor configured in
 *                       aspect.xml
 * 
 *                       2. annotate the method with @Send("mytopic") of the
 *                       producer class; * @Send("mytopic") public DomainMessage
 *                       myMethod() { DomainMessage em = new
 *                       DomainMessage(this.name); return em; }
 * 
 *                       3. the "mytopic" value in @Send("mytopic") is equals to
 *                       the "mytopic" value in @Consumer("mytopic");
 * 
 *                       4. annotate the consumer class with
 *                       <br>@Consumer("mytopic");
 * 
 *                       5.there are two kind of consumer
 * 
 *                       (1)the consumer class must implements
 *                       com.jdon.domain.message.DomainEventHandler
 *
 * <br>@Consumer("mytopic") public class MyDomainEventHandler implements
 *                      DomainEventHandler {
 * 
 *                      public void onEvent(EventDisruptor event, boolean
 *                      endOfBatch) throws Exception{..}
 * 
 *                      }
 * 
 * 
 *                      (2)or the consumer class's method annotated with
 *                      <br>@onEvent("mytopic")
 * 
 * 
 * 
 * 
 *                      Topic/queue(1:N or 1:1):
 *
 * <br>@Send(topicName) ==> @Consumer(topicName);
 * 
 * 
 * 
 *                  under version 6.3 there is event Older queue(1:1):
 *
 * <br>@Send(topicName) ==> @Component(topicName);
 * 
 *                  The message accepter class annotated with
 * <br>@Component(topicName) must implements com.jdon.domain.message.MessageListener
 * 
 * 
 * @see com.jdon.controller.model.ModelIF
 * @author banQ
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface Send {
	/**
	 * topic/queue name
	 *
	 * <br> @Send(topicName) ==> @Consumer(topicName);
	 * 
	 * @return topic/queue name
	 */
	String value();

	boolean asyn() default true;
}
