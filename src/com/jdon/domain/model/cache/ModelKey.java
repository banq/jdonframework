/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.domain.model.cache;

/**
 * 用于实现Model缓存的Key
 * 
 * <p>
 * Copyright: Jdon.com Copyright (c) 2005
 * </p>
 * <p>
 * </p>
 * 
 * @author banq
 * @version JdonFramework 2005 v1.0
 */
public class ModelKey {

	private Object dataKey;
	private String formName;
	private Class modelClass;

	public ModelKey(Object dataKey, String formName) {
		this.dataKey = dataKey;
		this.formName = formName;
	}

	public ModelKey(Object dataKey, Class modelClass) {
		this.dataKey = dataKey;
		this.modelClass = modelClass;
	}

	public Object getDataKey() {
		return dataKey;
	}

	public String getFormName() {
		return formName;
	}

	public Class getModelClass() {
		return modelClass;
	}

	public void setDataKey(Object dataKey) {
		this.dataKey = dataKey;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public void setModelClass(Class modelClass) {
		this.modelClass = modelClass;
	}

	/**
	 * 最好不使用本方法，容易和真正的CacheKey混淆
	 * 
	 * @return String
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder("ModelKey include: dataKey=");
		buf.append(dataKey.toString());
		if (modelClass != null)
			buf.append(" modelClass =").append(modelClass);
		else if (formName != null)
			buf.append(" formName =").append(formName);
		return buf.toString();
	}

}
