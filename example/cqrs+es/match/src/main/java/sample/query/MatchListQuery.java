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
package sample.query;

import java.util.ArrayList;
import java.util.Collection;

import sample.domain.Match;
import sample.repository.MatchRepository;

import com.jdon.annotation.Component;

@Component("matchListQuery")
public class MatchListQuery {

	private MatchRepository matchRepository;

	private Collection<Match> cache;

	public MatchListQuery(MatchRepository matchRepository) {
		this.matchRepository = matchRepository;
		this.cache = new ArrayList();
	}

	public Collection<Match> getAllMatchs() {
		if (cache.isEmpty()) {
			Collection matchs = getAllMatchsFromDB();
			cache.addAll(matchs);
		}
		return cache;
	}

	private Collection<Match> getAllMatchsFromDB() {
		Collection result = new ArrayList();
		for (Match match : matchRepository.getMemDB().values()) {
			result.add(match);
		}
		return result;
	}

	public void clearCache() {
		cache.clear();
	}
}
