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
package com.jdon.async.message;

import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import com.jdon.domain.message.MessageListener;

public class EventMessage {

	protected String channel;

	protected boolean asyn;

	protected MessageListener messageListener;

	private LinkedBlockingQueue futureTasks = new LinkedBlockingQueue();

	protected Object eventResult;

	protected boolean over;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public boolean isAsyn() {
		return asyn;
	}

	public void setAsyn(boolean asyn) {
		this.asyn = asyn;
	}

	public Object getEventResult() {
		Object result = null;
		if (over)
			return eventResult;
		try {
			FutureTask futureTask = (FutureTask) futureTasks.take();
			boolean runOk = (Boolean) futureTask.get();
			if (runOk) {
				result = this.eventResult;
			}
			over = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void setEventResult(Object result) {
		this.eventResult = result;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void addFutureTask(FutureTask futureTask) {
		this.futureTasks.add(futureTask);
	}

}
