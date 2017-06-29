package com.jdon.sample.test.bankaccount;

import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.a.BankAccount;

public interface AccountService {

	public DomainMessage transfer(BankAccount orignal, BankAccount target, int money);
	
	public DomainMessage transfer2(BankAccount orignal,  BankAccount target, int money); 

	BankAccount getBankAccount(String id);
	
	BankAccount getBankAccount(String id, int amount); 
	
}
