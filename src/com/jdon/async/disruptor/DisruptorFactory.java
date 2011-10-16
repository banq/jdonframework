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

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.jdon.async.disruptor.util.DisruptorWizard;
import com.jdon.async.disruptor.util.EventHandlerGroup;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.type.ConsumerLoader;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.domain.message.DomainEventDispatchHandler;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.consumer.ConsumerMethodHolder;
import com.jdon.util.Debug;
import com.lmax.disruptor.AbstractEvent;
import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;

public class DisruptorFactory implements EventFactory {
	public final static String module = DisruptorFactory.class.getName();
	protected final Map<String, TreeSet<DomainEventHandler>> handlesMap;
	private String RingBufferSize;

	private final ContainerWrapper containerWrapper;

	public DisruptorFactory(DisruptorParams disruptorParams, ContainerCallback containerCallback) {
		this.RingBufferSize = disruptorParams.getRingBufferSize();
		this.containerWrapper = containerCallback.getContainerWrapper();
		this.handlesMap = new ConcurrentHashMap<String, TreeSet<DomainEventHandler>>();
	}

	private DisruptorWizard createDw(int size) {
		if (size == 0)
			size = Integer.parseInt(RingBufferSize);
		return new DisruptorWizard<EventDisruptor>(this, size + 2, Executors.newCachedThreadPool(), ClaimStrategy.Option.SINGLE_THREADED,
				WaitStrategy.Option.YIELDING);
	}

	public DisruptorWizard addEventMessageHandler(String topic, TreeSet<DomainEventHandler> handlers) {
		if (handlers.size() == 0)
			return null;
		DisruptorWizard dw = createDw(handlers.size());
		EventHandlerGroup eh = null;
		for (DomainEventHandler handler : handlers) {
			if (eh == null) {
				eh = dw.handleEventsWith(handler);
			} else {
				eh = eh.handleEventsWith(handler);
			}
		}
		return dw;
	}

	public EventDisruptor getEventDisruptor(String topic) {
		TreeSet handlers = handlesMap.get(topic);
		if (handlers == null)// not inited
		{
			handlers = loadEvenHandler(topic);
			handlers = loadOnEventConsumers(topic, handlers);
			if (handlers.size() == 0) {
				// maybe in @Component(topicName)
				Object o = containerWrapper.lookup(topic);
				if (o == null) {
					Debug.logError("[Jdonframework]no found the class annotated with @Consumer(" + topic + ") ", module);
				}
				return null;
			}
			handlesMap.put(topic, handlers);
		}
		DisruptorWizard disruptorWizard = addEventMessageHandler(topic, handlers);
		if (disruptorWizard == null)
			return null;
		RingBuffer ringBuffer = disruptorWizard.start();
		EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.nextEvent();
		eventDisruptor.setRingBuffer(ringBuffer);
		return eventDisruptor;
	}

	/**
	 * if there are many consumers, execution order will be alphabetical list by
	 * Name of @Consumer class.
	 * 
	 * @param topic
	 * @return
	 */
	protected TreeSet<DomainEventHandler> loadEvenHandler(String topic) {
		TreeSet<DomainEventHandler> ehs = this.getTreeSet();
		Collection consumers = (Collection) containerWrapper.lookup(ConsumerLoader.TOPICNAME + topic);
		if (consumers == null || consumers.size() == 0) {
			Debug.logWarning("[Jdonframework]there is no any consumer class annotated with @Consumer(" + topic + ") ", module);
			return ehs;
		}
		for (Object o : consumers) {
			String consumerName = (String) o;
			DomainEventHandler eh = (DomainEventHandler) containerWrapper.getComponentNewInstance(consumerName);
			ehs.add(eh);
		}

		return ehs;

	}

	protected TreeSet<DomainEventHandler> loadOnEventConsumers(String topic, TreeSet<DomainEventHandler> ehs) {
		Collection consumerMethods = (Collection) containerWrapper.lookup(ConsumerLoader.TOPICNAME2 + topic);
		if (consumerMethods == null)
			return ehs;
		for (Object o : consumerMethods) {
			ConsumerMethodHolder consumerMethodHolder = (ConsumerMethodHolder) o;
			DomainEventDispatchHandler domainEventDispatchHandler = new DomainEventDispatchHandler(consumerMethodHolder, containerWrapper);
			ehs.add(domainEventDispatchHandler);
		}
		return ehs;

	}

	private TreeSet<DomainEventHandler> getTreeSet() {
		return new TreeSet(new Comparator() {
			public int compare(Object num1, Object num2) {
				String inum1, inum2;
				inum1 = num1.getClass().getName();
				inum2 = num2.getClass().getName();
				if (inum1.compareTo(inum2) < 1) {
					return -1; // returning the first object
				} else {

					return 1;
				}
			}

		});
	}

	@Override
	public AbstractEvent create() {
		return new EventDisruptor();

	}
}
