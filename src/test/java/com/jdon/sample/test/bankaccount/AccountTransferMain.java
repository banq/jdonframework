package com.jdon.sample.test.bankaccount;

import junit.framework.Assert;

import com.jdon.controller.AppUtil;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.a.BankAccount;

public class AccountTransferMain {

	public static void main(String[] args) {
		AppUtil appUtil = new AppUtil();
		AccountService accountService = (AccountService) appUtil
				.getComponentInstance("accountService");
		BankAccount bankAccountA = accountService.getBankAccount("11");
		BankAccount bankAccountB = accountService.getBankAccount("22");
		DomainMessage res = accountService.commandAandB(bankAccountA,
				bankAccountB, 100);

		long start = System.currentTimeMillis();
		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		AccountParameterVO result = (AccountParameterVO) res1.getBlockEventResult();

		long stop = System.currentTimeMillis();

		Assert.assertEquals(100, bankAccountA.getAmount());
		Assert.assertEquals(-100, bankAccountB.getAmount());
	}

}
