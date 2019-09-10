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
package com.jdon.domain.model.cache;

/**
 * when event model is added or removed from cache, will callback this method.
 * 
 * @author banq
 * 
 */
public interface LifeCycle {

	/**
	 * when event model has been put into cache, call this method
	 */
	public void start();

	/**
	 * when event model has been removed from cache,, call this method
	 */
	public void stop();

	/**
	 * Called immediately after an model has been put into the cache and the
	 * model already existed in the cache. This is thus an update.
	 */
	public void update();

}
