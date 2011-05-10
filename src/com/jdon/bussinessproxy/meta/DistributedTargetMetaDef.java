package com.jdon.bussinessproxy.meta;

import com.jdon.bussinessproxy.target.DistributedObjectFactory;
import com.jdon.bussinessproxy.target.TargetObjectFactory;
import com.jdon.servicelocator.web.ServiceLocator;

/**
 * EJB3 simple Distributed Object
 * @author banq
 *
 */
public class DistributedTargetMetaDef extends AbstractTargetMetaDef{

	/**
	 * 
	 */
	private static final long serialVersionUID = 717097486911596883L;

	protected String jndiName;
	
	protected String name;
	
	private String interfaceClass;
	
	private DistributedObjectFactory distributedObjectFactory;
	
	public DistributedTargetMetaDef(String name, String jndiName) {
		this.name = name;
		this.jndiName = jndiName;
		init();
	}
	
	public DistributedTargetMetaDef(String name, String jndiName, String interfaceClass) {
		this.name = name;
		this.jndiName = jndiName;		
		this.interfaceClass = interfaceClass;
		init();
	}
	
	private void init(){
		ServiceLocator serviceLocator = new ServiceLocator();
		distributedObjectFactory = new DistributedObjectFactory(serviceLocator, this);
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	
	/**
	 * jndiName作为EJB的key
	 * @return String
	 */
	public String getCacheKey() {
		return jndiName;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isEJB() {
		return true;
	}
	
	/**
	 * EJB3 class is same as interface Class
	 */
	public String getClassName() {
		return interfaceClass;
	}
	
	/**
	 * @return Returns the interfaceClass.
	 */
	public String getInterfaceClass() {
		return interfaceClass;
	}

	/**
	 * @param interfaceClass
	 *            The interfaceClass to set.
	 */
	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	public boolean isSingleton(){
		return false;
	}
	
	public TargetObjectFactory getTargetObjectFactory() {
		return distributedObjectFactory;
	}

	
}
