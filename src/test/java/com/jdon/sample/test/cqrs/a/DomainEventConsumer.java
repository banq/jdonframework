package com.jdon.sample.test.cqrs.a;

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
import com.jdon.annotation.Consumer;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.cqrs.RepositoryIF;
import com.jdon.sample.test.cqrs.ParameterVO;
import com.jdon.sample.test.cqrs.b.AbEventToCommandIF;
import com.jdon.sample.test.cqrs.b.AggregateRootB;

/**
 * acccept Domain message from @Send("mychannel") of @Introduce("message")
 * 
 * this is event futureTask message Listener;
 * 
 * @author banq
 * 
 */
@Consumer("toEventB")
public class DomainEventConsumer implements DomainEventHandler {

	private RepositoryIF aBRepository;
	private AbEventToCommandIF aBEventToCommand;

	public DomainEventConsumer(RepositoryIF aBRepository, AbEventToCommandIF aBEventToCommand) {
		super();
		this.aBRepository = aBRepository;
		this.aBEventToCommand = aBEventToCommand;
	}

	public void onEvent(EventDisruptor event, boolean endOfBatch) throws Exception {
		ParameterVO parameterVO = (ParameterVO) event.getDomainMessage().getEventSource();
		String aggregateRootBId = parameterVO.getNextId();
		AggregateRootB modelB = aBRepository.getB(aggregateRootBId);
		DomainMessage nextCommandResult = aBEventToCommand.ma(modelB, parameterVO);
		event.getDomainMessage().setEventResult(nextCommandResult.getBlockEventResult());

	}

}
