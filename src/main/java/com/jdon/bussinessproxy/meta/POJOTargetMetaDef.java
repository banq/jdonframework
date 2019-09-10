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
package com.jdon.bussinessproxy.meta;

import com.jdon.bussinessproxy.target.POJOObjectFactory;
import com.jdon.bussinessproxy.target.TargetObjectFactory;

/**
 * POJO Service Meta Definition
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class POJOTargetMetaDef extends AbstractTargetMetaDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5220467463142903890L;
	private String className;
	private String name;
	private String[] constructors;
	protected POJOObjectFactory pOJOObjectFactory;

	public POJOTargetMetaDef(String name, String className) {
		this.name = name;
		this.className = className;
	}

	public POJOTargetMetaDef(String name, String className, String[] constructors) {
		this.name = name;
		this.className = className;
		this.constructors = constructors;
	}

	public boolean isEJB() {
		return false;
	}

	public String getClassName() {
		return this.className;
	}

	public String getCacheKey() {
		return this.className;
	}

	public String[] getConstructors() {
		return constructors;
	}

	public void setConstructors(String[] constructors) {
		this.constructors = constructors;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public TargetObjectFactory getTargetObjectFactory() {
		if (pOJOObjectFactory == null)
			this.init();
		return pOJOObjectFactory;
	}

	protected void init() {
		pOJOObjectFactory = new POJOObjectFactory(this);
	}

}
