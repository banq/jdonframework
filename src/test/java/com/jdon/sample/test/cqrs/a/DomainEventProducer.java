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
package com.jdon.sample.test.cqrs.a;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.cqrs.ParameterVO;

@Component
@Introduce("message")
public class DomainEventProducer implements DomainEventProduceIF {

	@Send("toEventB")
	public DomainMessage sendtoAnotherAggragate(String aggregateRootBId, int state) {
		System.out.print("\n a event happend in AggregateRootA ");
		ParameterVO pvo = new ParameterVO(Integer.parseInt(aggregateRootBId));
		pvo.setChild(state);
		return new DomainMessage(pvo);
	}

}
