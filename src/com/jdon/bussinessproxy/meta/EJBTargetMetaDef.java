/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.bussinessproxy.meta;

import com.jdon.bussinessproxy.target.EJBObjectFactory;
import com.jdon.bussinessproxy.target.TargetObjectFactory;
import com.jdon.servicelocator.web.ServiceLocator;

/**
 * EJB2 Service META definition
 * 
 */

public class EJBTargetMetaDef extends DistributedTargetMetaDef {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4812146939421208809L;

	private boolean isLocal = true;

	private String home;

	private String remote;

	private String local;
	
	private EJBObjectFactory eJBObjectFactory;

	/**
	 * remote EJB
	 */
	public EJBTargetMetaDef(String name, String p_jndiName, String p_homeClassName, String p_remoteClassName) {		
		super(name, p_jndiName);
		this.home = p_homeClassName;
		this.remote = p_remoteClassName;
		this.isLocal = false;
		init();
	}

	/**
	 * local EJB
	 */
	public EJBTargetMetaDef(String name, String p_jndiName, String p_localClassName) {
		super(name, p_jndiName);
		this.local = p_localClassName;
		init();
	}
	
	private void init(){
		ServiceLocator serviceLocator = new ServiceLocator();
		eJBObjectFactory = new EJBObjectFactory(serviceLocator, this);
	}

	public boolean isEJB() {
		return true;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public Class getHomeClass() {
		try {
			return Class.forName(home);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Unable to load the class : " + home);
		}
	}

	public Class getRemoteClass() {
		try {
			return Class.forName(remote);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Unable to load the class : " + remote);
		}
	}

	public String getClassName() {
		if (isLocal)
			return getLocalName();
		else
			return getRemoteName();

	}

	public String getLocalName() {
		return local;
	}

	public String getRemoteName() {
		return remote;
	}

	public String getHomeName() {
		return home;
	}

	public boolean equals(Object obj) {
		if (obj instanceof EJBTargetMetaDef) {
			EJBTargetMetaDef definition = (EJBTargetMetaDef) obj;
			if (isLocal) {
				if (!(definition.getLocalName().equals(local)) || !(definition.getJndiName().equals(jndiName)))
					return false;

			} else {
				if (!(definition.getHomeName().equals(home)) || !(definition.getJndiName().equals(jndiName))
						|| !(definition.getRemoteName().equals(remote)))
					return false;
			}
			return true;
		}
		return false;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("EJB");
		buffer.append(jndiName);
		buffer.append("Home/Local class :");
		if (isLocal)
			buffer.append(local);
		else
			buffer.append(home);

		return buffer.toString();
	}

	public int hashCode() {
		return jndiName.hashCode();
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

	/**
	 * jndiName作为EJB的key
	 * 
	 * @return String
	 */
	public String getCacheKey() {
		return jndiName;
	}

	public TargetObjectFactory getTargetObjectFactory() {
		return eJBObjectFactory;
	}

}
