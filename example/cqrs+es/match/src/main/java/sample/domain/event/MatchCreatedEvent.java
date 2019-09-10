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

public class MatchCreatedEvent {

	private final String matchId;

	private final String matchTeamName1;

	private final String matchTeamName2;

	public MatchCreatedEvent(String matchId, String matchTeamName1, String matchTeamName2) {
		super();
		this.matchId = matchId;
		this.matchTeamName1 = matchTeamName1;
		this.matchTeamName2 = matchTeamName2;
	}

	public String getMatchId() {
		return matchId;
	}

	public String getMatchTeamName1() {
		return matchTeamName1;
	}

	public String getMatchTeamName2() {
		return matchTeamName2;
	}

}
