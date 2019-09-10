/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.sample.test.command;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.OnCommand;

@Model
public class BModel {
	private String id;

	private int state = 100;

	public BModel(String id) {
		super();
		this.id = id;
	}

	@OnCommand("CommandmaTest")
	public void save(TestCommand testCommand) {
		this.state = testCommand.getInput() + state;
		testCommand.setOutput(state);

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
