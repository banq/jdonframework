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

package com.jdon.controller.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.util.Debug;

public class XmlPojoServiceParser extends XmlParser {
	private final static String module = XmlPojoServiceParser.class.getName();

	public void parse(Element root, Map<String, TargetMetaDef> mps) throws Exception {
		Debug
				.logVerbose("[JdonFramework] enter XmlPojoServiceParser .",
						module);
		List services = root.getChildren("services");
		Iterator iter = services.iterator();
		while (iter.hasNext()) {
			Element service = (Element) iter.next();
			if (service.getChildren("pojoService") != null) {
				Iterator ii = service.getChildren("pojoService").iterator();
				while (ii.hasNext()) {
					Element pojoService = (Element) ii.next();
					parsePOJOServiceConfig(pojoService, mps);
				}
			}
			if (service.getChildren("component") != null) {
				Iterator ii = service.getChildren("component").iterator();
				while (ii.hasNext()) {
					Element pojoService = (Element) ii.next();
					parsePOJOServiceConfig(pojoService, mps);
				}
			}
		}
	}

	/**
	 * parse POJOService Config
	 * @param pojoService Element
	 * @param mps Map
	 * @throws Exception
	 */
	private void parsePOJOServiceConfig(Element pojoService, Map<String, TargetMetaDef> mps)
			throws Exception {
		String name = pojoService.getAttributeValue("name");
		String className = pojoService.getAttributeValue("class");
		Debug.logVerbose("[JdonFramework] pojoService/component name=" + name
				+ " class=" + className, module);

		if ((className == null) || (className.equals("")))
			throw new Exception("className is null ");

		List mappings = pojoService.getChildren("constructor");
		String[] constructors = null;
		if ((mappings != null) && (mappings.size() != 0)) {
			Debug.logVerbose("[JdonFramework] constructor parameters number:"
					+ mappings.size() + " for pojoservice " + name, module);
			constructors = new String[mappings.size()];
			int j = 0;
			Iterator i = mappings.iterator();
			while (i.hasNext()) {
				Element constructor = (Element) i.next();
				String value = constructor.getAttributeValue("value");
				Debug.logVerbose("[JdonFramework] pojoService constructor="
						+ value, module);
				constructors[j] = value;
				j++;
			}
		}

		POJOTargetMetaDef pojoMetaDef = null;
		if (constructors != null)
			pojoMetaDef = new POJOTargetMetaDef(name, className, constructors);
		else
			pojoMetaDef = new POJOTargetMetaDef(name, className);
		mps.put(name, pojoMetaDef);

	}
}
