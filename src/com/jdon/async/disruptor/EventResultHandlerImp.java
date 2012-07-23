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

import com.jdon.async.EventResultHandler;
import com.jdon.domain.message.DomainMessage;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SingleThreadedClaimStrategy;

public class EventResultHandlerImp implements EventResultHandler {

	protected String topic;

	protected DomainMessage domainMessage;

	protected volatile boolean over;

	protected Object result;

	// MILLISECONDS default is one seconds
	protected int timeoutforeturnResult = 10000;

	protected ValueEventProcessor valueEventProcessor;

	public EventResultHandlerImp(String topic, DomainMessage domainMessage) {
		super();
		this.topic = topic;
		this.domainMessage = domainMessage;
		RingBuffer ringBuffer = new RingBuffer<EventResultDisruptor>(EventResultDisruptor.EVENT_FACTORY, new SingleThreadedClaimStrategy(1),
				new BlockingWaitStrategy());
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
		if (over)
			return result;
		else
			return fecthResult();
	}

	private synchronized Object fecthResult() {
		if (over)
			return result;
		EventResultDisruptor ve = valueEventProcessor.waitFor(timeoutforeturnResult);
		if (ve != null) {
			result = ve.getValue();
			setOver(true);
			ve.clear();
			clear();
		}
		return result;

	}

	public Object getBlockedValue() {
		if (over)
			return result;
		else
			return fecthBlockingResult();
	}

	private synchronized Object fecthBlockingResult() {
		if (over)
			return result;
		EventResultDisruptor ve = valueEventProcessor.waitForBlocking();
		if (ve != null) {
			result = ve.getValue();
			setOver(true);
			ve.clear();
			clear();
		}
		return result;

	}

	public void clear() {
		valueEventProcessor.clear();
		valueEventProcessor = null;
	}

	public String getTopic() {
		return topic;
	}

	public int getTimeoutforeturnResult() {
		return timeoutforeturnResult;
	}

	public void setWaitforTimeout(int timeoutforeturnResult) {
		this.timeoutforeturnResult = timeoutforeturnResult;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

}
