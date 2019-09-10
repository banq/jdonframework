package com.jdon.sample.test.bankaccount.infras;

import com.jdon.annotation.pointcut.Around;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;

public interface AccountRepositoryIF {

	@Around
	public abstract BankAccount getBankAccount(String id);
	

	
	public int loadSequencId();


}