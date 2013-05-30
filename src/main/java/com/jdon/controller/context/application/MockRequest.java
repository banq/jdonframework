package com.jdon.controller.context.application;

import java.security.Principal;

import com.jdon.controller.context.ContextHolder;
import com.jdon.controller.context.RequestWrapper;

public class MockRequest implements RequestWrapper {
	
	private ContextHolder ch;
	
	public MockRequest(Application da , DemoSessionWrapper ds){
		ch = new ContextHolder(da, ds);
	}

	public ContextHolder getContextHolder() {
		return ch;
	}

	public Principal getRegisteredPrincipal() {
		return null;
	}

	public String getRemoteAddr() {
		return "127.0.0.1";
	}

}
