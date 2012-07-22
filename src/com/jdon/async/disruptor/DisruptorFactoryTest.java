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
package com.jdon.async.disruptor;

import java.util.TreeSet;

import junit.framework.TestCase;

import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.DomainMessage;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorFactoryTest extends TestCase {
	DisruptorFactory disruptorFactory;

	protected void setUp() throws Exception {
		super.setUp();
		disruptorFactory = new DisruptorFactory();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetDisruptor() {
		TreeSet handlers = new TreeSet();
		final DomainEventHandler<EventDisruptor> handler = new DomainEventHandler<EventDisruptor>() {

			@Override
			public void onEvent(EventDisruptor event, final boolean endOfBatch) throws Exception {
				System.out.println("MyEventA=" + event.getDomainMessage().getEventSource());
				event.getDomainMessage().setEventResult(8888888 + (Long) event.getDomainMessage().getEventSource());

			}
		};

		handlers.add(handler);
		Disruptor disruptor = disruptorFactory.addEventMessageHandler("test", handlers);
		disruptor.start();

		int i = 0;

		while (i < 20) {
			RingBuffer ringBuffer = disruptor.getRingBuffer();
			long sequence = ringBuffer.next();

			DomainMessage domainMessage = new DomainMessage(sequence);
			domainMessage.setResultEvent(new EventResultHandlerImp("test", domainMessage));

			EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.get(sequence);
			eventDisruptor.setTopic("test");
			eventDisruptor.setDomainMessage(domainMessage);

			ringBuffer.publish(sequence);
			System.out.print("\n RESULT=" + domainMessage.getBlockEventResult());

			i++;
		}

	}
}
