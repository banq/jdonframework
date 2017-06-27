package com.jdon.sample.test.bankaccount.a;

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
import com.jdon.annotation.Consumer;
import com.jdon.async.disruptor.EventDisruptor;
import com.jdon.domain.message.DomainEventHandler;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.AccountRepositoryIF;
import com.jdon.sample.test.bankaccount.AccountParameterVO;
import com.jdon.sample.test.bankaccount.b.AbEventToCommandIF;

/**
 * acccept Domain message from @Send("mychannel") of @Introduce("message")
 * 
 * this is a futureTask message Listener;
 * 
 * @author banq
 * 
 */
@Consumer("transferMoneyEventToB")
public class DomainEventConsumer implements DomainEventHandler {

	private AccountRepositoryIF accountRepository;
	private AbEventToCommandIF aBEventToCommand;

	public DomainEventConsumer(AccountRepositoryIF accountRepository, AbEventToCommandIF aBEventToCommand) {
		super();
		this.accountRepository = accountRepository;
		this.aBEventToCommand = aBEventToCommand;
	}

	public void onEvent(EventDisruptor event, boolean endOfBatch) throws Exception {
		AccountParameterVO parameterVO = (AccountParameterVO) event.getDomainMessage().getEventSource();
		String aggregateRootBId = parameterVO.getNextId();
		BankAccount accountB = accountRepository.getBankAccount(aggregateRootBId);
		DomainMessage nextCommandResult = aBEventToCommand.transfer(accountB, parameterVO);
		event.getDomainMessage().setEventResult(nextCommandResult.getBlockEventResult());

	}

}
