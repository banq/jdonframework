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

import java.util.concurrent.TimeUnit;

import com.jdon.async.EventMessage;
import com.jdon.domain.message.DomainMessage;
import com.lmax.disruptor.AbstractEvent;
import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.DependencyBarrier;
import com.lmax.disruptor.RingBuffer;

public class EventDisruptor extends AbstractEvent implements EventMessage {

	protected String topic;

	protected DomainMessage domainMessage;

	protected boolean over;

	protected Object eventResult;

	protected Object eventReturnResult;

	// MILLISECONDS default is one seconds
	protected int timeoutforeturnResult = 10000;

	protected RingBuffer<EventDisruptor> ringBuffer;

	public Object getEventResult() {
		if (over)
			return eventResult;
		try {
			EventDisruptor resultEvent = fetchResultEvent();
			if (resultEvent != null)
				eventResult = resultEvent.getEventReturnResult();
			over = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventResult;
	}

	public Object getBlockedEventResult() {
		if (over)
			return eventResult;
		try {
			EventDisruptor resultEvent = fetchBlockResultEvent();
			if (resultEvent != null)
				eventResult = resultEvent.getEventReturnResult();
			over = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return eventResult;
	}

	protected EventDisruptor fetchResultEvent() {
		EventDisruptor resultEvent = null;
		try {
			DependencyBarrier dependencyBarrier = ringBuffer.newDependencyBarrier();
			long nextSequence = this.getSequence() + 1L;
			final long availableSequence = dependencyBarrier.waitFor(nextSequence, timeoutforeturnResult, TimeUnit.MILLISECONDS);
			if (availableSequence == nextSequence) {
				resultEvent = ringBuffer.getEvent(availableSequence);
			}
		} catch (AlertException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return resultEvent;

	}

	protected EventDisruptor fetchBlockResultEvent() {
		EventDisruptor resultEvent = null;
		try {
			DependencyBarrier dependencyBarrier = ringBuffer.newDependencyBarrier();
			long nextSequence = this.getSequence() + 1L;
			final long availableSequence = dependencyBarrier.waitFor(nextSequence);
			if (availableSequence == nextSequence) {
				resultEvent = ringBuffer.getEvent(availableSequence);
			}
		} catch (AlertException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return resultEvent;

	}

	public void setEventResult(Object result) {
		EventDisruptor eventResult = ringBuffer.nextEvent();
		eventResult.setEventReturnResult(result);
		ringBuffer.publish(eventResult);
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

	public void setRingBuffer(RingBuffer<EventDisruptor> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public Object getEventReturnResult() {
		return eventReturnResult;
	}

	public void setEventReturnResult(Object eventReturnResult) {
		this.eventReturnResult = eventReturnResult;
	}

	public void publish() {
		ringBuffer.publish(this);
	}

	public int getTimeoutforeturnResult() {
		return timeoutforeturnResult;
	}

	public void setTimeoutforeturnResult(int timeoutforeturnResult) {
		this.timeoutforeturnResult = timeoutforeturnResult;
	}

}
