package com.jdon.sample.test.bankaccount;

import junit.framework.Assert;

import com.jdon.controller.AppUtil;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.bankaccount.a.BankAccount;
import com.jdon.sample.test.bankaccount.a.ResultEvent;

public class AccountTransferMain {

	public static boolean testTransferFail() {
		AppUtil appUtil = new AppUtil();
		AccountService accountService = (AccountService) appUtil
				.getComponentInstance("accountService");
		BankAccount bankAccountA = accountService.getBankAccount("11");
		BankAccount bankAccountB = accountService.getBankAccount("22");
		DomainMessage res = accountService.transfer(bankAccountA, bankAccountB,
				100);

		long start = System.currentTimeMillis();
		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		ResultEvent result = (ResultEvent) res1.getBlockEventResult();
//		Object o = result.getBlockEventResult();// block until all transfer ok;
		
		long stop = System.currentTimeMillis();

		Assert.assertEquals(0, bankAccountA.getAmount());
		Assert.assertEquals(0, bankAccountB.getAmount());
		
		return (bankAccountA.getAmount()==0 && bankAccountB.getAmount()==0);
	}

	public static boolean testTransferFinish() {
		AppUtil appUtil = new AppUtil();
		AccountService accountService = (AccountService) appUtil
				.getComponentInstance("accountService");
		BankAccount bankAccountA = accountService.getBankAccount("11", 100);
		BankAccount bankAccountB = accountService.getBankAccount("22", 0);
		DomainMessage res = accountService.transfer(bankAccountA, bankAccountB,
				100);

		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		DomainMessage result = (DomainMessage) res1.getBlockEventResult();
		Object o = result.getBlockEventResult();// block until all transfer ok;

		Assert.assertEquals(0, bankAccountA.getAmount());
		Assert.assertEquals(100, bankAccountB.getAmount());

		return (bankAccountA.getAmount()==0 && bankAccountB.getAmount()==100);
	}

	public static boolean testTransferFinish2() {
		AppUtil appUtil = new AppUtil();
		AccountService accountService = (AccountService) appUtil
				.getComponentInstance("accountService");
		BankAccount bankAccountA = accountService.getBankAccount("11", 0);
		BankAccount bankAccountB = accountService.getBankAccount("22", 100);
		DomainMessage res = accountService.transfer2(bankAccountA, bankAccountB,
				100);

		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		DomainMessage result = (DomainMessage) res1.getBlockEventResult();
		Object o = result.getBlockEventResult();// block until all transfer ok;

		Assert.assertEquals(100, bankAccountA.getAmount());
		Assert.assertEquals(0, bankAccountB.getAmount());
		return (bankAccountA.getAmount()==100 && bankAccountB.getAmount()==0);
	}

	public static void main(String[] args) {

		testTransferFinish();
	}

}
