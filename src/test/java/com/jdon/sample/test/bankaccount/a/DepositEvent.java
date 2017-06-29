package com.jdon.sample.test.bankaccount.a;

public class DepositEvent extends TransferEvent {
	
	private final String preId;
	
	public DepositEvent(int id, int value, String nextId, String preId) {
		super(id, value, nextId);
		this.preId = preId;
	}

	public String getPreId() {
		return preId;
	}
	
	

}
