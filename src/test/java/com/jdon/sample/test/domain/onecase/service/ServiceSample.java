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
package com.jdon.sample.test.domain.onecase.service;

import com.jdon.annotation.Service;
import com.jdon.sample.test.domain.onecase.DomainEvent;
import com.jdon.sample.test.domain.onecase.repository.Repository;

@Service("serviceSample")
public class ServiceSample implements IServiceSample {

	private Repository repository;

	public ServiceSample(Repository repository) {
		super();
		this.repository = repository;
	}

	public String eventPointEntry(String name) {
		DomainEvent domainEvent = repository.getModel(new Long(100));
		domainEvent.setName(name);

		DomainEvent domainEvent2 = repository.getModel(new Long(100));
		System.out.print("name=" + domainEvent2.getName());

		domainEvent2.myMethod1();

		domainEvent2.myMethod();
		return domainEvent2.getName();

	}

}
