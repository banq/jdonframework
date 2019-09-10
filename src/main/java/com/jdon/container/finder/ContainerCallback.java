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
package com.jdon.container.finder;

import java.io.Serializable;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.pico.Startable;

/**
 * the components in container can call the container by this class; set this
 * class as components construtor parameter.
 * 
 * difference with ContainerFinder: ContainerFinder is call the container
 * outside the container
 * 
 * 
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class ContainerCallback implements Serializable, Startable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7709130409701965983L;

	public final static String NAME = "ContainerCallback";

	private ContainerWrapper containerWrapper;

	public ContainerCallback(ContainerWrapper containerWrapper) {
		this.containerWrapper = containerWrapper;
	}

	/**
	 * @return Returns the containerWrapper.
	 */
	public ContainerWrapper getContainerWrapper() {
		return containerWrapper;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		if (containerWrapper != null && containerWrapper.isStart())
			containerWrapper.stop();
		containerWrapper = null;

	}

}
