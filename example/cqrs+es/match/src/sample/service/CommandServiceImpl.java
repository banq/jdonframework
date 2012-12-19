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

import java.util.Random;

import sample.domain.Match;
import sample.domain.event.MatchCreatedEvent;
import sample.repository.MatchRepository;

import com.jdon.annotation.Service;
import com.jdon.domain.dci.RoleAssigner;

@Service("commandService")
public class CommandServiceImpl implements CommandService {

	private final RoleAssigner roleAssigner;

	private final MatchRepository matchRepository;

	public CommandServiceImpl(RoleAssigner roleAssigner, MatchRepository matchRepository) {
		super();
		this.roleAssigner = roleAssigner;
		this.matchRepository = matchRepository;
	}

	@Override
	public String createMatchAggregate(String homename, String awayname) {
		String id = getNextId();
		MatchCreatedEvent matchCreatedEvent = new MatchCreatedEvent(id, homename, awayname);
		Match matchcreator = new Match();
		roleAssigner.assignDomainEvents(matchcreator);
		matchcreator.es.created(matchCreatedEvent);
		return id;
	}

	private String getNextId() {
		Random r = new Random();
		return Integer.toString(r.nextInt());
	}

	public Match getMatch(String id) {
		return matchRepository.find(id);
	}

}
