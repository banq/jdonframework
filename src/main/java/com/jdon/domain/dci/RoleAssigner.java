/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
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
package com.jdon.domain.dci;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.Model;
import com.jdon.domain.advsior.ModelAdvisor;
import com.jdon.domain.model.injection.ModelProxyInjection;
import com.jdon.util.Debug;

/**
 * DCI : Data Context interactions
 * 
 * in context, the interactions of Role will be assigned to data model;
 * 
 * the component name is "roleAssigner";
 * 
 * 
 * @author banq
 * 
 */
// @Component("roleAssigner")
public class RoleAssigner {
	public final static String module = RoleAssigner.class.getName();

	private ModelProxyInjection modelProxyInjection;
	private ModelAdvisor modelAdvisor;

	public RoleAssigner(ModelProxyInjection modelProxyInjection, ModelAdvisor modelAdvisor) {
		this.modelProxyInjection = modelProxyInjection;
		this.modelAdvisor = modelAdvisor;
	}

	public void assignDomainEvents(Object datamodel) {
		modelProxyInjection.injectProperties(datamodel);
	}

	/**
	 * assign event object as event AggregateRoot role, AggregateRoot can receive event
	 * command and reactive event event in CQRS.
	 * 
	 * when we get event domain mode from repository with @introduce("modelcache")
	 * and @Around, the mode has been assign as event AggregateRoot;no need call
	 * this method.
	 * 
	 * @param datamodel
	 */
	public Object assignAggregateRoot(Object datamodel) {
		modelProxyInjection.injectProperties(datamodel);
		return modelAdvisor.createProxy(datamodel);
	}

	public Object assignRoleEvents(Object role) {
		return modelAdvisor.createProxy(role);
	}

	public Object assignRoleEvents(Class roleClass) {
		try {
			return modelAdvisor.createProxy(roleClass.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object assign(Object datamodel, Object role) {
		Class[] inters = role.getClass().getInterfaces();
		if (inters == null || inters.length == 0) {
			Debug.logError(
					"[JdonFramework] role:" + role.getClass() + " should implements event interface when be assigned to data:" + datamodel.getClass(),
					module);
			return null;
		}

		if (datamodel.getClass().isAnnotationPresent(Model.class))
			assignDomainEvents(datamodel);

		if (role.getClass().isAnnotationPresent(Introduce.class)) {
			role = modelAdvisor.createProxy(role);
		}

		return role;
	}
}
