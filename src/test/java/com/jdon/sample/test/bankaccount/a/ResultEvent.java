package com.jdon.sample.test.bankaccount.a;

public class ResultEvent extends TransferEvent {

	private final String rootId;
	
	public ResultEvent(int id, int value, String rootId) {
		super(id, value, null);
		this.rootId = rootId;
	}

	public String getRootId() {
		return rootId;
	}
	
	

}
