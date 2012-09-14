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
package com.jdon.async.task;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jdon.util.Debug;

public class TaskMonitor extends Thread {
	private final static String module = TaskMonitor.class.getName();

	private ThreadPoolExecutor exec;

	private Semaphore semaphore;

	private MessageProcessor messageProcessor;

	private final AtomicBoolean running = new AtomicBoolean(false);

	public TaskMonitor(String taskExecCount, String MaxconcurrentTaskCount) {
		exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(taskExecCount));
		semaphore = new Semaphore(Integer.parseInt(MaxconcurrentTaskCount));
	}

	/**
	 * only call this method, you will can make the work running above 8.
	 */
	public void releaseSemaphore() {
		semaphore.release();
	}

	/**
	 * Get tasks from the task engine. The call to get another task will block
	 * until there is an available task to execute.
	 */
	public void run() {
		while (running.get()) {
			try {
				if (messageProcessor != null) {
					semaphore.acquire();
					Task message = (Task) messageProcessor.getMessage();
					exec.execute(new TaskWorker(message));
					// Thread thread = new Thread(new TaskWorker(message));
					// thread.start();
					releaseSemaphore();// todo : need check exec.execute is
					// started?
				}
			} catch (Exception e) {
				Debug.logError("[JdonFramework]run() error" + e, module);
			}

		}
	}

	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}

	class TaskWorker implements Runnable {
		protected Task to;

		public TaskWorker(Task to) {
			this.to = to;
		}

		public void run() {
			try {
				to.action();
			} catch (Exception e) {
				Debug.logError("[JdonFramework]task run() error" + e + " task =" + to.getClass().getName(), module);
			} finally {

			}
		}
	}

	public void shut() {
		releaseSemaphore();
		exec.shutdownNow();
		running.set(false);
	}

}
