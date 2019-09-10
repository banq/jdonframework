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

package com.jdon.model;

import com.jdon.domain.model.cache.ModelKey;

/**
 * 本类是产生框架使用的各种Model，面向客户端调用 如果需要调用Model相关功能，调用本ModelManager
 * 
 * 为提高性能，可在此嵌入对象池。
 * 
 * @author banq
 */
public interface ModelHandlerManager {

	/**
	 * 从Modelhandler池中借用一个实例 借用后必须归还
	 */
	public ModelHandler borrowtHandlerObject(String formName);

	/**
	 * 归还从Modelhandler池中借用一个实例
	 */
	public void returnHandlerObject(ModelHandler modelHandler);

	/**
	 * 获得一个新的Model实例
	 */
	public Object getModelObject(String formName);

	/**
	 * 将Model实例加入缓存
	 */
	public void addCache(ModelKey modelKey, Object model);

	/**
	 * 将Model实例加入缓存
	 */
	public void addCache(Object key, String className, Object model);

	/**
	 * 获取加入缓存的Model实例
	 */

	public Object getCache(ModelKey modelKey);

	/**
	 * 获取加入缓存的Model实例
	 */
	public Object getCache(Object key, String className);

	/**
	 * 根据主键值，删除缓存中的Model实例
	 */
	public void removeCache(Object dataKey) throws Exception;

	public void clearCache();

}
