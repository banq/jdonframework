package com.jdon.sample.test.bankaccount.b;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.AccountRepositoryIF;
import com.jdon.sample.test.bankaccount.AccountParameterVO;
import com.jdon.sample.test.bankaccount.a.BankAccount;

@Component()
@Introduce("componentmessage")
public class ABEventToCommand implements AbEventToCommandIF {

	private AccountRepositoryIF accountRepository;
	
	public ABEventToCommand(AccountRepositoryIF accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public DomainMessage transfer(BankAccount bModel, AccountParameterVO parameterVO) {
		return new DomainMessage(parameterVO);

	}
}
