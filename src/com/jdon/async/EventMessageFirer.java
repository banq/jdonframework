/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package com.jdon.async;

import com.jdon.annotation.model.Send;
import com.jdon.async.disruptor.DisruptorFactory;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.async.future.EventMessageFuture;
import com.jdon.async.future.FutureDirector;
import com.jdon.async.future.FutureListener;
import com.jdon.domain.message.DomainMessage;

public class EventMessageFirer {
	private DisruptorFactory disruptorFactory;
	private FutureDirector futureDirector;

	public EventMessageFirer(DisruptorFactory disruptorFactory, FutureDirector futureDirector) {
		super();
		this.disruptorFactory = disruptorFactory;
		this.futureDirector = futureDirector;
	}

	public void fire(DomainMessage domainMessage, Send send, FutureListener futureListener) {
		EventMessageFuture eventMessageFuture = new EventMessageFuture(send.value(), futureListener, domainMessage);
		eventMessageFuture.setAsyn(send.asyn());
		domainMessage.setEventMessage(eventMessageFuture);
		futureDirector.fire(domainMessage);

	}

	public void fire(DomainMessage domainMessage, Send send) {
		String topic = send.value();
		EventDisruptor eventDisruptor = disruptorFactory.getEventDisruptor(topic);
		eventDisruptor.setTopic(topic);
		eventDisruptor.setDomainMessage(domainMessage);
		domainMessage.setEventMessage(eventDisruptor);
		eventDisruptor.publish();
		// disruptorFactory.fire(topic, eventDisruptor);
	}

}
