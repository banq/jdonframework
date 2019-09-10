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

import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.container.ContainerWrapper;
import com.jdon.domain.message.consumer.ConsumerMethodHolder;
import com.jdon.util.Debug;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * action by annotation "@onEvent("topicName")"
 * 
 * 
 * @author banq
 * 
 */
public class DomainEventDispatchHandler implements DomainEventHandler<EventDisruptor> {
	public final static String module = DomainEventDispatchHandler.class.getName();

	private ConsumerMethodHolder consumerMethodHolder;
	private ContainerWrapper containerWrapper;

	public DomainEventDispatchHandler(ConsumerMethodHolder consumerMethodHolder, ContainerWrapper containerWrapper) {
		super();
		this.consumerMethodHolder = consumerMethodHolder;
		this.containerWrapper = containerWrapper;
	}

	public String getSortName() {
		Object o = containerWrapper.lookupOriginal(consumerMethodHolder.getClassName());
		return o.getClass().getName();
	}

	@Override
	public void onEvent(EventDisruptor event, final boolean endOfBatch) throws Exception {
		try {
			Method method = consumerMethodHolder.getMethod();
			Class[] pTypes = method.getParameterTypes();
			if (pTypes.length == 0) {
				Object o = containerWrapper.lookupOriginal(consumerMethodHolder.getClassName());
				method.invoke(o, new Object[] {});
			}
			Object parameter = event.getDomainMessage().getEventSource();
			if (parameter == null) {
				Debug.logError("[Jdonframework]DomainMessage's EventSource is null, need " + pTypes[0].getName(), module);
				return;
			}

			Object[] parameters = new Object[pTypes.length];
			int i = 0;
			for (Class pType : pTypes) {
				if (pType.isAssignableFrom(parameter.getClass())) {
					parameters[i] = parameter;
				} else {
					// init other parameter to event instance;
					if (!pType.isPrimitive())
						parameters[i] = pType.newInstance();
					else
						parameters[i] = defaultValues.get(pType);
				}
				i++;
			}
			Object o = containerWrapper.lookupOriginal(consumerMethodHolder.getClassName());
			Object eventResult = method.invoke(o, parameters);
			event.getDomainMessage().setEventResult(eventResult);
		} catch (Exception e) {
			Debug.logError("[Jdonframework]" + consumerMethodHolder.getClassName() + " method with @onEvent error: " + e, module);
		}

	}

	private final static Map<Class<?>, Object> defaultValues = new HashMap<Class<?>, Object>();
	static {
		defaultValues.put(String.class, "");
		defaultValues.put(Integer.class, 0);
		defaultValues.put(int.class, 0);
		defaultValues.put(Long.class, 0L);
		defaultValues.put(long.class, 0L);
		defaultValues.put(Character.class, '\0');
		defaultValues.put(char.class, '\0');
		// etc
	}

}
