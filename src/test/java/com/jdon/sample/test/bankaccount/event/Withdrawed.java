package com.jdon.sample.test.bankaccount.event;

public class Withdrawed extends TransferEvent {

	public Withdrawed(String transactionId, String aggregateId, String eventId, int value) {
		super(transactionId, aggregateId, eventId, value);
	}

	public int getValue() {
		return Math.negateExact(super.getValue());
	}

}
