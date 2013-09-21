/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.Around;
import com.jdon.sample.test.cqrs.a.AggregateRootA;
import com.jdon.sample.test.cqrs.b.AggregateRootB;

@Component
@Introduce("modelCache")
public class ABRepository implements ABRepositoryIF {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jdon.sample.test.cqrs.ABRepositoryIF#loadA(java.lang.String)
	 */
	@Around
	public AggregateRootA loadA(String id) {
		AggregateRootA model = new AggregateRootA(id);
		model.setAggregateRootBId("22");
		return model;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jdon.sample.test.cqrs.ABRepositoryIF#loadB(java.lang.String)
	 */
	@Around
	public AggregateRootB loadB(String id) {
		return new AggregateRootB(id);

	}

}
