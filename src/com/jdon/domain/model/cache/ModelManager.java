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

package com.jdon.domain.model.cache;

/**
 * 本类是产生框架使用的各种Model，面向客户端调用 如果需要调用Model相关功能，调用本ModelManager
 * 
 * 为提高性能，可在此嵌入对象池。
 * 
 * @author banq
 */
public interface ModelManager {

	/**
	 * 将Model实例加入缓存
	 * 
	 * @return a saved cache model, maybe is not different from old model object
	 */
	public Object addCache(ModelKey modelKey, Object model);

	/**
	 * 将Model实例加入缓存
	 * 
	 * @return
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

	/**
	 * 
	 */

	public boolean containInCache(ModelKey modelKey);

	public void clearCache();

}
