package com.jdon.sample.test.bankaccount.event;

public class Deposited extends TransferEvent {

	public Deposited(String transactionId, String aggregateId, String eventId, int value) {
		super(transactionId, aggregateId, eventId, value);
	}
}
