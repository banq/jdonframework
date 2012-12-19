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
package sample.domain;

import java.util.Date;

import sample.domain.event.MatchCreatedEvent;
import sample.domain.event.MatchFinishedEvent;
import sample.domain.event.MatchStartedEvent;
import sample.event.domain.publisher.EventSourcing;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;

/**
 * aggregate root
 * 
 * @author banq
 * 
 */
@Model
public class Match {

	private String id;

	private Date matchDate;

	private Team teams[] = new Team[2];

	private boolean finished;

	@Inject
	public EventSourcing es;

	public void handle(MatchCreatedEvent matchCreatedEvent) {
		this.id = matchCreatedEvent.getMatchId();
		this.teams[0] = new Team(matchCreatedEvent.getMatchTeamName1());
		this.teams[1] = new Team(matchCreatedEvent.getMatchTeamName2());
		this.finished = false;
	}

	public void startMatch(Date matchDate) {
		if (this.matchDate != null)
			System.err.print("the match has started");
		es.started(new MatchStartedEvent(this.id, matchDate));
	}

	public void handle(MatchStartedEvent event) {
		this.matchDate = event.getMatchDate();
	}

	public void finishWithScore(Score score, Date matchDate) {
		if (this.matchDate == null)
			System.err.print("the match has not started");
		if (finished)
			System.err.print("the match has finished");
		es.finished(new MatchFinishedEvent(this.id, matchDate, score.getHomeGoals(), score.getAwayGoals()));
	}

	public void handle(MatchFinishedEvent event) {
		this.finished = true;
	}

	public String getId() {
		return id;
	}

	public String getHomeTeamName() {
		return teams[0].getName();
	}

	public String getAwayTeamName() {
		return teams[1].getName();
	}

	public boolean isFinished() {
		return finished;
	}

}
