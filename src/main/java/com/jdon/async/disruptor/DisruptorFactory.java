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

import com.jdon.async.disruptor.pool.DisruptorPoolFactory;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.type.ConsumerLoader;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.pico.Startable;
import com.jdon.domain.message.DomainEventDispatchHandler;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.consumer.ConsumerMethodHolder;
import com.jdon.util.Debug;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

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
public class DisruptorFactory implements Startable {
	public final static String module = DisruptorFactory.class.getName();
	protected final ConcurrentHashMap<String, TreeSet<DomainEventHandler>> handlesMap;

	private String RingBufferSize;

	private ContainerWrapper containerWrapper;

	private DisruptorPoolFactory disruptorPoolFactory;

	public DisruptorFactory(DisruptorParams disruptorParams, ContainerCallback containerCallback, DisruptorPoolFactory disruptorPoolFactory) {
		this.RingBufferSize = disruptorParams.getRingBufferSize();
		this.containerWrapper = containerCallback.getContainerWrapper();
		this.handlesMap = new ConcurrentHashMap<String, TreeSet<DomainEventHandler>>();
		this.disruptorPoolFactory = disruptorPoolFactory;
		this.disruptorPoolFactory.setDisruptorFactory(this);

	}

	public DisruptorFactory() {
		// @todo configure in xml
		this.RingBufferSize = "8";
		this.containerWrapper = null;
		this.handlesMap = new ConcurrentHashMap<String, TreeSet<DomainEventHandler>>();
		this.disruptorPoolFactory = new DisruptorPoolFactory();
		this.disruptorPoolFactory.setDisruptorFactory(this);
	}

	public Disruptor createDw(String topic) {
		int size = Integer.parseInt(RingBufferSize);
		return new Disruptor(new EventDisruptorFactory(), size, Executors.newCachedThreadPool());
	}

	public Disruptor createSingleDw(String topic) {
		int size = Integer.parseInt(RingBufferSize);
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		return new Disruptor(new EventDisruptorFactory(), size, Executors.newCachedThreadPool(), ProducerType.SINGLE, waitStrategy);
	}

	public Disruptor addEventMessageHandler(Disruptor dw, String topic, TreeSet<DomainEventHandler> handlers) {
		if (handlers.size() == 0)
			return null;
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

	public Disruptor getDisruptor(String topic) {
		return this.disruptorPoolFactory.getDisruptor(topic);
	}

	public void releaseDisruptor(Object owner) {

	}

	/**
	 * one topic one EventDisruptor
	 * 
	 * @param topic
	 * @return
	 */
	public Disruptor createDisruptor(String topic) {
		TreeSet handlers = getHandles(topic);
		if (handlers == null)
			return null;

		Disruptor dw = createDw(topic);
		Disruptor disruptor = addEventMessageHandler(dw, topic, handlers);
		if (disruptor == null)
			return null;
		disruptor.start();
		return disruptor;
	}

	/**
	 * single producer :single consumer
	 * 
	 * no lock
	 * 
	 * @param topic
	 * @return
	 */
	public Disruptor createSingleDisruptor(String topic) {
		TreeSet handlers = getHandles(topic);
		if (handlers == null)
			return null;
		Disruptor dw = createSingleDw(topic);
		Disruptor disruptor = addEventMessageHandler(dw, topic, handlers);
		if (disruptor == null)
			return null;
		disruptor.start();
		return disruptor;
	}

	private TreeSet getHandles(String topic) {
		TreeSet handlersExist = handlesMap.get(topic);
		TreeSet handlersNew = null;
		if (handlersExist == null)// not inited
		{
			handlersNew = getTreeSet();
			handlersNew.addAll(loadEvenHandler(topic));
			handlersNew.addAll(loadOnEventConsumers(topic));
			if (handlersNew.size() == 0) {
				// maybe by mistake in @Component(topicName)
				Object o = containerWrapper.lookup(topic);
				if (o == null) {
					Debug.logError("[Jdonframework]no found the class annotated with @Consumer(" + topic + ") ", module);
				}
				return null;
			}
			handlersExist = handlesMap.putIfAbsent(topic, handlersNew);
		}
		return handlersExist != null?handlersExist:handlersNew;
	}

	public boolean isContain(String topic) {
		if (containerWrapper.lookup(ConsumerLoader.TOPICNAME + topic) == null && containerWrapper.lookup(ConsumerLoader.TOPICNAME2 + topic) == null) {
			return false;
		} else
			return true;

	}

	/**
	 * if there are many consumers, execution order will be alphabetical list by
	 * Name of @Consumer class.
	 * 
	 * @param topic
	 * @return
	 */
	protected Collection loadEvenHandler(String topic) {
		Collection ehs = new ArrayList();
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

	protected Collection loadOnEventConsumers(String topic) {
		Collection ehs = new ArrayList();
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
				if (num1 instanceof DomainEventDispatchHandler) {
					inum1 = ((DomainEventDispatchHandler) num1).getSortName();
				} else {
					inum1 = num1.getClass().getName();
				}
				if (num2 instanceof DomainEventDispatchHandler) {
					inum2 = ((DomainEventDispatchHandler) num2).getSortName();
				} else {
					inum2 = num2.getClass().getName();
				}
				if (inum1.compareTo(inum2) < 1) {
					return -1; // returning the first object
				} else {

					return 1;
				}
			}

		});
	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		this.containerWrapper = null;
		this.handlesMap.clear();
		this.RingBufferSize = null;

	}
}
