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
package sample.event.domain.consumer.write;

import sample.domain.Robot;
import sample.event.bus.subscriber.EventBusHandler;
import sample.event.bus.subscriber.SavemeEventBusHandler;
import sample.query.RobotsQuery;

import com.jdon.annotation.Consumer;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainEventHandler;

@Consumer("saveme")
public class SavemeEventBusSubscriber implements DomainEventHandler {

	private EventBusHandler eventBusHandler;

	public SavemeEventBusSubscriber(RobotsQuery robotsQuery) {
		this.eventBusHandler = new SavemeEventBusHandler(robotsQuery);
	}

	@Override
	public void onEvent(EventDisruptor eventDisruptor, boolean arg1) throws Exception {
		Robot robot = (Robot) eventDisruptor.getDomainMessage().getEventSource();
		// todo send to JMS Server or EventBus Server;
		eventBusHandler.refresh(robot.getId());
	}

}
