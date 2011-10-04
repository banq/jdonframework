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
package com.jdon.domain.message;

import com.jdon.async.EventMessage;

public class DomainMessage {

	protected Object eventSource;
	protected EventMessage eventMessage;

	public DomainMessage(Object eventSource) {
		super();
		this.eventSource = eventSource;
	}

	public EventMessage getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(EventMessage eventMessage) {
		this.eventMessage = eventMessage;
	}

	public Object getEventSource() {
		return eventSource;
	}

	public void setEventSource(Object eventSource) {
		this.eventSource = eventSource;
	}

	/**
	 * setup time out(MILLISECONDS) value for get a Event Result
	 * 
	 * @param timeoutforeturnResult
	 *            MILLISECONDS
	 */
	public void setTimeoutforeturnResult(int timeoutforeturnResult) {
		eventMessage.setTimeoutforeturnResult(timeoutforeturnResult);
	}

	/**
	 * get a Event Result until time out value
	 * 
	 * @return Event Result
	 */
	public Object getEventResult() {
		if (eventMessage == null)
			System.err.print("eventMessage is null " + eventSource.getClass());
		return eventMessage.getEventResult();
	}

	/**
	 * * Blocking until get a Event Result
	 * 
	 * @return
	 */
	public Object getBlockEventResult() {
		if (eventMessage == null)
			System.err.print("eventMessage is null " + eventSource.getClass());
		return eventMessage.getBlockedEventResult();
	}

	public void setEventResult(Object eventResult) {
		if (eventMessage == null)
			System.err.print("eventMessage is null " + eventSource.getClass());

		eventMessage.setEventResult(eventResult);
	}

}
