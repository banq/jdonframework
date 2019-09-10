package com.jdon.sample.test.bankaccount.event;

public class WithdrawEvent extends TransferEvent {

	public WithdrawEvent(int id, int value) {
		super(id, value);
	}

}
