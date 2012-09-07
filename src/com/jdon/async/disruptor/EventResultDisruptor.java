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

import com.lmax.disruptor.EventFactory;

/**
 * 
 * A Consumer send response back to the Subscriber by this value object
 * 
 * @author banq
 * 
 */
public class EventResultDisruptor {

	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	public final static EventFactory<EventResultDisruptor> EVENT_FACTORY = new EventFactory<EventResultDisruptor>() {
		public EventResultDisruptor newInstance() {
			return new EventResultDisruptor();
		}
	};

	public void clear() {
		value = null;
	}

	public void finalize() {
		clear();
	}
}
