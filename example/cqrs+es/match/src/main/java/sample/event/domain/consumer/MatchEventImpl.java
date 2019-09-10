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
import sample.domain.event.MatchFinishedEvent;
import sample.domain.event.MatchStartedEvent;
import sample.repository.MatchRepository;

import com.jdon.annotation.Component;
import com.jdon.annotation.model.OnEvent;

@Component
public class MatchEventImpl {

	private final MatchRepository matchRepository;

	public MatchEventImpl(MatchRepository matchRepository) {
		super();
		this.matchRepository = matchRepository;
	}

	@OnEvent("started")
	public void started(MatchStartedEvent matchStartedEvent) {
		Match match = matchRepository.find(matchStartedEvent.getMatchId());
		match.handle(matchStartedEvent);

	}

	@OnEvent("finished")
	public void finished(MatchFinishedEvent matchFinishedEvent) {
		Match match = matchRepository.find(matchFinishedEvent.getMatchId());
		match.handle(matchFinishedEvent);
	}
}
