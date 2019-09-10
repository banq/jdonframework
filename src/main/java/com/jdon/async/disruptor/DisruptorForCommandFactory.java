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

import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import com.jdon.async.disruptor.pool.DisruptorCommandPoolFactory;
import com.jdon.async.disruptor.pool.DomainCommandHandlerFirst;
import com.jdon.async.disruptor.pool.DomainEventHandlerDecorator;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.type.ModelConsumerLoader;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.pico.Startable;
import com.jdon.domain.message.DomainCommandDispatchHandler;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.consumer.ModelConsumerMethodHolder;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;

public class DisruptorForCommandFactory implements Startable {
	public final static String module = DisruptorForCommandFactory.class.getName();
	protected final Map<String, TreeSet<DomainEventHandler>> handlesMap;

	private ContainerWrapper containerWrapper;

	private DisruptorCommandPoolFactory disruptorCommandPoolFactory;

	private DisruptorFactory disruptorFactory;

	public DisruptorForCommandFactory(DisruptorParams disruptorParams, ContainerCallback containerCallback,
			DisruptorCommandPoolFactory disruptorCommandPoolFactory, DisruptorFactory disruptorFactory) {
		this.containerWrapper = containerCallback.getContainerWrapper();
		this.handlesMap = new ConcurrentHashMap<String, TreeSet<DomainEventHandler>>();
		this.disruptorCommandPoolFactory = disruptorCommandPoolFactory;
		this.disruptorCommandPoolFactory.setDisruptorForCommandFactory(this);
		this.disruptorFactory = disruptorFactory;
	}

	public Disruptor getDisruptor(String topic, Object target) {
		return this.disruptorCommandPoolFactory.getDisruptor(topic, target);
	}

	public void releaseDisruptor(Object owner) {

	}

	private Disruptor createDw(String topic) {
		return disruptorFactory.createDw(topic);
	}

	private Disruptor createDisruptorWithEventHandler(String topic) {
		TreeSet<DomainEventHandler> handlers = handlesMap.get(topic);
		if (handlers == null)// not inited
		{
			handlers = this.getTreeSet();
			handlers = loadOnCommandConsumers(topic, handlers);
			handlesMap.put(topic, handlers);
		}
		if (handlers.isEmpty())
			return null;

		Disruptor dw = createDw(topic);
		EventHandlerGroup eh = dw.handleEventsWith(new DomainCommandHandlerFirst(this));

		for (DomainEventHandler handler : handlers) {
			DomainEventHandlerAdapter dea = new DomainEventHandlerDecorator(handler);
			eh = eh.handleEventsWith(dea);
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

		Disruptor disruptor = createDisruptorWithEventHandler(topic);
		if (disruptor != null)
			disruptor.start();
		return disruptor;
	}

	public boolean isContain(String topic) {
		if (containerWrapper.lookup(ModelConsumerLoader.TOPICNAME2 + topic) == null) {
			return false;
		} else
			return true;
	}

	public ModelConsumerMethodHolder getModelConsumerMethodHolder(String topic) {
		return (ModelConsumerMethodHolder) containerWrapper.lookup(ModelConsumerLoader.TOPICNAME2 + topic);
	}

	protected TreeSet<DomainEventHandler> loadOnCommandConsumers(String topic, TreeSet<DomainEventHandler> ehs) {
		ModelConsumerMethodHolder modelConsumerMethodHolder = getModelConsumerMethodHolder(topic);
		if (modelConsumerMethodHolder == null)
			return ehs;
		DomainCommandDispatchHandler domainCommandDispatchHandler = new DomainCommandDispatchHandler(modelConsumerMethodHolder);
		ehs.add(domainCommandDispatchHandler);
		return ehs;

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.containerWrapper = null;
		this.handlesMap.clear();

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
}
