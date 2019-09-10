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
package com.jdon.async.disruptor.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.picocontainer.Startable;

import com.jdon.async.disruptor.DisruptorForCommandFactory;
import com.jdon.util.Debug;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorCommandPoolFactory implements Startable {
	public final static String module = DisruptorPoolFactory.class.getName();

	private DisruptorForCommandFactory disruptorForCommandFactory;
	private ConcurrentHashMap<String, Disruptor> topicDisruptors;
	private ScheduledExecutorService scheduExecStatic = Executors.newScheduledThreadPool(1);

	public DisruptorCommandPoolFactory() {
		super();
		this.topicDisruptors = new ConcurrentHashMap();
	}

	public void start() {
		Runnable task = new Runnable() {
			public void run() {
				stopDisruptor();
			}
		};
		scheduExecStatic.scheduleAtFixedRate(task, 60 * 60, 60 * 60, TimeUnit.SECONDS);
	}

	public void stop() {
		if (topicDisruptors != null) {
			stopDisruptor();
			topicDisruptors.clear();
			topicDisruptors = null;
		}

		scheduExecStatic.shutdownNow();
	}

	private void stopDisruptor() {
		Map<String, Disruptor> mydisruptors = new HashMap(topicDisruptors);
		topicDisruptors.clear();
		try {
			Thread.sleep(10000);// wait event while until all disruptor is done;
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (String topic : mydisruptors.keySet()) {
			Disruptor disruptor = (Disruptor) mydisruptors.get(topic);
			try {
				disruptor.halt();
			} catch (Exception e) {
			}
		}
		mydisruptors.clear();

	}

	public Disruptor getDisruptor(String topic, Object target) {
		Disruptor disruptor = (Disruptor) topicDisruptors.get(topic + target);
		if (disruptor == null) {
			disruptor = disruptorForCommandFactory.createDisruptor(topic);
			if (disruptor == null) {
				Debug.logWarning("not create disruptor for " + topic, module);
				return null;
			}
			Disruptor disruptorOLd = topicDisruptors.putIfAbsent(topic + target, disruptor);
			if (disruptorOLd != null)
				disruptor = disruptorOLd;
		}
		return disruptor;
	}

	public DisruptorForCommandFactory getDisruptorForCommandFactory() {
		return disruptorForCommandFactory;
	}

	public void setDisruptorForCommandFactory(DisruptorForCommandFactory disruptorForCommandFactory) {
		this.disruptorForCommandFactory = disruptorForCommandFactory;
	}

}
