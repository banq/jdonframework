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
package com.jdon.domain.message;

import com.jdon.async.disruptor.EventDisruptor;

/**
 * This is event Disruptor EvenHandler.
 * 
 * if event class annotated with @Consumer(XX); it must implements
 * com.jdon.domain.message.DomainEventHandler
 * 
 * 
 * * Domain Model producer /Consumer:
 * 
 * 1. annotate the producer class with @Model and @Introduce("message")
 * 
 * 
 * 2. annotate the method with @Send("mytopic") of the producer class;
 * 
 * 3. the "mytopic" value in @Send("mytopic") is equals to the "mytopic" value
 * in @Consumer("mytopic");
 * 
 * 4. annotate the consumer class with @Consumer("mytopic");
 * 
 * 5. the consumer class must implements
 * com.jdon.domain.message.DomainEventHandler
 * 
 * @author banq
 * 
 * @param <EventDisruptor>
 */
public interface DomainEventHandler<T> {

	void onEvent(EventDisruptor event, final boolean endOfBatch) throws Exception;
}
