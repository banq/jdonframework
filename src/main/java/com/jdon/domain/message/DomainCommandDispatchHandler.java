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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.consumer.ModelConsumerMethodHolder;
import com.jdon.util.Debug;

public class DomainCommandDispatchHandler implements DomainEventHandler<EventDisruptor> {
	public final static String module = DomainCommandDispatchHandler.class.getName();

	private ModelConsumerMethodHolder modelConsumerMethodHolder;

	public DomainCommandDispatchHandler(ModelConsumerMethodHolder modelConsumerMethodHolder) {
		super();
		this.modelConsumerMethodHolder = modelConsumerMethodHolder;
	}

	@Override
	public void onEvent(EventDisruptor event, final boolean endOfBatch) throws Exception {
		try {
			Method method = modelConsumerMethodHolder.getConsumerMethodHolder().getMethod();
			Object model = ((Command) event.getDomainMessage()).getDestination();
			if (model == null) {
				Debug.logError("[Jdonframework]Destination that will be sent is null ", module);
				return;
			}
			Class[] pTypes = method.getParameterTypes();
			if (pTypes.length == 0) {
				method.invoke(model, new Object[] {});
			}
			Object parameter = event.getDomainMessage().getEventSource();
			if (parameter == null) {
				Debug.logError("[Jdonframework]the publisher method with @Send need return type" + pTypes[0].getName(), module);
				return;
			}

			Object[] parameters = new Object[pTypes.length];
			int i = 0;
			for (Class pType : pTypes) {
				if (pType.isAssignableFrom(parameter.getClass())) {
					parameters[i] = parameter;
				} else {
					// init other parameter to event instance;
					if (!pType.isPrimitive()) {
						try {
							parameters[i] = pType.newInstance();
						} catch (Exception e) {
							Debug.logError("[Jdonframework] " + pType.getName() + " no default construtor :" + e, module);
							e.printStackTrace();
						}
					} else
						parameters[i] = defaultValues.get(pType);
				}
				i++;
			}
			Object eventResult = method.invoke(model, parameters);
			event.getDomainMessage().setEventResult(eventResult);
		} catch (Exception e) {
			Debug.logError("[Jdonframework]" + modelConsumerMethodHolder.getConsumerMethodHolder().getClassName()
					+ " method with @onCommand  happended error: " + e, module);
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
