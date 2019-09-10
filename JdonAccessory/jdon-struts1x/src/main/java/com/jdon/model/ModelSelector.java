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

package com.jdon.model;

import java.util.Collection;

import com.jdon.controller.model.Model;

/**
 * 本类配合html:select 和html:options使用
 * 
 * 例如： <html:optionsCollection name="modelSelector" property="modelList"
 * value="catId" label="name"/>
 * 
 * @author banq
 */
public class ModelSelector extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4716843792943239349L;

	// 选中的Model的关键字
	private String keySelected = null;

	// 节点的关键字
	private Collection modelList;

	public ModelSelector(String keySelected, Collection modelList) {
		this.keySelected = keySelected;
		this.modelList = modelList;
	}

	public String getKeySelected() {
		return keySelected;
	}

	public Collection getModelList() {
		return modelList;
	}

	public void setKeySelected(String keySelected) {
		this.keySelected = keySelected;
	}

	public void setModelList(Collection modelList) {
		this.modelList = modelList;
	}

}
