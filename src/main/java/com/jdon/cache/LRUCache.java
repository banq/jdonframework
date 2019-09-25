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

package com.jdon.cache;

import java.util.Collection;


import com.jdon.container.pico.Startable;
import com.jdon.controller.cache.Cache;
import com.jdon.util.PropsUtil;

/**
 * the LRU Cache implemention. default is OFBiz's UtilCache, we can replace it
 * with better cache product.
 * 
 * cache parameters must be defined, and the configure file name must be defined
 * in container.xml too.
 * <p>
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *         </p>
 */
public class LRUCache implements Cache, Startable {

	private final UtilCache cache;

	/**
	 * configFileName must be defined in container.xml
	 * 
	 * @param configFileName
	 */
	public LRUCache(String configFileName) {
		PropsUtil propsUtil = new PropsUtil(configFileName);
		cache = new UtilCache(propsUtil);
	}

	public Object get(Object key) {
		return cache.get(key);
	}

	public void put(Object key, Object value) {
		cache.put(key, value);
	}
	
	@Override
	public synchronized Object putIfAbsent(Object key, Object value) {		
		if (!cache.containsKey(key)){
			cache.put(key, value);
		}
		return cache.get(key);

	}


	public void remove(Object key) {
		cache.remove(key);
	}

	public long size() {
		return cache.size();
	}

	public void clear() {
		cache.clearAllCaches();
	}

	public boolean contain(Object key) {
		return cache.containsKey(key);
	}

	public Collection keySet() {
		return cache.keySet();
	}

	public void stop() {
		cache.stop();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	public long getCacheHits() {
		return cache.getHitCount();
	}

	public long getCacheMisses() {
		return cache.getMissCount();
	}

}
