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
import com.jdon.annotation.Component;
import com.jdon.annotation.model.OnEvent;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.AccountRepositoryIF;

/**
 * acccept Domain message from @Send("mychannel") of @Introduce("message")
 * 
 * this is a futureTask message Listener;
 * 
 * @author banq
 * 
 */
@Component
public class DomainEventCommandAdapter {

	private AccountRepositoryIF accountRepository;
	private CommandProducerIF commandProducer;

	public DomainEventCommandAdapter(AccountRepositoryIF accountRepository,
			CommandProducerIF commandProducer) {
		super();
		this.accountRepository = accountRepository;
		this.commandProducer = commandProducer;
	}

	// nextStep is deposit's nextstep is withdraw or withdraw's nextstep is
	// deposit;
	@OnEvent("transfernext")
	public Object nextStep(TransferEvent transferEvent) {
		String aggregateRootBId = transferEvent.getNextId();
		BankAccount account = accountRepository
				.getBankAccount(aggregateRootBId);
		DomainMessage nextCommandResult = null;
		if (transferEvent instanceof WithdrawEvent)
			nextCommandResult = commandProducer.depositCommand(account,
					transferEvent);
		else if (transferEvent instanceof DepositEvent)
			nextCommandResult = commandProducer.withdrawCommand(account,
					transferEvent);
		return nextCommandResult.getBlockEventResult();
	}

	@OnEvent("failureEvent")
	public Object failure(TransferEvent transferEvent) throws Exception {
		ResultEvent re = (ResultEvent) transferEvent;
		BankAccount account = accountRepository.getBankAccount(re.getRootId());
		DomainMessage nextCommandResult = commandProducer.failureCommand(
				account, transferEvent);
		return nextCommandResult.getBlockEventResult();
	}

	@OnEvent("finishEvent")
	public Object finish(TransferEvent transferEvent) throws Exception {
		ResultEvent re = (ResultEvent) transferEvent;
		BankAccount account = accountRepository.getBankAccount(re.getRootId());
		DomainMessage nextCommandResult = commandProducer.finishCommand(
				account, transferEvent);
		return nextCommandResult.getBlockEventResult();
	}

}
