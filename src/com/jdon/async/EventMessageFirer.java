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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jdon.annotation.model.Send;
import com.jdon.async.disruptor.DisruptorFactory;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.async.future.EventResultFuture;
import com.jdon.async.future.FutureDirector;
import com.jdon.async.future.FutureListener;
import com.jdon.cache.UtilCache;
import com.jdon.container.pico.Startable;
import com.jdon.domain.message.DomainMessage;
import com.jdon.util.Debug;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class EventMessageFirer implements Startable {
	public final static String module = EventMessageFirer.class.getName();
	private static ScheduledExecutorService scheduExecStatic = Executors.newScheduledThreadPool(1);

	private DisruptorFactory disruptorFactory;
	private FutureDirector futureDirector;
	private final UtilCache topicDisruptors;

	public EventMessageFirer(DisruptorFactory disruptorFactory, FutureDirector futureDirector) {
		super();
		this.disruptorFactory = disruptorFactory;
		this.futureDirector = futureDirector;
		this.topicDisruptors = new UtilCache(100, 60 * 60 * 1000, true);
	}

	public void start() {
		Runnable task = new Runnable() {
			public void run() {
				topicDisruptors.clear();
			}
		};
		scheduExecStatic.scheduleAtFixedRate(task, 0, 60 * 60 * 24, TimeUnit.SECONDS);
	}

	public void stop() {
		topicDisruptors.clear();
	}

	public void fire(DomainMessage domainMessage, Send send, FutureListener futureListener) {
		EventResultFuture eventMessageFuture = new EventResultFuture(send.value(), futureListener, domainMessage);
		eventMessageFuture.setAsyn(send.asyn());
		domainMessage.setEventResultHandler(eventMessageFuture);
		futureDirector.fire(domainMessage);

	}

	public void fire(DomainMessage domainMessage, Send send) {
		try {
			String topic = send.value();
			Disruptor disruptor = (Disruptor) topicDisruptors.get(topic);
			if (disruptor == null) {
				disruptor = disruptorFactory.getDisruptor(topic);
				if (disruptor == null) {
					Debug.logWarning("not create disruptor for " + topic, module);
					return;
				}
				topicDisruptors.put(topic, disruptor);
			}

			RingBuffer ringBuffer = disruptor.getRingBuffer();
			long sequence = ringBuffer.next();

			EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.get(sequence);
			if (eventDisruptor == null)
				return;
			eventDisruptor.setTopic(topic);
			eventDisruptor.setDomainMessage(domainMessage);
			ringBuffer.publish(sequence);
		} catch (Exception e) {
			Debug.logError("fire error: " + send.value() + " domainMessage:" + domainMessage.getEventSource(), module);
		}
	}

}
