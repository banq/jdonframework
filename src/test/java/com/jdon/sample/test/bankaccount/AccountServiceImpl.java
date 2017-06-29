package com.jdon.sample.test.bankaccount;

import com.jdon.annotation.Service;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.a.BankAccount;
import com.jdon.sample.test.bankaccount.a.CommandProducerIF;
import com.jdon.sample.test.bankaccount.a.TransferEvent;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	private AccountRepositoryIF accountRepository;
	
	private CommandProducerIF commandProducer;
	
	public AccountServiceImpl(AccountRepositoryIF accountRepository, CommandProducerIF commandProducer) {
		super();
		this.accountRepository = accountRepository;
		this.commandProducer = commandProducer;
	}

	public BankAccount getBankAccount(String id) {
		return accountRepository.getBankAccount(id);
	}
	
	public BankAccount getBankAccount(String id, int amount) {
		return accountRepository.getBankAccount(id, amount);
	}


	public DomainMessage transfer(BankAccount orignal,  BankAccount target, int money) {
		TransferEvent event = new TransferEvent(accountRepository.loadSequencId(), money, target.getId());
		return commandProducer.withdrawCommand(orignal, event);		
	}

	public DomainMessage transfer2(BankAccount orignal,  BankAccount target, int money) {
		TransferEvent event = new TransferEvent(accountRepository.loadSequencId(), money, target.getId());
		return commandProducer.depositCommand(orignal, event);		
	}
	
	
}
