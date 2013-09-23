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
package com.jdon.framework.test.repository;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.jdon.annotation.Component;
import com.jdon.container.pico.Startable;
import com.jdon.controller.cache.CacheManager;
import com.jdon.domain.model.cache.ModelManager;
import com.jdon.framework.test.Constants;
import com.jdon.model.query.PageIteratorSolver;

@Component()
public class ModelCacheManager implements Startable {

	private final PageIteratorSolver pageIteratorSolverOfUser;

	private final ModelManager modelManager;

	private final DataSource dataSource;

	public ModelCacheManager(CacheManager cacheManager, Constants constants, ModelManager modelManager) throws NamingException {
		Context ic = new InitialContext();
		this.dataSource = (DataSource) ic.lookup(constants.getJndiname());
		this.pageIteratorSolverOfUser = new PageIteratorSolver(dataSource, cacheManager);
		this.modelManager = modelManager;

	}

	public PageIteratorSolver getPageIteratorSolverOfUser() {
		return pageIteratorSolverOfUser;
	}

	public void clearModelList() {
		pageIteratorSolverOfUser.clearCache();
	}

	public void removeModelFromCache(Object key) {
		try {
			modelManager.removeCache(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {

	}

}
