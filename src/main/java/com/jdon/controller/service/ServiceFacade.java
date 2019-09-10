/*
 * Copyright 2003-2006 the original author or authors.
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
package com.jdon.controller.service;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerFinder;
import com.jdon.container.finder.ContainerFinderImp;
import com.jdon.controller.context.AppContextWrapper;

/**
 * @author <event href="mailto:banqJdon<AT>jdon.com">banq</event>
 * 
 */
public class ServiceFacade {

	private final ContainerFinder containerFinder;

	public ServiceFacade() {
		this.containerFinder = new ContainerFinderImp();
	}

	/**
	 * the applciation' code get the service instance, such as: XXXService
	 * xxxService = WeAppUtil.getService("xxxService");
	 * 
	 * @return Returns the service.
	 */
	public Service getService(AppContextWrapper sc) {
		ContainerWrapper containerWrapper = containerFinder.findContainer(sc);
		Service service = (Service) containerWrapper.lookup(ComponentKeys.WEBSERVICE);
		return service;
	}

	/**
	 * the model configure in jdonframework.xml will execute the service
	 * directly.
	 * 
	 * and the remote servlet will call this method
	 * 
	 * @return Returns the serviceFactory.
	 */
	public ServiceFactory getServiceFactory(AppContextWrapper sc) {
		ContainerWrapper containerWrapper = containerFinder.findContainer(sc);
		ServiceFactory serviceFactory = (ServiceFactory) containerWrapper.lookup(ComponentKeys.WEBSERVICE_FACTORY);
		return serviceFactory;
	}

	public ContainerFinder getContainerFinder() {
		return containerFinder;
	}

}
