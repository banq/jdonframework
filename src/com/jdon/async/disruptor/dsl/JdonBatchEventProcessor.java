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
package com.jdon.async.disruptor.dsl;

import java.util.concurrent.atomic.AtomicBoolean;

import com.lmax.disruptor.AlertException;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventProcessor;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.FatalExceptionHandler;
import com.lmax.disruptor.LifecycleAware;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.Sequencer;

/**
 * add a RingBuffer clear for memory leak than old BatchEventProcessor
 * 
 * @author banq
 * 
 * @param <T>
 */
public class JdonBatchEventProcessor<T> implements EventProcessor {
	private final AtomicBoolean running = new AtomicBoolean(false);
	private ExceptionHandler exceptionHandler = new FatalExceptionHandler();
	private RingBuffer<T> ringBuffer;
	private SequenceBarrier sequenceBarrier;
	private EventHandler<T> eventHandler;
	private Sequence sequence = new Sequence(Sequencer.INITIAL_CURSOR_VALUE);

	/**
	 * Construct a {@link EventProcessor} that will automatically track the
	 * progress by updating its sequence when the
	 * {@link EventHandler#onEvent(Object, long, boolean)} method returns.
	 * 
	 * @param ringBuffer
	 *            to which events are published.
	 * @param sequenceBarrier
	 *            on which it is waiting.
	 * @param eventHandler
	 *            is the delegate to which events are dispatched.
	 */
	public JdonBatchEventProcessor(final RingBuffer<T> ringBuffer, final SequenceBarrier sequenceBarrier, final EventHandler<T> eventHandler) {
		this.ringBuffer = ringBuffer;
		this.sequenceBarrier = sequenceBarrier;
		this.eventHandler = eventHandler;

		if (eventHandler instanceof SequenceReportingEventHandler) {
			((SequenceReportingEventHandler<?>) eventHandler).setSequenceCallback(sequence);
		}
	}

	@Override
	public Sequence getSequence() {
		return sequence;
	}

	@Override
	public void halt() {
		running.set(false);
		sequenceBarrier.alert();
		this.ringBuffer = null;
		this.sequence = null;
		this.sequenceBarrier = null;
	}

	/**
	 * Set a new {@link ExceptionHandler} for handling exceptions propagated out
	 * of the {@link BatchEventProcessor}
	 * 
	 * @param exceptionHandler
	 *            to replace the existing exceptionHandler.
	 */
	public void setExceptionHandler(final ExceptionHandler exceptionHandler) {
		if (null == exceptionHandler) {
			throw new NullPointerException();
		}

		this.exceptionHandler = exceptionHandler;
	}

	/**
	 * It is ok to have another thread rerun this method after a halt().
	 */
	@Override
	public void run() {
		if (!running.compareAndSet(false, true)) {
			throw new IllegalStateException("Thread is already running");
		}
		sequenceBarrier.clearAlert();

		notifyStart();

		T event = null;
		long nextSequence = sequence.get() + 1L;
		while (running.get()) {
			try {
				final long availableSequence = sequenceBarrier.waitFor(nextSequence);
				while (nextSequence <= availableSequence) {
					event = ringBuffer.get(nextSequence);
					eventHandler.onEvent(event, nextSequence, nextSequence == availableSequence);
					nextSequence++;
				}

				sequence.set(nextSequence - 1L);
			} catch (final AlertException ex) {
				if (!running.get()) {
					break;
				}
			} catch (final Throwable ex) {
				ex.printStackTrace();
				exceptionHandler.handleEventException(ex, nextSequence, event);
				sequence.set(nextSequence);
				running.set(false);
				break;
			}
		}

		notifyShutdown();

		running.set(false);
	}

	private void notifyStart() {
		if (eventHandler instanceof LifecycleAware) {
			try {
				((LifecycleAware) eventHandler).onStart();
			} catch (final Throwable ex) {
				exceptionHandler.handleOnStartException(ex);
			}
		}
	}

	private void notifyShutdown() {
		if (eventHandler instanceof LifecycleAware) {
			try {
				((LifecycleAware) eventHandler).onShutdown();
			} catch (final Throwable ex) {
				exceptionHandler.handleOnShutdownException(ex);
			}
		}
	}

}
