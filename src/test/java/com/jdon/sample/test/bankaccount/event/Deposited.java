package com.jdon.sample.test.bankaccount.event;

public class DepositEvent extends TransferEvent {
	

	public DepositEvent(int id, int value) {
		super(id, value);
	}


}
