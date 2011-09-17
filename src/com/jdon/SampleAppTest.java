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
package com.jdon;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.jdon.container.startup.ContainerSetupScript;
import com.jdon.controller.AppUtil;
import com.jdon.controller.context.application.Application;
import com.jdon.sample.test.component.BInterface;
import com.jdon.sample.test.dci.RepositoryContext;
import com.jdon.sample.test.domain.onecase.IServiceSample;
import com.jdon.sample.test.domain.simplecase.IServiceSampleTwo;

public class SampleAppTest extends TestCase {
	ContainerSetupScript css = new ContainerSetupScript();
	Application da;

	AppUtil appUtil;

	protected void setUp() throws Exception {
		da = new Application();
		css.prepare("com.jdon.jdonframework.xml", da);
		appUtil = new AppUtil("com.jdon.jdonframework.xml");
	}

	public void testGetService() {

		BInterface b = (BInterface) appUtil.getService("b");
		Assert.assertEquals(b.bMethod(1), 10);

	}

	public void testDomainEvent() {

		IServiceSample serviceSample = (IServiceSample) appUtil.getService("serviceSample");
		Assert.assertTrue(serviceSample.eventPointEntry("hello"));

	}

	public void testDomainEventSimple() {

		IServiceSampleTwo serviceSample = (IServiceSampleTwo) appUtil.getService("serviceSampleTwo");
		String res = (String) serviceSample.eventPointEntry();
		System.out.print(res);
		Assert.assertEquals(res, "sayHelloeventMessage=100");

	}

	public void testDCIDomainEvent() {

		IServiceSampleTwo serviceSample = (IServiceSampleTwo) appUtil.getService("serviceSampleTwo");
		String res = (String) serviceSample.nameFinderContext();
		System.out.print(res);
		Assert.assertEquals(res, "eventMessage=100");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ContainerSetupScript css = new ContainerSetupScript();
		Application da = new Application();

		css.prepare("com.jdon.jdonframework.xml", da);
		AppUtil appUtil = new AppUtil("com.jdon.jdonframework.xml");
		RepositoryContext repositoryContext = (RepositoryContext) appUtil.getComponentInstance("repositoryContext");
		repositoryContext.interact();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		appUtil.clear();
	}
}
