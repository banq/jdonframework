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
package com.jdon.components.encache;

import com.jdon.domain.model.cache.LifeCycle;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

import java.util.Iterator;

/**
 * <cacheEventListenerFactory
 * class="com.jdon.components.encache.EhCacheListnerFactory"/>
 * 
 * need configure in META-INF/jdon_ehcache.xml
 * 
 * @author banq
 * 
 */
public class EhCacheListener implements CacheEventListener {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void notifyElementEvicted(Ehcache arg0, Element e) {
		Object model = e.getObjectValue();
		if (model instanceof LifeCycle) {
			LifeCycle lf = (LifeCycle) model;
			lf.stop();
		}

	}

	public void notifyElementExpired(Ehcache arg0, Element e) {
		Object model = e.getObjectValue();
		if (model instanceof LifeCycle) {
			LifeCycle lf = (LifeCycle) model;
			lf.stop();
		}
	}

	public void notifyElementPut(Ehcache arg0, Element e) throws CacheException {
		Object model = e.getObjectValue();
		if (model instanceof LifeCycle) {
			LifeCycle lf = (LifeCycle) model;
			lf.start();
		}

	}

	public void notifyElementRemoved(Ehcache arg0, Element e) throws CacheException {
		Object model = e.getObjectValue();
		if (model instanceof LifeCycle) {
			LifeCycle lf = (LifeCycle) model;
			lf.stop();
		}

	}

	public void notifyElementUpdated(Ehcache arg0, Element e) throws CacheException {
		Object model = e.getObjectValue();
		if (model instanceof LifeCycle) {
			LifeCycle lf = (LifeCycle) model;
			lf.update();
		}

	}

	public void notifyRemoveAll(Ehcache ehcache) {
		// TODO Auto-generated method stub
		Iterator ite = ehcache.getKeys().iterator();
		while (ite.hasNext()) {
			Element e = ehcache.get(ite.next());
			try {
				Object model = e.getObjectValue();
				if (model instanceof LifeCycle) {
					LifeCycle lf = (LifeCycle) model;
					lf.stop();
				}
			} catch (Exception ex) {
			}
		}
	}

	public java.lang.Object clone() throws java.lang.CloneNotSupportedException {
		return new EhCacheListener();
	}

}
