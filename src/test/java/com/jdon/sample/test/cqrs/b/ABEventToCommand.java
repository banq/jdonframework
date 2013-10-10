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
package com.jdon.sample.test.cqrs.b;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.cqrs.ParameterVO;

@Component()
@Introduce("componentmessage")
public class ABEventToCommand implements AbEventToCommandIF {

	public DomainMessage ma(AggregateRootB bModel, int state) {
		System.out.print("\n send command to AggregateRootB =" + bModel.getId());
		return new DomainMessage(new ParameterVO(state));

	}
}
