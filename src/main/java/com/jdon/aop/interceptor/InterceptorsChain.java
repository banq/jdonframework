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
package com.jdon.aop.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jdon.container.pico.Startable;
import com.jdon.util.Debug;

/**
 * Interceptors chain
 * 
 * all interceptors will add in this collection
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class InterceptorsChain implements Startable {
	private final static String module = InterceptorsChain.class.getName();
	public final static String NAME = "InterceptorsChain";

	/**
	 * the key is target name
	 */
	private final Map<String, List<Advisor>> chain;

	public InterceptorsChain() {
		chain = new ConcurrentHashMap<String, List<Advisor>>();
	}

	public void start() {
		Debug.logVerbose("[JdonFramework]InterceptorsChain start..", module);
	}

	public void stop() {
		chain.clear();
	}

	public void addInterceptor(String pointcut, String InterceptorName) {
		if (pointcut == null) {
			System.err.print("pointcut is null in InterceptorsChain");
			return;
		}
		List<Advisor> interceptors = (List) chain.get(pointcut);
		if (interceptors == null) {
			interceptors = new ArrayList();
			chain.put(pointcut, interceptors);
		}
		Advisor advisor = new Advisor(InterceptorName, pointcut);
		interceptors.add(advisor);
	}

	public boolean findInterceptorFromChainByName(String pointcut, String InterceptorName) {
		boolean ok = false;
		List<Advisor> interceptors = (List) chain.get(pointcut);
		for (Advisor advisor : interceptors) {
			if (advisor.getAdviceName().equals(InterceptorName)) {
				ok = true;
				break;
			}
		}
		return ok;
	}

	/**
	 * @return Returns the interceptors.
	 */
	public List<Advisor> getAdvisors(String pointcut) {
		return chain.get(pointcut);
	}

	public int size() {
		return chain.size();
	}
}
