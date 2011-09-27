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

package com.jdon.controller.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.DistributedTargetMetaDef;
import com.jdon.bussinessproxy.meta.EJBTargetMetaDef;
import com.jdon.util.Debug;

public class XmlServiceParser extends XmlParser {
	private final static String module = XmlServiceParser.class.getName();

	public void parse(Element root, Map<String, TargetMetaDef> mps) throws Exception {
		List services = root.getChildren("services");
		Iterator iter = services.iterator();
		while (iter.hasNext()) {
			Element service = (Element) iter.next();
			if (service.getChildren("ejbService") != null) {
				Iterator i = service.getChildren("ejbService").iterator();
				while (i.hasNext()) {
					Element ejbService = (Element) i.next();
					parseEJBServiceConfig(ejbService, mps);
				}
			}
		}
	}

	private void parseEJBServiceConfig(Element ejbService, Map<String, TargetMetaDef> mps)
			throws Exception {
		String name = ejbService.getAttributeValue("name");
		Debug.logVerbose("[JdonFramework] ejbService name=" + name, module);

		Element jndiE = ejbService.getChild("jndi");
		String jndiName = jndiE.getAttributeValue("name");

		Element ejbLobjectE = ejbService.getChild("ejbLocalObject");

		Element ejbHobjectE = ejbService.getChild("ejbHomeObject");
		Element ejbRobjectE = ejbService.getChild("ejbRemoteObject");
		Element interfaceE = ejbService.getChild("interface");

		DistributedTargetMetaDef eJBMetaDef = null;
		if (ejbLobjectE != null) {//local EJB
			eJBMetaDef = new EJBTargetMetaDef(name, jndiName, ejbLobjectE
					.getAttributeValue("class"));
			Debug.logVerbose("[JdonFramework] jndiName=" + jndiName, module);
			Debug.logVerbose("[JdonFramework] local="
					+ eJBMetaDef.getClassName(), module);
			if (interfaceE != null) {
				eJBMetaDef.setInterfaceClass(interfaceE
						.getAttributeValue("class"));
			}
		} else if ((ejbRobjectE != null) && (ejbHobjectE != null)) {//remote EJB
			String homeClassName = ejbHobjectE.getAttributeValue("class");//ejbHome class
			String remoteClassName = ejbRobjectE.getAttributeValue("class");//ejbRemote class
			eJBMetaDef = new EJBTargetMetaDef(name, jndiName, homeClassName,
					remoteClassName);
			Debug.logVerbose("[JdonFramework] jndiName=" + jndiName, module);
			Debug.logVerbose("[JdonFramework] ejbHome class=" + homeClassName,
					module);
			Debug.logVerbose("[JdonFramework] ejbRemote class="
					+ remoteClassName, module);
			if (interfaceE != null) {
				eJBMetaDef.setInterfaceClass(interfaceE
						.getAttributeValue("class"));
			}
		} else if (interfaceE != null) {//EJB3
			eJBMetaDef = new DistributedTargetMetaDef(name, jndiName,
					interfaceE.getAttributeValue("class"));
		} else {
			throw new Exception(
					"please config ejbLocalObject or ejbHomeObject/ejbRemoteObject or interface(EJB3) ");
		}
		mps.put(name, eJBMetaDef);

	}

}
