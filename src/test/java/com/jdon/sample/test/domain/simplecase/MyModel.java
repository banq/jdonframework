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
package com.jdon.sample.test.domain.simplecase;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.domain.message.DomainMessage;
import com.jdon.sample.test.domain.simplecase.domainservice.MyModelService;

@Model
public class MyModel {

	private Long id;
	private String name;

	@Inject
	private MyModelDomainEvent myModelDomainEvent;

	@Inject
	private MyModelService myModelServiceCommand;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getName() {
		if (this.name == null) {
			DomainMessage message = myModelDomainEvent.asyncFindName(new FindNameEvent(this.id));
			this.name = (String) message.getBlockEventResult();
		}
		return name;
	}

	public void save() {
		myModelDomainEvent.save(new MyModelCreatedEvent(this.id, this.name));
	}

	public String sayHelloSynchronous() {
		return myModelServiceCommand.sayHello();
	}

}
