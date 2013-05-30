package com.jdon.container.visitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jdon.container.visitor.http.HttpSessionVisitorFactorySetup;

public class ComponentsboxsInSession implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6975744109699061292L;
	private final Map componentsboxs = new ConcurrentHashMap();
	public final int maxSize;

	public ComponentsboxsInSession(HttpSessionVisitorFactorySetup httpSessionVisitorFactorySetup) {
		super();
		this.maxSize = httpSessionVisitorFactorySetup.getComponentsboxsMaxSize();
	}

	public void add(String key, Object o) {
		if (componentsboxs.size() >= maxSize)
			componentsboxs.clear();
		componentsboxs.put(key, o);
	}

	public Object get(String key) {
		return componentsboxs.get(key);
	}

	public void clear() {
		componentsboxs.clear();
	}

	public int size() {
		return componentsboxs.size();
	}

}
