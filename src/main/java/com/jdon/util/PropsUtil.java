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

package com.jdon.util;

import java.io.InputStream;

import com.jdon.util.jdom.XMLProperties;

/**
 * @author banq
 */

public class PropsUtil {

	public final static String module = PropsUtil.class.getName();

	public static String ENCODING = "UTF-8";

	private XMLProperties properties;

	private FileLocator fileLocator = new FileLocator();

	public PropsUtil(String configureFile) {
		loadProperties(configureFile);
	}

	public void loadProperties(String configName) {
		InputStream pathCongfgName = fileLocator.getConfStream(configName);
		if (pathCongfgName == null) {
			System.out.println(" cann't load config file:-->" + configName);
			return;
		}
		this.properties = new XMLProperties(pathCongfgName);
	}

	public String getProperty(String name) {
		String res = properties.getProperty(name);
		if (res == null)
			res = "";
		return res;
	}

	public String[] getChildrenProperties(String name) {
		return properties.getChildrenProperties(name);
	}

	public String[] getChildrenValues(String name) {
		return properties.getChildrenPropertiesValues(name);
	}

	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}

}
