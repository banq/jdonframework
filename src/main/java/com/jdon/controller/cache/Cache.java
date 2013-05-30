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

import java.util.Collection;

/**
 * Cache interface A Cache object is responsible for managing a collection of
 * cached objects Given an ObjectKey object, a Cache object quickly finds the
 * corresponding cached object. The Cache-Manager object passes an ObjectKey
 * object to the Cache object’s fetchObject method to get a cached object from
 * the cache
 * 
 * <p>
 * @author <a href="mailto:banqiao@jdon.com">banq</a>
 * </p>
 */
public interface Cache {
	public Object get(Object key);

	public void put(Object key, Object value);

	public void remove(Object key);

	public long size();

	public void clear();

	public boolean contain(Object key);

	public Collection keySet();

	public long getCacheHits();

	public long getCacheMisses();
}
