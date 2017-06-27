package com.jdon.sample.test.bankaccount;

import com.jdon.annotation.model.Receiver;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.a.BankAccount;

public interface AccountService {

	@Send("depositCommand")
	public DomainMessage commandAandB(@Receiver BankAccount orignal, BankAccount target, int money);

	BankAccount getBankAccount(String id);
	
}
