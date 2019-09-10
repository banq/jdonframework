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

@Component("c")
public class C {

	public Object testOne(Object inVal) {
		System.out.println("this is C.testOne " + inVal);
		int i = (Integer) inVal;
		return i++;
	}

	public Object testWo(Object inVal) {
		System.out.println("this is C.testWo ");
		int i = (Integer) inVal;
		return i++;
	}

}
