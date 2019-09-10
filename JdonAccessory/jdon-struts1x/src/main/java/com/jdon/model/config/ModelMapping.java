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

package com.jdon.model.config;

import com.jdon.model.handler.HandlerMetaDef;

/**
 * Model meta mapping of xml configuration: jdonframework.xml
 * 
 * @author banq
 *
 */
public class ModelMapping implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5452626947483524868L;

	private String formName; //actionForm Name

	private String keyName; //ID
	
	private Class keyClassType; //ID class type

	private String className; //Model Class Name

	private String handler; //ModelHandle 

	private HandlerMetaDef handlerMetaDef; //ModelHandle's configuration implemention

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getHandler() {
		return handler;
	}

	public HandlerMetaDef getHandlerMetaDef() {
		return handlerMetaDef;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public void setHandlerMetaDef(HandlerMetaDef handlerMetaDef) {
		this.handlerMetaDef = handlerMetaDef;
	}

	public Class getKeyClassType() {
		return keyClassType;
	}

	public void setKeyClassType(Class keyClassType) {
		this.keyClassType = keyClassType;
	}
	
	

}
