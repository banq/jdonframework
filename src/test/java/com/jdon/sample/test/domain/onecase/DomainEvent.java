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
package com.jdon.sample.test.domain.onecase;

import com.jdon.annotation.Introduce;
import com.jdon.annotation.Model;
import com.jdon.annotation.model.Send;
import com.jdon.domain.message.DomainMessage;

@Model
@Introduce("message")
public class DomainEvent {
	private Long id;
	private String name;

	public DomainEvent() {
		super();
	}

	public DomainEvent(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// decorator for myMethod,
	public void myMethod1() {
		myMethod();
	}

	// async call @Component("mychannel") and @Consumer("mychannel")
	@Send("mychannel")
	public DomainMessage myMethod() {
		DomainMessage em = new DomainMessage(this.name);
		return em;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
