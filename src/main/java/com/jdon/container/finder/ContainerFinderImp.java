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

/**
 * Container lookup from outside the container.
 * 
 */
public class ContainerFinderImp implements ContainerFinder {

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
		init(sc, cb);
		launch(sc, cb);
		return cb.getContainerWrapper();
	}

	private void init(AppContextWrapper sc, ContainerRegistryBuilder cb) {
		if (cb == null)
			prepare(sc, cb);
	}

	private void launch(AppContextWrapper sc, ContainerRegistryBuilder cb) {
		if (!cb.isKernelStartup())
			start(sc);
	}

	private void prepare(AppContextWrapper sc, ContainerRegistryBuilder cb) {
		ContainerSetupScript containerSetupScript = new ContainerSetupScript();
		// no jdonramework.xml, only have annotation
		containerSetupScript.prepare("", sc);
		cb = (ContainerRegistryBuilder) sc.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
	}

	private void start(AppContextWrapper sc) {
		ContainerSetupScript containerSetupScript = new ContainerSetupScript();
		containerSetupScript.startup(sc);
	}

}
