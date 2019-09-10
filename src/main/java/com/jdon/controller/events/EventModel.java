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

package com.jdon.controller.events;

import com.jdon.controller.model.Model;

public class EventModel extends EventSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2684296939642991370L;
	protected Object model = null;

	public EventModel() {

	}

	public EventModel(Object model) {
		this.model = model;
	}

	public void setModelIF(Object model) {
		this.model = model;
	}

	public Object getModelIF() {
		return this.model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	// for old version
	public Model getModel() {
		return (Model) this.model;
	}

}
