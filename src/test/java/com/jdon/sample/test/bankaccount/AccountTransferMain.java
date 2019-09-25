package com.jdon.sample.test.bankaccount;

import com.jdon.controller.AppUtil;
import com.jdon.sample.test.bankaccount.aggregates.BankAccount;
import junit.framework.Assert;

public class AccountTransferMain {

	public static void main(String[] args) {
        testTransferFinish();
	}

    public static boolean testTransferFinish() {
        AppUtil appUtil = new AppUtil();
        AccountTransferService accountTransferService = (AccountTransferService) appUtil
                .getComponentInstance("accountService");
        BankAccount bankAccountA = accountTransferService.getBankAccount("11");
        bankAccountA = accountTransferService.getBankAccount("11");
        BankAccount bankAccountB = accountTransferService.getBankAccount("22");
        accountTransferService.transfer(bankAccountA, bankAccountB,
                100);

        while (bankAccountA.getBalance() != 0 ) {

        }
        Assert.assertEquals(0, bankAccountA.getBalance());
        while (bankAccountB.getBalance() != 200) {

        }
        Assert.assertEquals(200, bankAccountB.getBalance());
//        bankAccountA = accountTransferService.getBankAccount("33");
//        bankAccountB = accountTransferService.getBankAccount("44");
//        accountTransferService.transfer(bankAccountA, bankAccountB,
//                1000);
//        Assert.assertEquals(100, bankAccountA.getBalance());
//        Assert.assertEquals(100, bankAccountB.getBalance());
        return true;


    }

}
