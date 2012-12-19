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
package sample.domain.event;

import java.util.Date;

public class MatchStartedEvent {

	private final String matchId;

	private final Date matchDate;

	public MatchStartedEvent(String matchId, Date matchDate) {
		super();
		this.matchId = matchId;
		this.matchDate = matchDate;
	}

	public String getMatchId() {
		return matchId;
	}

	public Date getMatchDate() {
		return matchDate;
	}

}
