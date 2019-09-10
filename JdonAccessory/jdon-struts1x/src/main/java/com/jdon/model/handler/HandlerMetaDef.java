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

package com.jdon.model.handler;

import com.jdon.model.config.ModelMapping;

public class HandlerMetaDef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6571346933023411516L;
	private String serviceRef;
	private String findMethod;
	private String createMethod;
	private String updateMethod;
	private String deleteMethod;
	private String initMethod;

	private ModelMapping modelMapping;

	/**
	 * ServiceMapping
	 * 
	 * @param aString
	 *            String
	 */

	public String getFindMethod() {
		return findMethod;
	}

	public String getServiceRef() {
		return serviceRef;
	}

	public String getUpdateMethod() {
		return updateMethod;
	}

	public String getDeleteMethod() {
		return deleteMethod;
	}

	public void setCreateMethod(String createMethod) {
		this.createMethod = createMethod;
	}

	public void setFindtMethod(String getMethod) {
		this.findMethod = getMethod;
	}

	public void setServiceRef(String serviceRef) {
		this.serviceRef = serviceRef;
	}

	public void setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
	}

	public void setDeleteMethod(String deleteMethod) {
		this.deleteMethod = deleteMethod;
	}

	public String getCreateMethod() {
		return createMethod;
	}

	/**
	 * @return Returns the initMethod.
	 */
	public String getInitMethod() {
		return initMethod;
	}

	/**
	 * @param initMethod
	 *            The initMethod to set.
	 */
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	public ModelMapping getModelMapping() {
		return modelMapping;
	}

	public void setModelMapping(ModelMapping modelMapping) {
		this.modelMapping = modelMapping;
	}

}
