package com.jdon.controller.context.application;

import java.util.HashMap;
import java.util.Map;

import com.jdon.controller.context.SessionWrapper;

public class DemoSessionWrapper implements SessionWrapper {
	private Map session = new HashMap();

	public Object getAttribute(String key) {
		return session.get(key);
	}

	public void setAttribute(String key, Object o) {
		session.put(key, o);
	}

}
