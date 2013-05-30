package com.jdon.container.visitor.http;

public class HttpSessionVisitorFactorySetup {
	
	private final int ComponentsboxsMaxSize;
	private final boolean dynamiceProxyisCached;
	
	public HttpSessionVisitorFactorySetup(String maxSize, String dynamiceProxyisCached) {
		super();
		this.ComponentsboxsMaxSize = Integer.parseInt(maxSize);
		if (dynamiceProxyisCached.equalsIgnoreCase("true"))
			this.dynamiceProxyisCached = true;
		else
			this.dynamiceProxyisCached = false;
	}

	public int getComponentsboxsMaxSize() {
		return ComponentsboxsMaxSize;
	}

	public boolean isDynamiceProxyisCached() {
		return dynamiceProxyisCached;
	}

	
	
}
