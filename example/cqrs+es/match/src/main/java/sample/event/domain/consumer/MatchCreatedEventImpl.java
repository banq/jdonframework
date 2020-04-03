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
package sample.event.domain.consumer;

import sample.domain.Match;
import sample.domain.event.MatchCreatedEvent;
import sample.repository.MatchRepository;

import com.jdon.annotation.Consumer;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainEventHandler;

@Consumer("created")
public class MatchCreatedEventImpl implements DomainEventHandler {

	private final MatchRepository matchRepository;

	public MatchCreatedEventImpl(MatchRepository matchRepository) {
		super();
		this.matchRepository = matchRepository;
	}

	public void onEvent(EventDisruptor event, boolean arg1) throws Exception {
		MatchCreatedEvent matchCreatedEvent = (MatchCreatedEvent) event.getDomainMessage().getEventSource();

		Match match = new Match();
		match.handle(matchCreatedEvent);
		matchRepository.save(match);
	}

}
