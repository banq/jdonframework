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

package com.jdon.container.finder;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.builder.ContainerRegistryBuilder;
import com.jdon.container.startup.ContainerSetupScript;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;

/**
 *  Container lookup from outside the container.
 * 
 */
public class ContainerFinderImp implements ContainerFinder {
	private final static String module = ContainerFinderImp.class.getName();

	private final ContainerSetupScript containerSetupScript = new ContainerSetupScript();

	/**
	 * lazy startup container when first time the method is called, it will
	 * startup the container
	 * 
	 * @param sc
	 *            ServletContext
	 * @return ContainerWrapper
	 * @throws Exception
	 */

	public ContainerWrapper findContainer(AppContextWrapper sc) {
		ContainerRegistryBuilder cb = (ContainerRegistryBuilder) sc.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
		if (cb == null){
			containerSetupScript.prepare("", sc);//no jdonramework.xml, only have annotation
			cb = (ContainerRegistryBuilder) sc.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
		}
		ContainerWrapper containerWrapper = null;
		try {			
			if (!cb.isKernelStartup()) {
				containerSetupScript.startup(sc);
			}
			containerWrapper = cb.getContainerWrapper();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] not find jdonFramework configuration file", module);
		}
		return containerWrapper;
	}
	
	
}
