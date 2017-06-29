package com.jdon.sample.test.bankaccount.a;

public class TransferEvent {

	private final int id;
	private final int stateValue;
	private String nextId;

	public TransferEvent(int id, int value, String nextId) {
		super();
		this.id = id;
		this.stateValue = value;
		this.nextId = nextId;
	}

	public int getValue() {
		return stateValue;
	}

	public String getNextId() {
		return nextId;
	}

	public int getId() {
		return id;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
	

}
