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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jdon.async.EventMessage;
import com.jdon.domain.message.DomainMessage;
import com.lmax.disruptor.AbstractEvent;
import com.lmax.disruptor.RingBuffer;

public class EventDisruptor extends AbstractEvent implements EventMessage {

	protected String topic;

	protected DomainMessage domainMessage;

	protected boolean over;

	protected Object eventResult;

	protected BlockingQueue resultQueue;

	protected RingBuffer ringBuffer;

	public EventDisruptor() {
		resultQueue = new LinkedBlockingQueue();
	}

	public Object getEventResult() {
		if (over)
			return eventResult;
		try {
			eventResult = resultQueue.take();
			over = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventResult;
	}

	public void setEventResult(Object result) {
		try {
			resultQueue.put(result);
		} catch (InterruptedException e) {
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public DomainMessage getDomainMessage() {
		return domainMessage;
	}

	public void setDomainMessage(DomainMessage domainMessage) {
		this.domainMessage = domainMessage;
	}

	public RingBuffer getRingBuffer() {
		return ringBuffer;
	}

	public void setRingBuffer(RingBuffer ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

}
