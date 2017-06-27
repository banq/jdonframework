package com.jdon.sample.test.bankaccount;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.Service;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.a.BankAccount;

@Service("accountService")
@Introduce("componentmessage")
public class AccountServiceImpl implements AccountService {

	private AccountRepositoryIF accountRepository;
	
	public AccountServiceImpl(AccountRepositoryIF accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public BankAccount getBankAccount(String id) {
		return accountRepository.getBankAccount(id);
	}
	


	public DomainMessage commandAandB(BankAccount orignal,  BankAccount target, int money) {		
		return new DomainMessage(new AccountParameterVO(accountRepository.loadSequencId(), money, target.getId()), 60000);
	}

}
