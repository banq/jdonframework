/*
 * Copyright 2003-2006 the original author or authors.
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
package com.jdon.model.handler;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jdon.annotation.Component;
import com.jdon.model.ModelHandler;
import com.jdon.model.factory.ModelHandlerClassXMLBuilder;
import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq </event>
 * 
 */
@Component
public class HandlerObjectFactory {
	public final static String module = HandlerObjectFactory.class.getName();

	private final int poolSize = 50;

	private final ModelHandlerClassXMLBuilder modelHandlerClassBuilder;

	// 空闲ModelHandler池
	// private Map handlerFreePool = Collections.synchronizedMap(new HashMap());
	private final Map handlerFreePool = new ConcurrentHashMap();

	// 在使用的ModelHandler池
	// private Map handlerUsedPool = Collections.synchronizedMap(new HashMap());
	private final Map handlerUsedPool = new ConcurrentHashMap();

	public HandlerObjectFactory(ModelHandlerClassXMLBuilder modelXmlConfig) {
		this.modelHandlerClassBuilder = modelXmlConfig;
	}

	/**
	 * 获得一个空闲的ModelHandler 如果空闲池没有，就重新生成指定个数的ModelHandler实例。 获得成功，则记入在用池。
	 * 当客户端调用完毕，调用returnHandlerObject返回该ModelHandler实例备重用。
	 * 
	 * @param formName
	 * @return
	 * @throws java.lang.Exception
	 */
	public ModelHandler borrowHandlerObject(String formName) {
		ModelHandler modelHandler = null;
		try {
			modelHandler = makeHandlerObject(formName);
		} catch (Exception e) {
			Debug.logError("[JdonFramework]borrowHandlerObject error:" + e, module);
		}
		/**
		 * ModelHandler modelHandler = null; try { String poolKey =
		 * (modelHandlerClassBuilder.getModelMapping(formName)).getHandler();
		 * 
		 * LinkedList listFree = (LinkedList) handlerFreePool.get(poolKey); if
		 * ((listFree == null) || (listFree.isEmpty())) { // 如果空了，生产 listFree =
		 * makeHandlerObjects(formName); handlerFreePool.put(poolKey, listFree);
		 * } modelHandler = (ModelHandler) listFree.removeFirst();
		 * 
		 * // 加入已经用的池 LinkedList listUsed = (LinkedList)
		 * handlerUsedPool.get(poolKey); if (listUsed == null) { listUsed = new
		 * LinkedList(); handlerUsedPool.put(poolKey, listUsed); }
		 * listUsed.add(modelHandler);
		 * 
		 * Debug.logVerbose("[JdonFramework]--> borrow Modelhandler instance " +
		 * poolKey + listFree.size() + " for " + formName, module); } catch
		 * (Exception e) {
		 * Debug.logError("[JdonFramework]borrowHandlerObject error:" + e,
		 * module); }
		 **/
		return modelHandler;
	}

	/**
	 * 生成指定个数的实例
	 * 
	 * @param formName
	 * @return
	 * @throws java.lang.Exception
	 */
	private synchronized LinkedList makeHandlerObjects(String formName) throws Exception {
		Debug.logVerbose("[JdonFramework]--> create Modelhandler instance " + poolSize, module);
		int count = 0;
		ModelHandler modelHandler = null;
		LinkedList list = new LinkedList();
		while (count < poolSize) {
			modelHandler = makeHandlerObject(formName);
			list.add(modelHandler);
			count++;
		}
		return list;
	}

	/**
	 * 返还使用过的ModelHandler 1.从使用池中删除该实例 2.将该实例加入空闲池
	 * 
	 * @param modelHandler
	 * @throws java.lang.Exception
	 */
	public void returnHandlerObject(ModelHandler modelHandler) {
		/**
		 * try { String poolKey = modelHandler.getClass().getName(); LinkedList
		 * listUsed = (LinkedList) handlerUsedPool.get(poolKey); if (listUsed ==
		 * null) { Debug.logError(
		 * "[JdonFramework]ERROR:not find the used pool: class = " + poolKey,
		 * module); return; } listUsed.remove(modelHandler); LinkedList listFree
		 * = (LinkedList) handlerFreePool.get(poolKey); if (listFree == null) {
		 * Debug
		 * .logError("[JdonFramework]ERROR:not find the free pool: class = " +
		 * poolKey, module); return; } listFree.add(modelHandler);
		 * Debug.logVerbose
		 * ("[JdonFramework]--> return Modelhandler instance successfully" +
		 * poolKey + listFree.size(), module); } catch (Exception e) {
		 * Debug.logError("[JdonFramework]returnHandlerObject error:" + e,
		 * module); }
		 */
	}

	private synchronized ModelHandler makeHandlerObject(String formName) throws Exception {
		ModelHandler object = null;
		Class handlerClass = null;
		try {
			handlerClass = modelHandlerClassBuilder.getHandlerClasses(formName);
			if (handlerClass == null) {
				throw new Exception(" not found the handler in config xml formName=" + formName);
			}
			object = (ModelHandler) handlerClass.newInstance();
		} catch (Exception e) {
			Debug.logError("[JdonFramework]--> call Handler: " + handlerClass + " error:" + e, module);
			throw new Exception(e);
		}
		return object;
	}

}
