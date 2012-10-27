/*
 * Copyright 2007 the original author or jdon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package com.jdon.components.encache;

import java.io.InputStream;
import java.util.Collection;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.jdon.container.pico.Startable;
import com.jdon.util.FileLocator;

public class EncacheProvider implements com.jdon.controller.cache.Cache, Startable {

	private CacheManager manager;

	private final EhcacheConf ehcacheConf;

	/**
	 * configFileName must be defined in container.xml
	 * 
	 * @param configFileName
	 */
	public EncacheProvider(EhcacheConf ehcacheConf) {
		this.ehcacheConf = ehcacheConf;
	}

	public void start() {
		synchronized (CacheManager.class) {
			if (this.manager == null) {
				FileLocator fileLocator = new FileLocator();
				InputStream pathCongfgName = fileLocator.getConfStream(ehcacheConf.getEhcacheConfFileName());
				this.manager = new CacheManager(pathCongfgName);
			}
		}

	}

	public void stop() {
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		cache.removeAll();
		manager.removeCache(ehcacheConf.getPredefinedCacheName());
		manager.removalAll();
		manager.clearAll();
		manager.shutdown();
		manager = null;
	}

	public Object get(Object key) {
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		Element e = (Element) cache.get(key);
		if (e == null)
			return null;
		return e.getObjectValue();
	}

	public void put(Object key, Object value) {
		Element element = new Element(key, value);
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		cache.put(element);
	}

	public void remove(Object key) {
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		cache.remove(key);
	}

	public long size() {
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		return cache.getMemoryStoreSize();
	}

	public void clear() {
		if (manager != null) {
			Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
			cache.removeAll();
		}
	}

	public boolean contain(Object key) {
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		return cache.isKeyInCache(key);
	}

	public Collection keySet() {
		Cache cache = manager.getCache(ehcacheConf.getPredefinedCacheName());
		return cache.getKeys();
	}

	public static void main(String[] args) throws Exception {
		EhcacheConf ehcacheConf = new EhcacheConf("jdon_ehcache.xml", "jdonCache");
		EncacheProvider encacheProvider = new EncacheProvider(ehcacheConf);
		encacheProvider.start();
		encacheProvider.put("key1", "value122");
		String value = (String) encacheProvider.get("key1");
		System.out.println("value=" + value);
		encacheProvider.remove("key1");
		value = (String) encacheProvider.get("key2");
		System.out.println("value2=" + value);
	}

}