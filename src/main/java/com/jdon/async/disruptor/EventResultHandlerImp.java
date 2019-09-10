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

import java.util.concurrent.TimeUnit;

import com.jdon.async.EventResultHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;

public class EventResultHandlerImp implements EventResultHandler {

	// MILLISECONDS default is one seconds
	protected ValueEventProcessor valueEventProcessor;

	public EventResultHandlerImp(int timeoutforeturnResult) {
		super();
		RingBuffer ringBuffer = RingBuffer.createSingleProducer(new EventResultFactory(), 1, new TimeoutBlockingWaitStrategy(timeoutforeturnResult,
				TimeUnit.MILLISECONDS));
		this.valueEventProcessor = new ValueEventProcessor(ringBuffer);

	}

	/**
	 * send event result
	 * 
	 */
	public void send(Object result) {
		valueEventProcessor.send(result);
	}

	public Object get() {
		Object result = null;
		EventResultDisruptor ve = valueEventProcessor.waitFor();
		if (ve != null) {
			result = ve.getValue();
			ve.clear();
			// clear();
		}
		return result;

	}

	public Object getBlockedValue() {
		Object result = null;
		EventResultDisruptor ve = valueEventProcessor.waitForBlocking();
		if (ve != null) {
			result = ve.getValue();
			ve.clear();
			// clear();
		}
		return result;
	}

	public void clear() {
		valueEventProcessor.clear();
	}

	/**
	 * deprecated since 6.6.8
	 */
	public void setWaitforTimeout(int timeoutforeturnResult) {
		System.err.print("deprecated since 6.6.8");
	}

}
