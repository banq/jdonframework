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
package com.jdon.async.disruptor;

import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.DomainMessage;
import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.TimeoutException;
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
		TreeSet<DomainEventHandler> handlers = disruptorFactory.getTreeSet();
		final DomainEventHandler<EventDisruptor> handler = new DomainEventHandler<EventDisruptor>() {

			@Override
			public void onEvent(EventDisruptor event, final boolean endOfBatch) throws Exception {
				System.out.println("\nMyEventA=" + event.getDomainMessage().getEventSource());
				event.getDomainMessage().setEventResult("not null");

			}
		};

		final DomainEventHandler<EventDisruptor> handler2 = new DomainEventHandler<EventDisruptor>() {

			@Override
			public void onEvent(EventDisruptor event, final boolean endOfBatch) throws Exception {
				System.out.println("\nMyEventA2=" + event.getDomainMessage().getEventSource());
				event.getDomainMessage().setEventResult(null);

			}
		};
		handlers.add(handler2);
		handlers.add(handler);

		Disruptor disruptor = disruptorFactory.createSingleDw("test");
		disruptorFactory.addEventMessageHandler(disruptor, "test", handlers);
		disruptor.start();

		int i = 0;

		// while (i < 10) {
		RingBuffer ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();

		DomainMessage domainMessage = new DomainMessage(sequence);

		EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.get(sequence);
		eventDisruptor.setTopic("test");
		eventDisruptor.setDomainMessage(domainMessage);

		ringBuffer.publish(sequence);
		System.out.print("\n RESULT=" + domainMessage.getBlockEventResult());

		System.out.print("\n RESULT=" + domainMessage.getBlockEventResult());

		System.out.print("\n RESULT=" + domainMessage.getBlockEventResult());

		i++;
		System.out.print(i);

		// }

		System.out.print("ok");
	}

	public void testValueEventProcessor() throws AlertException, InterruptedException, TimeoutException {
		RingBuffer ringBuffer = RingBuffer.createSingleProducer(new EventResultFactory(), 4, new TimeoutBlockingWaitStrategy(10000,
				TimeUnit.MILLISECONDS));
		ValueEventProcessor valueEventProcessor = new ValueEventProcessor(ringBuffer);

		int numMessages = ringBuffer.getBufferSize();
		int offset = 0;
		for (int i = 0; i < numMessages + offset; i++) {
			valueEventProcessor.send(i);
			System.out.print("\n push=" + i);
		}

		long expectedSequence = numMessages + offset - 1;
		SequenceBarrier barrier = ringBuffer.newBarrier();
		long available = barrier.waitFor(expectedSequence);
		assertEquals(expectedSequence, available);
		System.out.print("\n expectedSequence=" + expectedSequence);

		for (int i = 0; i < numMessages + offset; i++) {
			System.out.print("\n i=" + ((EventResultDisruptor) ringBuffer.get(i)).getValue() + " " + i);
		}
	}

}
