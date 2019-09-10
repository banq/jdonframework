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

import com.jdon.async.disruptor.DisruptorForCommandFactory;
import com.jdon.async.disruptor.EventDisruptor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LifecycleAware;

public class DomainCommandHandlerFirst implements EventHandler<EventDisruptor>, LifecycleAware {

	private DisruptorForCommandFactory disruptorForCommandFactory;
	private DisruptorSwitcher disruptorSwitcher;

	public DomainCommandHandlerFirst(DisruptorForCommandFactory disruptorForCommandFactory) {
		super();
		this.disruptorSwitcher = new DisruptorSwitcher();
		this.disruptorForCommandFactory = disruptorForCommandFactory;
	}

	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(EventDisruptor event, long arg1, boolean arg2) throws Exception {

	}
}
