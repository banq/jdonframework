package com.jdon.sample.test.bankaccount.a;

public class TransferEvent {

	private final int id;
	private final int stateValue;

	public TransferEvent(int id, int value) {
		super();
		this.id = id;
		this.stateValue = value;
	}

	public int getValue() {
		return stateValue;
	}


	public int getId() {
		return id;
	}


}
