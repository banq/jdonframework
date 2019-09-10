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
package com.jdon.async.disruptor.pool;

import com.jdon.async.disruptor.DomainEventHandlerAdapter;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainEventHandler;

public class DomainEventHandlerDecorator extends DomainEventHandlerAdapter {
	private DisruptorSwitcher disruptorSwitcher;

	public DomainEventHandlerDecorator(DomainEventHandler handler) {
		super(handler);
		this.disruptorSwitcher = new DisruptorSwitcher();
	}

	public void onEvent(EventDisruptor event, long sequence, boolean endOfBatch) throws Exception {
		try {
			disruptorSwitcher.setCommandTopic(event.getTopic());
			super.onEvent(event, sequence, endOfBatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
