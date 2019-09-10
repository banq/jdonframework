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
package com.jdon.sample.test.cqrs.a;

import java.util.concurrent.atomic.AtomicInteger;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.annotation.model.OnCommand;
import com.jdon.sample.test.cqrs.ParameterVO;

@Model
public class AggregateRootA {
	private String id;

	private String aggregateRootBId;

	private final AtomicInteger state = new AtomicInteger(100);

	@Inject
	private DomainEventProduceIF domainEventProducer;

	public AggregateRootA(String id) {
		super();
		this.id = id;
	}

	@OnCommand("CommandtoEventA")
	public Object save(ParameterVO parameterVO) {
		int newstate = state.addAndGet(parameterVO.getValue());		
		System.out.print("\n AggregateRootA Action " + newstate);
		ParameterVO parameterVONew = new ParameterVO(parameterVO.getId(), newstate, parameterVO.getNextId());  
		return domainEventProducer.sendtoAnotherAggragate(parameterVONew);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAggregateRootBId() {
		return aggregateRootBId;
	}

	public void setAggregateRootBId(String aggregateRootBId) {
		this.aggregateRootBId = aggregateRootBId;
	}
	
	public int getState() {
		return state.get();
	}


}
