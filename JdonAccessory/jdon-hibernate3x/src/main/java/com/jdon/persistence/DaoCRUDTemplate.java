/*
 * Copyright 2007 the original author or jdon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.jdon.persistence;

import java.io.Serializable;

import com.jdon.controller.cache.CacheManager;
import com.jdon.model.query.PageIteratorSolver;

public abstract class DaoCRUDTemplate {

	protected PageIteratorSolver pageIteratorSolver;

	protected DaoCRUD daoCRUD;

	/**
	 * Constants must be configured in jdonframework.xml
	 * CacheManager has been in jdon container, do not need configured it again.
	 * in the jdon framework's container.xml it has existed:
	 *   <component name="cacheManager" class="com.jdon.controller.cache.CacheManager" />
	 * 
	 * @param constants
	 * @param cacheManager
	 */
	public DaoCRUDTemplate(CacheManager cacheManager, DaoCRUD daoCRUD) {
		pageIteratorSolver = new PageIteratorSolver(daoCRUD.getDataSource(), cacheManager);
		this.daoCRUD = daoCRUD;
	}

	public void insert(Object o) throws Exception {
		daoCRUD.insert(o);
		clearCacheOfItem();
	}

	public void update(Object o) throws Exception {
		daoCRUD.update(o);
		clearCacheOfItem();
	}

	public void delete(Object o) throws Exception {
		daoCRUD.delete(o);
		clearCacheOfItem();
	}

	public Object loadModelById(Class classz, Serializable Id) throws Exception {
		return daoCRUD.loadById(classz, Id);
	}

	public void clearCacheOfItem() {
		pageIteratorSolver.clearCache();
	}
	
	
	
	//public abstract PageIterator getModels(int start, int count) throws Exception;

}
