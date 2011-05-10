package com.jdon.bussinessproxy.target;

import javax.naming.InitialContext;

import com.jdon.bussinessproxy.meta.DistributedTargetMetaDef;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.servicelocator.web.ServiceLocator;
import com.jdon.util.Debug;

public class DistributedObjectFactory implements TargetObjectFactory {
	private final static String module = EJBObjectFactory.class.getName();

	private DistributedTargetMetaDef dtargetMetaDef;

	/**
	 * @param sl
	 */
	public DistributedObjectFactory(ServiceLocator sl, DistributedTargetMetaDef dtargetMetaDef) {
		super();
		this.dtargetMetaDef = dtargetMetaDef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.jdon.bussinessproxy.target.TargetObjectFactory#create(com.jdon.
	 * bussinessproxy.TargetMetaDef)
	 */
	public Object create(ContainerCallback containerCallback) throws Exception {
		Object obj = null;
		Debug.logVerbose("[JdonFramework] enter createObject in EJBTargetService " + dtargetMetaDef.getClassName(), module);
		try {
			Debug.logVerbose("[JdonFramework] this is EJB3  JNDIName=" + dtargetMetaDef.getJndiName(), module);
			InitialContext ctx = new InitialContext();
			obj = ctx.lookup(dtargetMetaDef.getJndiName());
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]create ejb  error: " + ex, module);
			throw new Exception(ex);
		}
		Debug.logVerbose("[JdonFramework] enter createObject in EJBTargetService " + obj.getClass().getName(), module);
		return obj;
	}

}
