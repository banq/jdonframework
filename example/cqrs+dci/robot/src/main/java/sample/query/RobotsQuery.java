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

import sample.domain.Robot;
import sample.repository.RobotRepository;

import com.jdon.annotation.Component;

@Component("robotsQuery")
public class RobotsQuery {

	private RobotRepository robotRepository;

	private Collection<Robot> cache;

	public RobotsQuery(RobotRepository robotRepository) {
		this.robotRepository = robotRepository;
		this.cache = new ArrayList();
	}

	public Collection<Robot> getAllRobots() {
		if (cache.isEmpty()) {
			Collection robots = getAllRobotsFromDB();
			cache.addAll(robots);
		}
		return cache;
	}

	private Collection getAllRobotsFromDB() {
		Collection result = new ArrayList();
		for (Robot robot : robotRepository.getMemDB().values()) {
			result.add(robot);
		}
		return result;
	}

	public void clearCache() {
		cache.clear();
	}
}
