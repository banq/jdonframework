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
package com.jdon.sample.test.domain.simplecase.service;

import com.jdon.annotation.Service;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.domain.simplecase.IServiceSampleTwo;
import com.jdon.sample.test.domain.simplecase.MyModel;
import com.jdon.sample.test.domain.simplecase.dci.NameFinderContext;
import com.jdon.sample.test.domain.simplecase.dci.NameFinderRoleEventsImp;
import com.jdon.sample.test.domain.simplecase.repository.MyModelRepository;

@Service("serviceSampleTwo")
public class ServiceSampleTwo implements IServiceSampleTwo {

	private MyModelRepository repository;
	private NameFinderContext nameFinderContext;

	public ServiceSampleTwo(MyModelRepository repository, NameFinderContext nameFinderContext) {
		super();
		this.repository = repository;
		this.nameFinderContext = nameFinderContext;
	}

	public Object eventPointEntry() {
		MyModel myModel = repository.getModel(new Long(100));
		MyModel myModel2 = repository.getModel(new Long(100));
		return myModel.sayHelloNow() + myModel2.getName();

	}

	public String nameFinderContext() {
		MyModel myModel = repository.getModel(new Long(100));
		DomainMessage dm = (DomainMessage) nameFinderContext.interact(myModel, new NameFinderRoleEventsImp());
		return (String) dm.getEventResult();
	}

}
