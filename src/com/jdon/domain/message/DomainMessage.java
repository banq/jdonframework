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

import com.jdon.async.EventResultHandler;
import com.jdon.async.disruptor.EventResultHandlerImp;

public class DomainMessage {

	protected Object eventSource;

	protected volatile EventResultHandler eventResultHandler;

	protected volatile Object eventResult;

	public DomainMessage(Object eventSource) {
		super();
		this.eventSource = eventSource;
		// default is EventResultHandlerImp
		this.eventResultHandler = new EventResultHandlerImp();
	}

	public Object getEventSource() {
		return eventSource;
	}

	public EventResultHandler getEventResultHandler() {
		return eventResultHandler;
	}

	public void setEventResultHandler(EventResultHandler eventResultHandler) {
		this.eventResultHandler = eventResultHandler;
	}

	/**
	 * setup time out(MILLISECONDS) value for get a Event Result
	 * 
	 * @param timeoutforeturnResult
	 *            MILLISECONDS
	 */
	public void setTimeoutforeturnResult(int timeoutforeturnResult) {
		if (eventResultHandler != null)
			eventResultHandler.setWaitforTimeout(timeoutforeturnResult);
	}

	/**
	 * get a Event Result until time out value
	 * 
	 * @return Event Result
	 */
	public Object getEventResult() {
		if (eventResult == null) {
			synchronized (this) {
				if (eventResultHandler != null) {
					eventResult = eventResultHandler.get();
					if (this.eventResultHandler != null) {
						this.eventResultHandler.clear();
						this.eventResultHandler = null;
					}
				}
			}
		}
		return this.eventResult;
	}

	/**
	 * * Blocking until get a Event Result
	 * 
	 * @return
	 */
	public Object getBlockEventResult() {
		if (eventResult == null) {
			synchronized (this) {
				if (eventResultHandler != null) {
					eventResult = eventResultHandler.getBlockedValue();
					if (this.eventResultHandler != null) {
						eventResultHandler.clear();
						this.eventResultHandler = null;
					}
				}
			}
		}
		return this.eventResult;
	}

	public void setEventResult(Object eventResultValue) {
		if (eventResultHandler != null) {
			eventResultHandler.send(eventResultValue);
		}
	}

	public void setEventSource(Object eventSource) {
		this.eventSource = eventSource;
	}

	public void clear() {
		this.eventResult = null;
		this.eventResultHandler = null;
		this.eventSource = null;
	}

	public boolean isNull() {
		if (this.eventResultHandler == null)
			return true;
		else
			return false;
	}

}
