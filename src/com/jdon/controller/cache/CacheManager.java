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

package com.jdon.controller.cache;

import com.jdon.cache.CacheableWrapper;
import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;

/**
 * Cacahe Managerment Pattern
 * 
 * Client objects request objects from a CacheManager object by calling its
 * fetchObject method. The argument to the fetchObject method is an CacheKey
 * object that identifies the object to fetch. The fetchObject method works by
 * first calling the Cache object’s fetchObject method.
 * 
 * there is one CacheManager in jdon container, you can get it from the
 * container, do not need create it.
 * 
 * @author banq
 */
public class CacheManager implements Startable {
	public final static String module = CacheManager.class.getName();

	private Cache cache;

	public CacheManager(Cache cache) {
		this.cache = cache;
	}

	public void start() {
		Debug.logVerbose("[JdonFramework]CacheFactory start .....", module);
		clear();
	}

	public void stop() {
		clear();
		cache = null;
	}

	public void clear() {
		if (cache != null)
			cache.clear();
	}

	/**
	 * 从缓存中获得缓存
	 * 
	 * @param cacheKey
	 * @return
	 */
	public Object fetchObject(StringKey skey) {
		Debug.logVerbose("[JdonFramework]<-cache->try to get cache: " + skey, module);
		return fetchObject(skey.getKey());
	}

	public Object fetchObject(String skey) {
		Debug.logVerbose("[JdonFramework]<-cache->try to get cache: " + skey, module);
		CacheableWrapper cw = (CacheableWrapper) cache.get(skey);
		if (cw != null) {
			Debug.logVerbose("[JdonFramework]<-cache->got it, hashcode=" + cw.hashCode(), module);
			return cw.getCachedValue();
		} else
			return null;
	}

	/**
	 * 保存到缓存中
	 * 
	 * @param cacheKey
	 * @param value
	 */
	public void putObect(CacheKey ckey, Object value) {
		if (ckey == null)
			return;
		if (cache.contain(ckey.getKey()))
			return;
		cache.put(ckey.getKey(), new CacheableWrapper(ckey.getDataKey(), value));
		Debug.logVerbose("[JdonFramework]<-cache->save cache: " + ckey.getKey() + ", cache size:" + cache.size(), module);
	}

	public void putObect(String skey, Object value) {
		if (skey == null)
			return;
		if (cache.contain(skey))
			return;
		cache.put(skey, new CacheableWrapper(skey, value));
		Debug.logVerbose("[JdonFramework]<-cache->save cache: " + skey + ", cache size:" + cache.size(), module);
	}

	/**
	 * 清除缓存
	 * 
	 * @param cacheKey
	 */
	public void removeObect(StringKey skey) {
		if (skey == null)
			return;
		cache.remove(skey.getKey());
		Debug.logVerbose("[JdonFramework]<-cache->remove the object of " + skey + " from cache", module);
	}

	public void removeObect(String skey) {
		if (skey == null)
			return;
		cache.remove(skey);
		Debug.logVerbose("[JdonFramework]<-cache->remove the object of " + skey + " from cache", module);
	}

	/**
	 * 清除缓存中该dataKey的相关所有缓存数据 当该dataKey相关的数据增删改时，调用本方法。以便及时清除缓存。
	 * 
	 * dataKey是数据的ID，如ProductId , ItemId等
	 * 
	 * @param dataKey
	 * @param formName
	 */
	public void removeCache(Object dataKey) {
		if (dataKey == null)
			return;
		try {
			for (Object o : cache.keySet()) {
				String key = (String) o;
				Object cachedValue = (Object) cache.get(key);
				if (cachedValue instanceof CacheableWrapper) {
					CacheableWrapper cw = (CacheableWrapper) cachedValue;
					if (cw.getCachedValueKey().equals(dataKey.toString()))
						removeObect(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Cache getCache() {
		return cache;
	}

}
