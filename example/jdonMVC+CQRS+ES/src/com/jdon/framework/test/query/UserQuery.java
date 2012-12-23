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
package com.jdon.framework.test.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.jdon.annotation.Component;
import com.jdon.controller.cache.CacheManager;
import com.jdon.controller.model.PageIterator;
import com.jdon.framework.test.Constants;
import com.jdon.framework.test.domain.UserModel;
import com.jdon.framework.test.repository.UserRepository;
import com.jdon.model.query.PageIteratorSolver;

@Component("userQuery")
public class UserQuery {

	private final UserRepository userRepository;

	private final PageIteratorSolver pageIteratorSolverOfUser;

	public UserQuery(UserRepository userRepository, CacheManager cacheManager, Constants constants) throws NamingException {
		this.userRepository = userRepository;
		DataSource dataSource;
		Context ic = new InitialContext();
		dataSource = (DataSource) ic.lookup(constants.getJndiname());
		this.pageIteratorSolverOfUser = new PageIteratorSolver(dataSource, cacheManager);

	}

	public List getUserList() {
		List<UserModel> list = new ArrayList();
		List ids = getUsers();
		for (Object o : ids) {
			String userId = (String) o;
			list.add(userRepository.getUser(userId));

		}
		return list;
	}

	public List getUsers() {
		String GET_ALL_ITEMS_ALLCOUNT = "select count(1) from testuser ";
		String GET_ALL_ITEMS = "select userId  from testuser ";
		PageIterator pageIterator = pageIteratorSolverOfUser.getDatas("", GET_ALL_ITEMS_ALLCOUNT, GET_ALL_ITEMS, 0, 9999999);
		return Arrays.asList(pageIterator.getKeys());
	}

	public void clearCacheOfItem() {
		pageIteratorSolverOfUser.clearCache();
	}
}
