/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * Consumer of the producer annotated with @send(topic) of the method;
 *
 * * Topic/queue(1:N or 1:1):
 *
 * if event class annotated with @Consumer(XX); it must implements
 * com.jdon.domain.message.DomainEventHandler
 *
 * the @Send(topicName) ==> @Consumer(topicName);
 *
 * if there are many consumers, execution order will be
 *                  alphabetical list by Name of @Consumer class.
 *
 * Domain Model producer /Consumer:
 *
 * 1. annotate the producer class with @Model and @Introduce("message")
 * the @Model
 * the @Introduce("message")
 *public class DomainEvent {}
 * 
 * 
 * 2. annotate the method with @Send("mytopic") of the producer class;
 *  the @Send("mytopic")
 *	public DomainMessage myMethod() {
 *		DomainMessage em = new DomainMessage(this.name);
 *		return em;
 *	}
 * 
 * 3. the "mytopic" value in @Send("mytopic") is equals to the "mytopic" value
 * in @Consumer("mytopic");
 * 
 * 4. annotate the consumer class with @Consumer("mytopic");
 * 
 * 5. the consumer class must implements
 * com.jdon.domain.message.DomainEventHandler
 *
 * '@Consumer("mychannel")'
 *public class MyDomainEventHandler implements DomainEventHandler {}
 * 
 * 
 * 
 * @author banq
 * @see @Send
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Consumer {
	/**
	 * topic name
	 * 
	 * @Send(topicName) ==> @Consumer(topicName);
	 * 
	 * @return topic name
	 */
	String value();

}
