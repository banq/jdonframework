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
package com.jdon.sample.test.domain.onecase;

import com.jdon.annotation.Consumer;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainEventHandler;

/**
 * acccept Domain message from @Send("mychannel") of @Introduce("message")
 * 
 * this is event consumer with the ringbuffer of the disruptor
 * 
 * @author banq
 * 
 */
@Consumer("mychannel")
public class MyDomainEventHandler implements DomainEventHandler {
	public void onEvent(EventDisruptor event, boolean endOfBatch) throws Exception {
		System.out.println("MyDomainEventHandler action " + event.getDomainMessage().getEventSource());
	}

}
