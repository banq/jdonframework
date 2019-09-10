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
package com.jdon.domain.proxy;

import com.jdon.controller.model.Model;

public class ModelProxyWrapper extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object model;
	private Object modelproxy;

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Object getModelproxy() {
		return modelproxy;
	}

	public void setModelproxy(Object modelproxy) {
		this.modelproxy = modelproxy;
	}

}
