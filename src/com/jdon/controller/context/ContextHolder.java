package com.jdon.controller.context;

public class ContextHolder {

	private AppContextWrapper appContextHolder;
	private SessionWrapper sessionHolder;
	
	public ContextHolder(AppContextWrapper appContextHolder,
			SessionWrapper sessionHolder) {
		super();
		this.appContextHolder = appContextHolder;
		this.sessionHolder = sessionHolder;
	}

	public AppContextWrapper getAppContextHolder() {
		return appContextHolder;
	}

	public void setAppContextHolder(AppContextWrapper appContextHolder) {
		this.appContextHolder = appContextHolder;
	}

	public SessionWrapper getSessionHolder() {
		return sessionHolder;
	}

	public void setSessionHolder(SessionWrapper sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

}
