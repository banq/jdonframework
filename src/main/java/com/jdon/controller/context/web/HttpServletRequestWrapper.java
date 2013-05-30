package com.jdon.controller.context.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.ContextHolder;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.controller.context.SessionWrapper;

public class HttpServletRequestWrapper implements RequestWrapper {

	private HttpServletRequest request;
	private ContextHolder contextHolder;

	public HttpServletRequestWrapper(HttpServletRequest request) {
		this.request = request;
		AppContextWrapper acw = new ServletContextWrapper(request.getSession().getServletContext());
		SessionWrapper sw = new HttpSessionWrapper(request.getSession());
		this.contextHolder = new ContextHolder(acw, sw);
	}

	public HttpServletRequestWrapper(HttpServletRequest request, ContextHolder contextHolder) {
		this.request = request;
		this.contextHolder = contextHolder;
	}

	public ContextHolder getContextHolder() {
		return contextHolder;
	}

	public Principal getRegisteredPrincipal() {
		return request.getUserPrincipal();
	}

	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

}
