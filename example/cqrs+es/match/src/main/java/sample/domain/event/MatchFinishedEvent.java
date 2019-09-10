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
package sample.domain.event;

import java.util.Date;

public class MatchFinishedEvent {

	private final String matchId;

	private final Date matchDate;

	private final int homeGoals;
	private final int awayGoals;

	public MatchFinishedEvent(String matchId, Date matchDate, int homeGoals, int awayGoals) {
		super();
		this.matchId = matchId;
		this.matchDate = matchDate;
		this.homeGoals = homeGoals;
		this.awayGoals = awayGoals;
	}

	public String getMatchId() {
		return matchId;
	}

	public Date getMatchDate() {
		return matchDate;
	}

	public int getHomeGoals() {
		return homeGoals;
	}

	public int getAwayGoals() {
		return awayGoals;
	}

}
