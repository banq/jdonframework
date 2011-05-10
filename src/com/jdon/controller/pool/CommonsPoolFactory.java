/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.controller.pool;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.jdon.bussinessproxy.target.TargetServiceFactory;
import com.jdon.util.Debug;

public class CommonsPoolFactory implements PoolableObjectFactory {
	private final static String module = CommonsPoolFactory.class.getName();

	private final TargetServiceFactory targetServiceFactory;
	private final CommonsPoolAdapter pool;

	/**
	 * @param targetServiceFactory
	 */
	public CommonsPoolFactory(TargetServiceFactory targetServiceFactory, String maxSize) {
		super();
		this.targetServiceFactory = targetServiceFactory;

		GenericObjectPool apachePool = new GenericObjectPool(this);
		pool = new CommonsPoolAdapter(apachePool);
		if (maxSize == null) {
			Debug.logError("[JdonFramework] not set pool's max size", module);
		} else {
			int maxInt = Integer.parseInt(maxSize);
			pool.setMaxPoolSize(maxInt);
		}
	}

	/**
	 * @return Returns the pool.
	 */
	public CommonsPoolAdapter getPool() {
		return pool;
	}

	public Object makeObject() {
		Object o = null;
		try {
			o = targetServiceFactory.create();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] Pool can not make object, error: " + ex, module);
		}
		return o;
	}

	public void destroyObject(Object o) throws Exception {
		targetServiceFactory.destroy();
	}

	public void activateObject(Object o) throws Exception {
	}

	public void passivateObject(Object o) throws Exception {
	}

	public boolean validateObject(Object o) {
		return true;
	}

}
