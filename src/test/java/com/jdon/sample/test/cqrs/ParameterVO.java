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
package com.jdon.sample.test.cqrs;

public class ParameterVO {

	private final int id;
	private final int stateValue;
	private final String nextId;

	public ParameterVO(int id, int value, String nextId) {
		super();
		this.id = id;
		this.stateValue = value;
		this.nextId = nextId;
	}

	public int getValue() {
		return stateValue;
	}

	public String getNextId() {
		return nextId;
	}

	public int getId() {
		return id;
	}
	
	

}
