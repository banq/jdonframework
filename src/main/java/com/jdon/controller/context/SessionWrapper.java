package com.jdon.controller.context;


public interface SessionWrapper {

	Object getAttribute(String key);

	void setAttribute(String key, Object o);
}
