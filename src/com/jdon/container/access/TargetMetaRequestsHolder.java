package com.jdon.container.access;

public class TargetMetaRequestsHolder {

	private static ThreadLocal targetMetaRequests = new ThreadLocal();

	public TargetMetaRequest getTargetMetaRequest() {
		return (TargetMetaRequest) targetMetaRequests.get();
	}

	public void setTargetMetaRequest(TargetMetaRequest targetMetaRequest) {
		targetMetaRequests.set(targetMetaRequest);
	}

	public void clear() {
		targetMetaRequests.remove();
	}

}
