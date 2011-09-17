/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.domain.dci;

import net.sf.cglib.proxy.Mixin;

import com.jdon.annotation.Component;
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
 * the component name is roleAssigner;
 * 
 * you can do this class in your application. if you want to use this class in
 * your app, you must deploy this class in your app's WEB-INF.so can be scanned.
 * 
 * 
 * 
 * @author banq
 * 
 */
@Component("roleAssigner")
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

	public Object assign(Object datamodel, Object role) {
		Class[] inters = role.getClass().getInterfaces();
		if (inters == null || inters.length == 0) {
			Debug.logError(
					"[JdonFramework] role:" + role.getClass() + " should implements a interface when be assigned to data:" + datamodel.getClass(),
					module);
			return null;
		}

		if (datamodel.getClass().isAnnotationPresent(Model.class))
			assignDomainEvents(datamodel);

		if (role.getClass().isAnnotationPresent(Introduce.class)) {
			role = modelAdvisor.createProxy(role);
		}

		return Mixin.create(new Object[] { datamodel, role });
	}
}
