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

import com.jdon.controller.AppUtil;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.command.AComponentIF;
import com.jdon.sample.test.command.BModel;
import com.jdon.sample.test.command.TestCommand;
import com.jdon.sample.test.component.BInterface;
import com.jdon.sample.test.cqrs.AService;
import com.jdon.sample.test.cqrs.a.AggregateRootA;
import com.jdon.sample.test.domain.onecase.service.IServiceSample;
import com.jdon.sample.test.domain.simplecase.service.IServiceSampleTwo;
import com.jdon.sample.test.event.AI;
import com.jdon.sample.test.event.TestEvent;
import com.jdon.sample.test.xml.BBI;

public class SampleAppTest extends TestCase {

	AppUtil appUtil;

	protected void setUp() throws Exception {
		appUtil = new AppUtil("com.jdon.jdonframework.xml");
	}

	public void testXml() {
		BBI bb = (BBI) appUtil.getService("bb");
		Assert.assertEquals(bb.myDo(), 99);
	}

	public void testGetService() {
		BInterface b = (BInterface) appUtil.getService("b");
		Assert.assertEquals(b.bMethod(1), 10);
	}

	public void testEvent() {
		AppUtil appUtil = new AppUtil();
		AI a = (AI) appUtil.getService("producer");
		TestEvent te = a.ma();
		long start = System.currentTimeMillis();
		while (te.getResult() != 100) {
		}
		long stop = System.currentTimeMillis();

		Assert.assertEquals(te.getResult(), 100);
		System.out.print("ok " + "  " + (stop - start) + "\n");
	}

	public void testCommand() {
		AppUtil appUtil = new AppUtil();
		AComponentIF a = (AComponentIF) appUtil.getComponentInstance("producerforCommand");
		BModel bModel = new BModel("one");
		TestCommand testCommand = a.ma(bModel);
		long start = System.currentTimeMillis();
		while (testCommand.getOutput() != 199) {
		}
		long stop = System.currentTimeMillis();
		Assert.assertEquals(testCommand.getOutput(), 199);
		System.out.print("ok " + "  " + (stop - start) + "\n");
	}

	public void testDomainEvent() {

		IServiceSample serviceSample = (IServiceSample) appUtil.getService("serviceSample");
		Assert.assertEquals("hello-2", serviceSample.eventPointEntry("hello"));

	}

	public void testDomainEventSimple() {

		IServiceSampleTwo serviceSample = (IServiceSampleTwo) appUtil.getService("serviceSampleTwo");
		String res = (String) serviceSample.eventPointEntry();
		System.out.print(res);
		Assert.assertEquals(res, "Synchronous sayHello and Asynchronous eventMessage=100");

	}

	public void testOnEvent() {

		IServiceSampleTwo serviceSample = (IServiceSampleTwo) appUtil.getService("serviceSampleTwo");
		serviceSample.onEventTest();
		Assert.assertTrue(true);

	}

	public void testComponentsEvent() {

		AI a = (AI) appUtil.getService("producer");
		a.ma();

	}

	public void testCQRS() {
		AppUtil appUtil = new AppUtil();
		AService service = (AService) appUtil.getComponentInstance("aService");
		AggregateRootA aggregateRootA = service.getAggregateRootA("11");
		DomainMessage res = service.commandA("11", aggregateRootA, 100);

		long start = System.currentTimeMillis();
		int result = 0;
		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		if (res1.getBlockEventResult() != null)
			result = (Integer) res1.getBlockEventResult();

		long stop = System.currentTimeMillis();
		Assert.assertEquals(result, 400);
		System.out.print("\n test CQRS ok \n" + result + " " + (stop - start));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SampleAppTest sampleAppTest = new SampleAppTest();
		try {
			sampleAppTest.setUp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sampleAppTest.testCommand();

		sampleAppTest.testCQRS();
		sampleAppTest.testDomainEvent();
		sampleAppTest.testDomainEventSimple();
		sampleAppTest.testEvent();
		sampleAppTest.testOnEvent();
		sampleAppTest.testGetService();

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		appUtil.clear();
	}

}
