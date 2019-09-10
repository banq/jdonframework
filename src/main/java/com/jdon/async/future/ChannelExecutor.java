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
package com.jdon.async.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jdon.domain.message.DomainMessage;
import com.jdon.util.Debug;

public class ChannelExecutor {
	private final static String module = ChannelExecutor.class.getName();
	private ExecutorService executor;

	public ChannelExecutor(String maxconcurrentTaskCount) {
		executor = Executors.newCachedThreadPool();
	}

	public void actionListener(DomainMessage domainMessage) {
		EventResultFuture eventMessageFuture = (EventResultFuture) domainMessage.getEventResultHandler();
		try {
			if (eventMessageFuture.getMessageListener() == null) {
				return;
			}
			if (eventMessageFuture.isAsyn()) {
				executor.execute(eventMessageFuture.getFutureTask());
			} else {
				eventMessageFuture.getFutureTask().run();
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework]actionChannelListener() error" + e, module);
		}
	}

	public void stop() {
		while (!executor.isShutdown()) {
			try {
				executor.shutdownNow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		executor = null;
	}

}
