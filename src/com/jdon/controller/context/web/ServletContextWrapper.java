package com.jdon.controller.context.web;

import java.io.InputStream;

import javax.servlet.ServletContext;

import com.jdon.controller.context.AppContextWrapper;

public class ServletContextWrapper implements AppContextWrapper {

	private ServletContext servletContext;

	public ServletContextWrapper(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	public InputStream getResourceAsStream(String name) {
		return servletContext.getResourceAsStream(name);
	}

	public String getInitParameter(String key) {
		return servletContext.getInitParameter(key);
	}

	public Object getAttribute(String key) {
		return servletContext.getAttribute(key);

	}

	public void setAttribute(String key, Object o) {
		servletContext.setAttribute(key, o);
	}

	public void removeAttribute(String key) {
		servletContext.removeAttribute(key);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

}
