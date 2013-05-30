package com.jdon.controller.context;

import java.io.InputStream;

public interface AppContextWrapper {

	InputStream getResourceAsStream(String name);

	String getInitParameter(String key);

	Object getAttribute(String key);

	void setAttribute(String key, Object o);

	void removeAttribute(String key);
}
