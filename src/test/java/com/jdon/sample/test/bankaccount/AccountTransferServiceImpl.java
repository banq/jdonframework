package com.jdon.sample.test.bankaccount;

import com.jdon.annotation.Service;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import com.jdon.sample.test.bankaccount.command.Deposit;
import com.jdon.sample.test.bankaccount.command.TransferCommand;
import com.jdon.sample.test.bankaccount.command.Withdraw;
import com.jdon.sample.test.bankaccount.event.TransferEvent;
import com.jdon.sample.test.bankaccount.infras.AccountRepositoryIF;
import com.jdon.sample.test.bankaccount.infras.BrokerProducerIF;
import com.jdon.sample.test.bankaccount.processManager.ProcessManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	private  final AccountRepositoryIF accountRepository;
	private final BrokerProducerIF brokerProducer;

	public AccountServiceImpl(AccountRepositoryIF accountRepository, BrokerProducerIF brokerProducer) {
		this.accountRepository = accountRepository;
		this.brokerProducer = brokerProducer;
	}

	public void  transfer(String fromId, String toId, int money) {
		List<TransferCommand> processDef = new ArrayList<>();
		String tangsactionId = UUID.randomUUID().toString();
		processDef.add(new Withdraw(tangsactionId,fromId,UUID.randomUUID().toString(),money));
		processDef.add(new Deposit(tangsactionId,toId,UUID.randomUUID().toString(),money));
		ProcessManager processManager = new ProcessManager(accountRepository,brokerProducer, processDef);
		processManager.start();
	}

	
}
