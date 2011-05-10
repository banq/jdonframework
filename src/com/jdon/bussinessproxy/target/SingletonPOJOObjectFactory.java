package com.jdon.bussinessproxy.target;

import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

public class SingletonPOJOObjectFactory extends POJOObjectFactory {
	private final static String module = SingletonPOJOObjectFactory.class.getName();

	public SingletonPOJOObjectFactory(POJOTargetMetaDef pOJOTargetMetaDef) {
		super(pOJOTargetMetaDef);
	}

	public Object create(ContainerCallback containerCallback) throws Exception {
		Object o = null;
		try {
			Debug.logVerbose("[JdonFramework] create singleton pojo Object for " + pOJOTargetMetaDef.getName(), module);
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			o = containerWrapper.lookup(pOJOTargetMetaDef.getName());
			Debug.logVerbose("[JdonFramework] create singleton pojo Object id " + o.hashCode(), module);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]create Singleton error: " + ex + " for class=" + pOJOTargetMetaDef.getClassName(), module);
			throw new Exception(ex);
		} catch (Throwable tw) {
			Debug.logError("[JdonFramework]create Singleton error: " + tw + " for class=" + pOJOTargetMetaDef.getClassName(), module);
			throw new Exception(tw);
		}
		return o;
	}

}
