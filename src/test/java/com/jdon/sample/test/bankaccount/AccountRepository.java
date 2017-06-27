package com.jdon.sample.test.bankaccount;

import java.util.concurrent.atomic.AtomicInteger;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.Around;
import com.jdon.sample.test.bankaccount.a.BankAccount;


@Component()
@Introduce("modelCache")
public class AccountRepository implements AccountRepositoryIF {
	private final AtomicInteger sequenceId = new AtomicInteger(0); 

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jdon.sample.test.cqrs.ABRepositoryIF#loadA(java.lang.String)
	 */
	@Around
	public BankAccount getBankAccount(String id) {
		BankAccount model = new BankAccount(id);		
		return model;

	}
	
	public int loadSequencId(){
		return sequenceId.incrementAndGet();
	}


}
