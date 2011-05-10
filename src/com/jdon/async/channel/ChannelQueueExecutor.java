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
package com.jdon.async.channel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.jdon.async.message.EventMessage;
import com.jdon.async.message.MessageProcessor;
import com.jdon.domain.message.DomainMessage;
import com.jdon.domain.message.MessageListener;
import com.jdon.util.Debug;

public class ChannelQueueExecutor extends Thread {
	private final static String module = ChannelQueueExecutor.class.getName();

	private final Executor executor;

	private MessageProcessor messageProcessor;

	private ChannelListenerHolder channelListenerHolder;

	public ChannelQueueExecutor(String MaxconcurrentTaskCount) {
		executor = Executors.newFixedThreadPool(Integer.parseInt(MaxconcurrentTaskCount));
	}

	/**
	 * Get tasks from the task engine. The call to get another task will block
	 * until there is an available task to execute.
	 */
	public void run() {
		while (true) {
			actionChannelListener();
		}
	}

	private void actionChannelListener() {
		try {
			EventMessage message = (EventMessage) messageProcessor.getMessage();
			MessageListener messageListener = channelListenerHolder.getChannelListener(message.getChannel());
			if (messageListener == null) {
				return;
			}
			executor.execute(new Thread(new TaskWorker(messageListener, message)));
		} catch (Exception e) {
			Debug.logError("[JdonFramework]actionChannelListener() error" + e, module);
		}
	}

	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}

	public ChannelListenerHolder getChannelListenerHolder() {
		return channelListenerHolder;
	}

	public void setChannelListenerHolder(ChannelListenerHolder channelListenerHolder) {
		this.channelListenerHolder = channelListenerHolder;
	}

	class TaskWorker implements Runnable {
		protected MessageListener dl;
		protected EventMessage message;

		public TaskWorker(MessageListener dl, EventMessage message) {
			this.dl = dl;
			this.message = message;
		}

		public void run() {
			try {
				dl.action((DomainMessage) message);
			} catch (Exception e) {
				Debug.logError("[JdonFramework]actionChannelListener() error" + e + " message=" + message.getChannel() + " listener="
						+ dl.getClass().getName(), module);
			} finally {
			}
		}
	}

}
