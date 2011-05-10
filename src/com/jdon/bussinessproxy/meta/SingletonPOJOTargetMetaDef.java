package com.jdon.bussinessproxy.meta;

import com.jdon.bussinessproxy.target.SingletonPOJOObjectFactory;

public class SingletonPOJOTargetMetaDef extends POJOTargetMetaDef{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5892020922243280686L;

	public SingletonPOJOTargetMetaDef(String name, String className) {
		super(name, className);
		init();
	}

	public SingletonPOJOTargetMetaDef(String name, String className,
			String[] constructors) {
		super(name, className, constructors);
		init();
	}
	
	protected void init(){
		pOJOObjectFactory = new SingletonPOJOObjectFactory(this);
	}
}
