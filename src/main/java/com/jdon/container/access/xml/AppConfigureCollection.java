/*
 * Copyright 2003-2006 the original author or authors.
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
package com.jdon.container.access.xml;

import java.util.ArrayList;
import java.util.Collection;

/**
 * the collection for framework application: jdonframework.xml (
 * {@link com.jdon.container.builder.XmlContainerRegistry})
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * @see com.jdon.container.builder.XmlContainerRegistry
 * @see com.jdon.container.builder.DefaultContainerBuilder addAppConfigureFile
 * 
 */
public class AppConfigureCollection {
	public final static String CONFIG_NAME = "modelmapping-config";

	public final static String NAME = "AppConfigureFiles";

	/**
	 * the filename collection it's element is the String type, filename.
	 */
	private final Collection configList;

	public AppConfigureCollection() {
		configList = new ArrayList();
	}

	/**
	 * @param configList
	 */
	public AppConfigureCollection(Collection configList) {
		this.configList = configList;
	}

	public void addConfigList(String configureFileName) {
		configList.add(configureFileName);
	}

	/**
	 * @return Returns the configList.
	 */
	public Collection getConfigList() {
		return configList;
	}

}
