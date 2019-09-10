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
package com.jdon.sample.test.cqrs.b;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.OnCommand;
import com.jdon.sample.test.cqrs.ParameterVO;

@Model
public class AggregateRootB {
	private String id;
	
	private final AtomicInteger state = new AtomicInteger(200);
	private final Map<Integer, Integer> states = new ConcurrentHashMap(); 

	public AggregateRootB(String id) {
		super();
		this.id = id;
	}

	@OnCommand("CommandToB")
	public Object save(ParameterVO parameterVO) {
		int newstate = state.addAndGet(parameterVO.getValue()); 
		System.out.print("\n AggregateRootB Action " + newstate);
		states.put(parameterVO.getId(), newstate);
		ParameterVO parameterVOnew = new ParameterVO(parameterVO.getId(),newstate,parameterVO.getNextId());
		return parameterVOnew;

	}
	
	public int getState(int id){
		return states.get(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getState() {
		return state.get();
	}
	
	
}
