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
package sample.service;

import sample.domain.User;

import com.jdon.annotation.Consumer;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainMessage;
import com.jdon.domain.message.DomainEventHandler;

@Consumer("userMessage")
public class UserMessageListener implements DomainEventHandler {

	@Override
	public void onEvent(EventDisruptor event, boolean endOfBatch) throws Exception {
		User user = (User) event.getDomainMessage().getEventSource();
		user.setAge(18);
		event.getDomainMessage().setEventResult(18);
	}

}
