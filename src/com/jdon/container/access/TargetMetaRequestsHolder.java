package com.jdon.container.access;

public class TargetMetaRequestsHolder {

	private final static ThreadLocal targetMetaRequests = new ThreadLocal();

	public TargetMetaRequest getTargetMetaRequest() {
		return (TargetMetaRequest) targetMetaRequests.get();
	}

	public void setTargetMetaRequest(TargetMetaRequest targetMetaRequest) {
		targetMetaRequests.set(targetMetaRequest);
	}

}
