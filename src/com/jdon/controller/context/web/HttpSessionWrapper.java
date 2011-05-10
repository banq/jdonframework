package com.jdon.controller.context.web;

import javax.servlet.http.HttpSession;

import com.jdon.controller.context.SessionWrapper;

public class HttpSessionWrapper implements SessionWrapper {
	
	private HttpSession session;

	public HttpSessionWrapper(HttpSession session) {
		super();
		this.session = session;
	}

	public Object getAttribute(String key) {
		return session.getAttribute(key);
	}

	public void setAttribute(String key, Object o) {
		session.setAttribute(key, o);

	}

}
