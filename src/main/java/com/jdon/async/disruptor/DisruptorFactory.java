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

import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.type.ConsumerLoader;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.pico.Startable;
import com.jdon.domain.message.DomainEventDispatchHandler;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.consumer.ConsumerMethodHolder;
import com.jdon.util.Debug;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ClaimStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.MultiThreadedClaimStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;

/**
 * SLEEPING is a better option when you have a large number of event processors
 * and you need throughput when you don't mind a 1ms latency hit in the worse
 * case. BLOCKING has the lowest throughput of all the strategies but it does
 * not have the 1ms latency spikes of SLEEPING. It uses no CPU when idle but it
 * does not scale up so well with increasing numbers of event processors because
 * of the contention on the lock. YIELDING and BUSY_SPIN have the best
 * performance for both throughput and latency but eat up a CPU. YIELDING is
 * more friendly in allowing other threads to run when cores are limited. It
 * would be nice if Java had access to the x86 PAUSE instruction to save power
 * and further reduce latency that gets lost due to the wrong choices the CPU
 * can make with speculative execution of busy spin loops. In all cases where
 * you have sufficient cores then all the wait strategies will beat pretty much
 * any other alternative such as queues.
 * 
 * @author banq
 * 
 */
public class DisruptorFactory implements EventFactory, Startable {
	public final static String module = DisruptorFactory.class.getName();
	protected final Map<String, TreeSet<DomainEventHandler>> handlesMap;

	private String RingBufferSize;

	private ContainerWrapper containerWrapper;

	public DisruptorFactory(DisruptorParams disruptorParams, ContainerCallback containerCallback) {
		this.RingBufferSize = disruptorParams.getRingBufferSize();
		this.containerWrapper = containerCallback.getContainerWrapper();
		this.handlesMap = new ConcurrentHashMap<String, TreeSet<DomainEventHandler>>();

	}

	public DisruptorFactory() {
		// @todo configure in xml
		this.RingBufferSize = "8";
		this.containerWrapper = null;
		this.handlesMap = new ConcurrentHashMap<String, TreeSet<DomainEventHandler>>();
	}

	private Disruptor createDw(String topic) {
		// executorService = Executors.newFixedThreadPool(100);
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		ClaimStrategy claimStrategy = new MultiThreadedClaimStrategy(Integer.parseInt(RingBufferSize));
		return new Disruptor(this, Executors.newCachedThreadPool(), claimStrategy, waitStrategy);
	}

	public Disruptor addEventMessageHandler(String topic, TreeSet<DomainEventHandler> handlers) {
		if (handlers.size() == 0)
			return null;
		Disruptor dw = createDw(topic);
		EventHandlerGroup eh = null;
		for (DomainEventHandler handler : handlers) {
			DomainEventHandlerAdapter dea = new DomainEventHandlerAdapter(handler);
			if (eh == null) {
				eh = dw.handleEventsWith(dea);
			} else {
				eh = eh.handleEventsWith(dea);
			}
		}
		return dw;
	}

	/**
	 * one event one EventDisruptor
	 * 
	 * @param topic
	 * @return
	 */
	public Disruptor createDisruptor(String topic) {
		TreeSet handlers = handlesMap.get(topic);
		if (handlers == null)// not inited
		{
			handlers = loadEvenHandler(topic);
			handlers = loadOnEventConsumers(topic, handlers);
			if (handlers.size() == 0) {
				// maybe by mistake in @Component(topicName)
				Object o = containerWrapper.lookup(topic);
				if (o == null) {
					Debug.logError("[Jdonframework]no found the class annotated with @Consumer(" + topic + ") ", module);
				}
				return null;
			}
			handlesMap.put(topic, handlers);
		}
		Disruptor disruptor = addEventMessageHandler(topic, handlers);
		if (disruptor == null)
			return null;
		disruptor.start();
		return disruptor;
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
		Collection<String> consumers = (Collection<String>) containerWrapper.lookup(ConsumerLoader.TOPICNAME + topic);
		if (consumers == null || consumers.size() == 0) {
			Debug.logWarning("[Jdonframework]there is no any consumer class annotated with @Consumer(" + topic + ") ", module);
			return ehs;
		}
		for (String consumerName : consumers) {
			DomainEventHandler eh = (DomainEventHandler) containerWrapper.lookup(consumerName);
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

	public TreeSet<DomainEventHandler> getTreeSet() {
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

	// create a Event;
	public EventDisruptor newInstance() {
		return new EventDisruptor();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.containerWrapper = null;
		this.handlesMap.clear();
		this.RingBufferSize = null;

	}
}
