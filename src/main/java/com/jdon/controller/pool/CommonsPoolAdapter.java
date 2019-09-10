/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.controller.pool;

import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * ObjectPool pool = new StackObjectPool(new MyPoolableObjectFactory());
 * CommonsPoolAdapter cp = new CommonsPoolAdapter(pool); MyObject mo =
 * (MyObject)cp.borrowObject(); .... cp.returnObject(mo);
 * 
 * <p>
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *         </p>
 */
public class CommonsPoolAdapter implements Pool {
	private final GenericObjectPool pool;

	public CommonsPoolAdapter(GenericObjectPool pool) {

		this.pool = pool;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		pool.setMaxActive(maxPoolSize);
	}

	public int getMaxPoolSize() {
		return pool.getMaxActive();
	}

	public Object acquirePoolable() throws Exception {
		return this.pool.borrowObject();
	}

	public void releasePoolable(Object object) throws Exception {
		this.pool.returnObject(object);
	}

	public int getNumActive() {
		return this.pool.getNumActive();
	}

	public int getNumIdle() {
		return this.pool.getNumIdle();
	}

	public void close() {
		pool.clear();
		try {
			pool.close();
		} catch (Exception e) {
		}
	}

}
