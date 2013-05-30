package com.jdon.controller.context.application;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.SessionWrapper;
import com.jdon.util.FileLocator;

public class Application implements AppContextWrapper, SessionWrapper {

	private Map attrs = new HashMap();

	public InputStream getResourceAsStream(String name) {
		FileLocator fl = new FileLocator();
		return fl.getConfPathXmlStream(name);
	}

	public String getInitParameter(String key) {
		return "";
	}

	public Object getAttribute(String key) {
		return attrs.get(key);

	}

	public void setAttribute(String key, Object o) {
		attrs.put(key, o);
	}

	public void clear() {
		attrs.clear();
	}

	@Override
	public void removeAttribute(String key) {
		attrs.remove(key);
	}

}
