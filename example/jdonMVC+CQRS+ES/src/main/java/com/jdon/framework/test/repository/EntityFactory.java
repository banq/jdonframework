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
package com.jdon.framework.test.repository;

import com.jdon.annotation.Component;
import com.jdon.framework.test.domain.UserModel;

/**
 * after fetch th user from repository, jdonframework inject events to this user
 * 
 * construct other parts of the user; ensure the model is only one in memory
 * 
 * @author banq
 * 
 */
@Component("entityFactory")
public class EntityFactory {

	private final UserRepository userRepository;

	public EntityFactory(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;

	}

	public UserModel getUser(String userId) {
		System.err.println(" enter getUser:EntityFactory");
		// after fetch th user from repository, jdonframework inject events to
		// this user
		UserModel user = userRepository.getUser(userId);
		// construct other parts of the user;
		// ensure the model is only one in memory
		return user;
	}

}
