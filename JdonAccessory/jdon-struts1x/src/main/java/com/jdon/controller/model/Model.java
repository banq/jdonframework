/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
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
package com.jdon.controller.model;

/**
 * Base domain model it can be DTO or nested Model. it is the important message
 * between business layer and view layer. in view layer, it is created by form
 * object(such as ActionForm object)；in business layer, it is created by
 * business components(such as session bean).
 * 
 * thi class can be cached, and setModified is important, this method can be
 * used to refresh the cache.
 * 
 * because setModified function ,so the class is designed for event class, but not event
 * interface.
 * 
 * the difference with setModified and setCacheable; setCacheable to false, the
 * model will never exist in the cache. setModified to true, if the model exists
 * in the cache, the client will not get it from cache, it is same as being
 * deleted from cache . deleting the model from cache must have event condition that
 * the deleting operator can access the cache of the container, if it cann't
 * access the container, it cann't delete the model from cache. such it is EJB.
 * 
 * 
 * 
 * @author banq
 */
public class Model implements ModelIF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3944062684510613038L;

	/**
	 * if set false, this Model will not be saved to cache, so this model will
	 * not be got from the cache.
	 */
	private volatile boolean cacheable = true;

	/**
	 * if set true, this model will not be got from the cache, because it must
	 * have been saved in the cache. this function is same as deleting the model
	 * from the cache.
	 */
	private volatile boolean modified;

	/**
	 * in the past version, this method name is isCacheble, now change it after
	 * 1.3 !
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * in the past version, this method name is setCacheble, now change it after
	 * 1.3 !
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public boolean isModified() {
		return modified;
	}

	/**
	 * set the property has been modified such as : setName(String name){
	 * this.name = name; setModified(true); }
	 */
	public void setModified(boolean modified) {
		this.modified = modified;
	}

}
