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
package com.jdon.domain.message;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.jdon.annotation.model.Send;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

/**
 * 1. create dynamic proxy for a Model in DomainCacheInterceptor.
 * 
 * 
 * 2. intercepte the method with @send
 * 
 * 3. @Channel will accept the message;
 * 
 * @author banq
 * 
 */
public class MessageInterceptor implements MethodInterceptor {
	public final static String module = MessageInterceptor.class.getName();

	private ContainerCallback containerCallback;
	private MessageMediator messageMediator;

	public MessageInterceptor(ContainerCallback containerCallback, MessageMediator messageMediator) {
		super();
		this.containerCallback = containerCallback;
		this.messageMediator = messageMediator;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (!invocation.getMethod().isAnnotationPresent(Send.class))
			return invocation.proceed();

		Send send = invocation.getMethod().getAnnotation(Send.class);
		String channel = send.value();
		boolean asyn = send.asyn();
		Object result = null;
		try {
			Object listener = containerCallback.getContainerWrapper().lookup(channel);
			if (listener == null) {
				Debug.logError("you must define a class with @Component(" + channel + ") to listern " + invocation.getThis(), module);
				return invocation.proceed();
			} else if (!(listener instanceof MessageListener)) {
				Debug.logError("you must define a class implements  com.jdon.domain.message.MessageListener to listern" + invocation.getThis(),
						module);
				return invocation.proceed();
			}

			result = invocation.proceed();

			DomainMessage message = null;
			if (result == null) {
				message = new DomainMessage(result);
			} else if (!(DomainMessage.class.isAssignableFrom(result.getClass()))) {
				Debug.logError("your method that with @Send must defines return type is com.jdon.domain.message.DomainMessage  :"
						+ invocation.getThis(), module);
				return result;
			} else
				message = (DomainMessage) result;

			message.setChannel(channel);
			message.setAsyn(asyn);
			message.setMessageListener((MessageListener) listener);
			messageMediator.sendMessage(message);
		} catch (Exception e) {
			Debug.logError("invoke error: " + e, module);
		}
		return result;
	}
}
