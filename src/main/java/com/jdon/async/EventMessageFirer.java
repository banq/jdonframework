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
package com.jdon.async;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;

import com.jdon.annotation.model.Owner;
import com.jdon.annotation.model.Receiver;
import com.jdon.annotation.model.Send;
import com.jdon.async.disruptor.DisruptorFactory;
import com.jdon.async.disruptor.DisruptorForCommandFactory;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.async.future.EventResultFuture;
import com.jdon.async.future.FutureDirector;
import com.jdon.async.future.FutureListener;
import com.jdon.container.pico.Startable;
import com.jdon.controller.model.ModelUtil;
import com.jdon.domain.message.Command;
import com.jdon.domain.message.DomainMessage;
import com.jdon.domain.message.consumer.ModelConsumerMethodHolder;
import com.jdon.domain.model.injection.ModelProxyInjection;
import com.jdon.util.Debug;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class EventMessageFirer implements Startable {
	public final static String module = EventMessageFirer.class.getName();

	private DisruptorFactory disruptorFactory;
	private DisruptorForCommandFactory disruptorForCommandFactory;
	private FutureDirector futureDirector;
	private ModelProxyInjection modelProxyInjection;

	public EventMessageFirer(DisruptorFactory disruptorFactory, DisruptorForCommandFactory disruptorForCommandFactory, FutureDirector futureDirector,
			ModelProxyInjection modelProxyInjection) {
		super();
		this.disruptorFactory = disruptorFactory;
		this.disruptorForCommandFactory = disruptorForCommandFactory;
		this.futureDirector = futureDirector;
		this.modelProxyInjection = modelProxyInjection;
	}

	public void start() {

	}

	public void stop() {
		if (futureDirector != null) {
			futureDirector.stop();
			futureDirector = null;
		}
	}

	public void fire(DomainMessage domainMessage, Send send, FutureListener futureListener) {
		EventResultFuture eventMessageFuture = new EventResultFuture(send.value(), futureListener, domainMessage);
		eventMessageFuture.setAsyn(send.asyn());
		domainMessage.setEventResultHandler(eventMessageFuture);
		futureDirector.fire(domainMessage);

	}

	public void fire(DomainMessage domainMessage, Send send) {
		String topic = send.value();
		if (disruptorForCommandFactory.isContain(topic)) {
			return;
		}
		if (!disruptorFactory.isContain(topic)) {
			Debug.logError(" no found any consumer annonated with @Consumer or its methods with @OnEvent for topic=" + topic, module);
			return;
		}

		try {

			Disruptor disruptor = disruptorFactory.getDisruptor(topic);
			if (disruptor == null) {
				Debug.logWarning("not create disruptor for " + topic, module);
				return;
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
			Debug.logError("fire error: " + e.getMessage() + " for" + send.value() + " from:" + domainMessage.getEventSource() + " ", module);
		} finally {

		}
	}

	public void fireToModel(DomainMessage domainMessage, Send send, MethodInvocation invocation) {
		String topic = send.value();
		if (disruptorFactory.isContain(topic))
			return;
		ModelConsumerMethodHolder modelConsumerMethodHolder = disruptorForCommandFactory.getModelConsumerMethodHolder(topic);
		if (modelConsumerMethodHolder == null) {
			Debug.logError(" no found any consumer annonated with @OnCommand for topic=" + topic, module);
			return;
		}
		Object[] arguments = invocation.getArguments();
		if (arguments.length == 0) {
			Debug.logError("there is no event destination parameter(@Receiver) in this method:" + invocation.getMethod().getName() + topic, module);
			return;
		}

		Map params = fetchCommandReceiver(invocation.getMethod(), arguments);
		if (params.size() == 0 || !ModelUtil.isModel(params.get("Receiver"))) {
			Debug.logError(" there is no event destination parameter(@Receiver)  in this method:" + invocation.getMethod().getName()
					+ " or the destination class not annotated with @Model", module);
			return;
		}
		//
		modelProxyInjection.injectProperties(params.get("Receiver"));
		// target model is the owner of the disruptor, single thread to modify
		// aggregate root model's state.
		((Command) domainMessage).setDestination(params.get("Receiver"));

		Object owner = "System";
		if (params.containsKey("Owner")) {
			owner = params.get("Owner");
		}

		Disruptor disruptor = disruptorForCommandFactory.getDisruptor(topic, owner);
		if (disruptor == null) {
			Debug.logWarning("not create command disruptor for " + topic, module);
			return;
		}

		try {

			RingBuffer ringBuffer = disruptor.getRingBuffer();
			long sequence = ringBuffer.next();

			EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.get(sequence);
			if (eventDisruptor == null)
				return;
			eventDisruptor.setTopic(topic);
			eventDisruptor.setDomainMessage(domainMessage);
			ringBuffer.publish(sequence);

		} catch (Exception e) {
			Debug.logError("fireToModel error: " + send.value() + " domainMessage:" + domainMessage.getEventSource() + " mode:"
					+ arguments[0].getClass().getName(), module);
		} finally {

		}
	}

	private Map fetchCommandReceiver(Method method, Object[] arguments) {
		Map result = new HashMap();
		int i = 0;
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		for (Annotation[] anns : paramAnnotations) {
			Object parameter = arguments[i++];
			for (Annotation annotation : anns) {
				if (annotation instanceof Receiver) {
					result.put("Receiver", parameter);
					return result;
				} else if (annotation instanceof Owner) {
					result.put("Owner", parameter);
				}
			}
		}
		return result;
	}

}
