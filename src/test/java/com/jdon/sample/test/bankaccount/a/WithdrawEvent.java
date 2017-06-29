package com.jdon.sample.test.bankaccount.a;

public class WithdrawEvent extends TransferEvent {
	private final String preId;
	
	public WithdrawEvent(int id, int value, String nextId, String preId) {
		super(id, value, nextId);
		this.preId = preId;
	}

	public String getPreId() {
		return preId;
	}
	
}
