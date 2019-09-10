/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.container.builder;

import com.jdon.container.ContainerWrapper;

/**
 * the container builder
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public interface ContainerRegistryBuilder {

	String APPLICATION_CONTEXT_ATTRIBUTE_NAME = "ContainerBuilder";

	public void registerAppRoot(String configureFileName) throws Exception;

	public void registerComponents() throws Exception;

	public void registerAspectComponents() throws Exception;

	public void registerUserService() throws Exception;

	public void startApp();

	public void stopApp();

	public ContainerWrapper getContainerWrapper();

	public void setKernelStartup(boolean startup);

	public boolean isKernelStartup();

	public void doAfterStarted() throws Exception;
}
