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

import com.jdon.annotation.model.Owner;
import com.jdon.annotation.model.Receiver;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.cqrs.a.AggregateRootA;
import com.jdon.sample.test.cqrs.b.AggregateRootB;

public interface AService {

	@Send("CommandtoEventA")
	public DomainMessage commandAandB(@Owner String rootId, @Receiver AggregateRootA model, int state);

	AggregateRootA getAggregateRootA(String id);
	
	AggregateRootB getAggregateRootB(String id);
}
