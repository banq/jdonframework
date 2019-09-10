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

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;

public class ValueEventProcessor {

	protected final RingBuffer<EventResultDisruptor> ringBuffer;

	private long waitAtSequence = 0;

	public ValueEventProcessor(RingBuffer<EventResultDisruptor> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void send(Object result) {

		waitAtSequence = ringBuffer.next();
		EventResultDisruptor ve = ringBuffer.get(waitAtSequence);
		ve.setValue(result);
		ringBuffer.publish(waitAtSequence);
	}

	public EventResultDisruptor waitFor() {
		SequenceBarrier barrier = ringBuffer.newBarrier();
		try {
			long a = barrier.waitFor(waitAtSequence);
			if (ringBuffer != null)
				return ringBuffer.get(a);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			barrier.alert();
		}
		return null;
	}

	/**
	 * not really block, the waiting time is longer than not block.
	 */
	public EventResultDisruptor waitForBlocking() {
		SequenceBarrier barrier = ringBuffer.newBarrier();
		try {
			long a = barrier.waitFor(waitAtSequence);
			if (ringBuffer != null)
				return ringBuffer.get(a);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			barrier.alert();
		}
		return null;
	}

	public long getWaitAtSequence() {
		return waitAtSequence;
	}

	public void setWaitAtSequence(long waitAtSequence) {
		this.waitAtSequence = waitAtSequence;
	}

	public void clear() {

	}

	public RingBuffer<EventResultDisruptor> getRingBuffer() {
		return ringBuffer;
	}

}
