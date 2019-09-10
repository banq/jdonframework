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
package sample.event.domain.publisher;

import sample.domain.event.MatchCreatedEvent;
import sample.domain.event.MatchFinishedEvent;
import sample.domain.event.MatchStartedEvent;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;

@Introduce("message")
public class EventSourcing {

	@Send("created")
	public DomainMessage created(MatchCreatedEvent matchCreatedEvent) {
		return new DomainMessage(matchCreatedEvent);
	}

	@Send("started")
	public DomainMessage started(MatchStartedEvent matchStartedEvent) {
		return new DomainMessage(matchStartedEvent);
	}

	@Send("finished")
	public DomainMessage finished(MatchFinishedEvent matchFinishedEvent) {
		return new DomainMessage(matchFinishedEvent);
	}
}
