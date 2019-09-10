package com.jdon.sample.test.bankaccount.processManager;

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

import com.jdon.annotation.Component;
import com.jdon.annotation.model.OnEvent;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import com.jdon.sample.test.bankaccount.command.TransferCommand;
import com.jdon.sample.test.bankaccount.event.Canceled;
import com.jdon.sample.test.bankaccount.event.TransferEvent;
import com.jdon.sample.test.bankaccount.infras.AccountRepositoryIF;
import com.jdon.sample.test.bankaccount.infras.BrokerProducerIF;


@Component
public class ProcessManagerSub {

	private final AccountRepositoryIF accountRepository;
	private final BrokerProducerIF brokerProducer;
	private final ProcessDef processDef;

	public ProcessManagerSub(ProcessDef processDef, AccountRepositoryIF accountRepository, BrokerProducerIF brokerProducer) {
		super();
		this.processDef = processDef;
		this.accountRepository = accountRepository;
		this.brokerProducer = brokerProducer;
	}

	@OnEvent("next")
	public void handle(TransferEvent transferEvent) {
		if (transferEvent instanceof Canceled) {
			TransferCommand preTransferCommand = processDef.getPreTransferCommand();
			if (preTransferCommand != null) {
				BankAccount bankAccount = accountRepository.getBankAccount(preTransferCommand.getAggregateId());
				brokerProducer.cancel(preTransferCommand.getAggregateId(), bankAccount, preTransferCommand.cancel());
			}
		}else{
			TransferCommand nextTransferCommand = processDef.getNextTransferCommand();
			if (nextTransferCommand != null)
				processDef.sendCommand(nextTransferCommand);
			else
				processDef.clear();
		}
		System.out.println(" processDef finish");

	}




}
