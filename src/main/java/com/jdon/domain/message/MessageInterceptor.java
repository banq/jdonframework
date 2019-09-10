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
package com.jdon.domain.message;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.annotation.model.Send;
import com.jdon.async.EventMessageFirer;
import com.jdon.async.future.FutureListener;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;

/**
 * this is for domain model, there is another for components/services
 * com.jdon.aop.interceptor.ComponentMessageInterceptor
 * 
 * useage see:com.jdon.sample.test.domain
 * 
 * 1. create dynamic proxy for event Model in DomainCacheInterceptor.
 * 
 * 
 * 2. intercepte the method with @send
 * 
 * 3. @Channel will accept the message;
 * 
 * @author banq
 * 
 */
public class MessageInterceptor implements MethodInterceptor, Startable {
	public final static String module = MessageInterceptor.class.getName();

	private ContainerCallback containerCallback;
	protected EventMessageFirer eventMessageFirer;

	public MessageInterceptor(ContainerCallback containerCallback, EventMessageFirer eventMessageFirer) {
		super();
		this.containerCallback = containerCallback;
		this.eventMessageFirer = eventMessageFirer;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (!invocation.getMethod().isAnnotationPresent(Send.class))
			return invocation.proceed();

		Send send = invocation.getMethod().getAnnotation(Send.class);
		String channel = send.value();
		Object result = null;
		try {

			result = invocation.proceed();

			DomainMessage message = null;
			if (DomainMessage.class.isAssignableFrom(result.getClass())) {
				message = (DomainMessage) result;
			} else {
				message = new DomainMessage(result);
			}
			eventMessageFirer.fire(message, send);

			// older queue @Send(myChannl) ==> @Component(myChannl)
			Object listener = containerCallback.getContainerWrapper().lookup(channel);
			if (listener != null && listener instanceof FutureListener)
				eventMessageFirer.fire(message, send, (FutureListener) listener);

			eventMessageFirer.fireToModel(message, send, invocation);

		} catch (Exception e) {
			Debug.logError("invoke error: " + e, module);
		}
		return result;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.containerCallback = null;
		this.eventMessageFirer = null;

	}
}
