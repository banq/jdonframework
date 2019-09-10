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
package com.jdon.sample.test.component;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.Around;

@Component("d")
@Introduce("aroundAdvice")
public class D implements DInterface {

	@Around
	public Object myMethod3(Object inVal) {
		System.out.println("this is D.myMethod3 is active!!!! ");
		int i = (Integer) inVal + 1;
		return i;
	}

	public Object myMethod4(Object inVal) {
		System.out.println("this is D.myMethod4 is active!!!! ");
		return "myMethod4" + inVal;
	}

}
