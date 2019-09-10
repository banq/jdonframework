package com.jdon.sample.test.bankaccount;

import com.jdon.sample.test.bankaccount.aggregates.BankAccount;

public interface AccountTransferService {

	void  transfer(BankAccount bankAccountA, BankAccount bankAccountB, int money);

	BankAccount getBankAccount(String id);
}
