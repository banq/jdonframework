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

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.jdon.domain.message.DomainMessage;
import com.jdon.util.Debug;

public class ChannelExecutor {
	private final static String module = ChannelExecutor.class.getName();
	private final Executor executor;

	public ChannelExecutor(String maxconcurrentTaskCount) {
		executor = Executors.newCachedThreadPool();
	}

	public void actionListener(DomainMessage message) {
		try {
			if (message.getMessageListener() == null) {
				return;
			}
			if (message.isAsyn()) {
				asynExecMessageListener(message);
			} else {
				synExecMessageListener(message);
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework]actionChannelListener() error" + e, module);
		}
	}

	private void asynExecMessageListener(final DomainMessage message) {
		FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				try {
					message.getMessageListener().action(message);
				} catch (Exception e) {
					Debug.logError("[JdonFramework]asynExecMessageListener() error" + e + " message=" + message.getChannel() + " listener="
							+ message.getMessageListener().getClass().getName(), module);
					return false;
				}
				return true;
			}
		});

		message.addFutureTask(futureTask);
		executor.execute(futureTask);

	}

	private void synExecMessageListener(final DomainMessage message) {
		FutureTask futureTask = new FutureTask(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				try {
					message.getMessageListener().action(message);
				} catch (Exception e) {
					Debug.logError("[JdonFramework]asynExecMessageListener() error" + e + " message=" + message.getChannel() + " listener="
							+ message.getMessageListener().getClass().getName(), module);
					return false;
				}
				return true;
			}
		});

		message.addFutureTask(futureTask);
		futureTask.run();
	}

}
