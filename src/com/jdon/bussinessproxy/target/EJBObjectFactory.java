/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.bussinessproxy.target;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.EJBTargetMetaDef;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.servicelocator.ServiceLocatorException;
import com.jdon.servicelocator.web.ServiceLocator;
import com.jdon.util.Debug;

/**
 * @author <a href="mailto:banqiao@jdon.com">banq</a>
 * 
 */
public class EJBObjectFactory implements TargetObjectFactory {
	private final static String module = EJBObjectFactory.class.getName();

	private ServiceLocator sl;

	private EJBTargetMetaDef eJBTargetMetaDef;

	/**
	 * @param sl
	 */
	public EJBObjectFactory(ServiceLocator sl, EJBTargetMetaDef eJBTargetMetaDef) {
		super();
		this.sl = sl;
		this.eJBTargetMetaDef = eJBTargetMetaDef;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.jdon.bussinessproxy.target.TargetObjectFactory#create(com.jdon.
	 * bussinessproxy.TargetMetaDef)
	 */
	public Object create(ContainerCallback containerCallback) throws Exception {
		Object obj = null;
		Debug.logVerbose("[JdonFramework] enter createObject in EJBTargetService " + eJBTargetMetaDef.getClassName(), module);
		try {
			obj = createEJB2(eJBTargetMetaDef);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]create ejb  error: " + ex, module);
			throw new Exception(ex);
		}
		Debug.logVerbose("[JdonFramework] enter createObject in EJBTargetService " + obj.getClass().getName(), module);
		return obj;
	}

	private Object createEJB2(TargetMetaDef targetMetaDef) throws Exception {
		EJBTargetMetaDef eJBMetaDef = (EJBTargetMetaDef) targetMetaDef;
		if (eJBMetaDef.isLocal()) {
			return createEJBLocal(eJBMetaDef);
		} else {
			return createEJBRemote(eJBMetaDef);
		}
	}

	private Object createEJBLocal(EJBTargetMetaDef eJBMetaDef) throws Exception {
		Debug.logVerbose("[JdonFramework] this is EJB2 local " + eJBMetaDef.getClassName(), module);
		Object obj = null;
		try {
			Class[] params = null;
			Object[] paramos = null;
			EJBLocalHome home = (EJBLocalHome) sl.getLocalHome(eJBMetaDef.getJndiName());
			Method createMethod = home.getClass().getMethod("create", params);
			obj = (EJBLocalObject) createMethod.invoke(home, paramos);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (ServiceLocatorException e) {
			Debug.logError("[JdonFramework]locator error: " + e, module);
			throw new Exception("JNID error:" + e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return obj;
	}

	private Object createEJBRemote(EJBTargetMetaDef eJBMetaDef) throws Exception {
		Debug.logVerbose("[JdonFramework] this is EJB2 remote " + eJBMetaDef.getClassName(), module);
		Object obj = null;
		Class[] params = null;
		Object[] paramos = null;
		try {
			EJBHome home = sl.getRemoteHome(eJBMetaDef.getJndiName(), eJBMetaDef.getHomeClass());
			Method createMethod = home.getClass().getMethod("create", params);
			obj = (EJBObject) createMethod.invoke(home, paramos);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (ServiceLocatorException e) {
			Debug.logError("[JdonFramework]locator error: " + e, module);
			throw new Exception("JNID error:" + e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return obj;
	}

}
