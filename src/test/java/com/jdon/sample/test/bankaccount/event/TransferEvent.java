package com.jdon.sample.test.bankaccount.event;

public  class TransferEvent {

	private final String transactionId;
	private final String aggregateId;
	private final String eventId;
	private final int value;

	public TransferEvent(String transactionId, String aggregateId, String eventId, int value) {
		this.transactionId = transactionId;
		this.aggregateId = aggregateId;
		this.eventId = eventId;
		this.value = value;
	}

	public int getValue() {
		return value;
	}


	public String getTransactionId() {
		return transactionId;
	}

	public String getAggregateId() {
		return aggregateId;
	}

	public String getEventId() {
		return eventId;
	}
}
