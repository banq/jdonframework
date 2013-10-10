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
package com.jdon.sample.test.cqrs;

import junit.framework.Assert;

import com.jdon.controller.AppUtil;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.cqrs.a.AggregateRootA;

public class TestMain {

	public static void main(String[] args) {
		AppUtil appUtil = new AppUtil();
		AService service = (AService) appUtil.getComponentInstance("aService");
		AggregateRootA aggregateRootA = service.getAggregateRootA("11");
		DomainMessage res = service.commandA("11", aggregateRootA, 100);

		long start = System.currentTimeMillis();
		int result = 0;
		DomainMessage res1 = (DomainMessage) res.getBlockEventResult();
		if (res1 != null && res1.getBlockEventResult() != null)
			result = (Integer) res1.getBlockEventResult();

		long stop = System.currentTimeMillis();
		Assert.assertEquals(result, 400);
		System.out.print("\n ok \n" + result + " time:" + (stop - start));
	}
}
