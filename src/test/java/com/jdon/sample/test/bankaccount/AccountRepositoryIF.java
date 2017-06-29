package com.jdon.sample.test.bankaccount;

import com.jdon.annotation.pointcut.Around;
import com.jdon.sample.test.bankaccount.a.BankAccount;

public interface AccountRepositoryIF {

	@Around
	public abstract BankAccount getBankAccount(String id);
	

	@Around
	public abstract BankAccount getBankAccount(String id, int amount);

	
	public int loadSequencId();


}