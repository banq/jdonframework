package com.jdon.sample.test.bankaccount;

import com.jdon.annotation.Service;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import com.jdon.sample.test.bankaccount.command.Deposit;
import com.jdon.sample.test.bankaccount.command.TransferCommand;
import com.jdon.sample.test.bankaccount.command.Withdraw;
import com.jdon.sample.test.bankaccount.infras.AccountRepositoryIF;
import com.jdon.sample.test.bankaccount.infras.BrokerProducerIF;
import com.jdon.sample.test.bankaccount.processManager.ProcessDef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("accountService")
public class AccountTransferServiceImpl implements AccountTransferService {
	private  final AccountRepositoryIF accountRepository;
	private final BrokerProducerIF brokerProducer;
	private final ProcessDef processDef;

	public AccountTransferServiceImpl(AccountRepositoryIF accountRepository, BrokerProducerIF brokerProducer, ProcessDef processDef) {
		this.accountRepository = accountRepository;
		this.brokerProducer = brokerProducer;
		this.processDef = processDef;
	}

	public void  transfer(BankAccount bankAccountA, BankAccount bankAccountB, int money) {
		List<TransferCommand> processDefList = new ArrayList<>();
		String tangsactionId = UUID.randomUUID().toString();
		processDefList.add(new Withdraw(tangsactionId,bankAccountA.getId(),UUID.randomUUID().toString(),money));
		processDefList.add(new Deposit(tangsactionId,bankAccountB.getId(),UUID.randomUUID().toString(),money));
		processDef.start(processDefList);
	}

	public BankAccount getBankAccount(String id) {
		return accountRepository.getBankAccount(id);
	}
	
}
