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
package com.jdon.container.interceptor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IntroduceInfo {

	private String adviceName;
	// key method Name
	private Map<String, Method> befores;
	private Map<String, Method> afters;
	private Map<String, Integer> inputParametersPositions;
	private Map<String, Integer> returnParametersPositions;
	private Map<String, Integer> introducedParametersPositions;

	private Map<Method, Method> methods;
	private Class target;
	private String targetName;

	public IntroduceInfo(String adviceName, Class target) {
		super();
		this.adviceName = adviceName;
		this.target = target;
		this.methods = new HashMap();
		this.afters = new HashMap();
		this.befores = new HashMap();
		this.inputParametersPositions = new HashMap();
		this.returnParametersPositions = new HashMap();
		this.introducedParametersPositions = new HashMap();
	}

	public String getAdviceName() {
		return adviceName;
	}

	public Map<Method, Method> getMethods() {
		return methods;
	}

	public void setMethods(Map<Method, Method> methods) {
		this.methods = methods;
	}

	public Map<String, Method> getBefores() {
		return befores;
	}

	public void setBefores(Map<String, Method> befores) {
		this.befores = befores;
	}

	public Map<String, Method> getAfters() {
		return afters;
	}

	public void setAfters(Map<String, Method> afters) {
		this.afters = afters;
	}

	public Class getTarget() {
		return target;
	}

	public void setTarget(Class target) {
		this.target = target;
	}

	public Map<String, Integer> getInputParametersPositions() {
		return inputParametersPositions;
	}

	public void setInputParametersPositions(Map<String, Integer> inputParametersPositions) {
		this.inputParametersPositions = inputParametersPositions;
	}

	public Map<String, Integer> getReturnParametersPositions() {
		return returnParametersPositions;
	}

	public void setReturnParametersPositions(Map<String, Integer> returnParametersPositions) {
		this.returnParametersPositions = returnParametersPositions;
	}

	public Map<String, Integer> getIntroducedParametersPositions() {
		return introducedParametersPositions;
	}

	public void setIntroducedParametersPositions(Map<String, Integer> introducedParametersPositions) {
		this.introducedParametersPositions = introducedParametersPositions;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public void clear() {
		this.afters.clear();
		this.befores.clear();
		this.inputParametersPositions.clear();
		this.introducedParametersPositions.clear();
		this.methods.clear();
		this.returnParametersPositions.clear();
		this.target = null;
		this.targetName = null;
	}

}
