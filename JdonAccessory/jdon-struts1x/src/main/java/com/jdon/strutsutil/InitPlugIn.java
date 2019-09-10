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

package com.jdon.strutsutil;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.PlugInConfig;

import com.jdon.container.access.xml.AppConfigureCollection;
import com.jdon.container.startup.ContainerSetupScript;
import com.jdon.controller.context.web.ServletContextWrapper;
import com.jdon.util.Debug;
import com.jdon.util.FileLocator;
import com.jdon.util.StringUtil;

/**
 * JdonFramework configuration starter.
 * 
 * add these into struts_config.xml ： <plug-in
 * className="com.jdon.strutsutil.InitPlugIn"/>
 * 
 * @author banq
 * @version 1.0
 */

public class InitPlugIn implements PlugIn {
	public final static String module = InitPlugIn.class.getName();

	private ContainerSetupScript css = new ContainerSetupScript();

	private FileLocator fileLocator = new FileLocator();

	private ActionServlet servlet = null;

	public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {
		// Remember our associated configuration and servlet
		this.servlet = servlet;

		ServletContext sc = servlet.getServletContext();
		ServletContextWrapper scw = new ServletContextWrapper(sc);
		String config_file = "";
		PlugInConfig[] plugInConfigs = config.findPlugInConfigs();
		int length = plugInConfigs.length;
		for (int i = 0; i < length; i++) {
			Set entries = plugInConfigs[i].getProperties().entrySet();
			Iterator iter = entries.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				if (AppConfigureCollection.CONFIG_NAME.equals(entry.getKey())) { // 可能有多个配置
					config_file = (String) entry.getValue();
					String[] configs = StringUtil.split(config_file, ",");
					for (int j = 0; j < configs.length; j++) {
						if (checkExsit(configs[j])) {
							Debug.logVerbose(" found and start config.:" + configs[j], module);
							css.prepare(configs[j], scw);
						}
					}

				}
			}
		}
	}

	public boolean checkExsit(String config_file) {
		boolean ret = false;
		InputStream xmlFile = null;
		try {
			xmlFile = fileLocator.getConfPathXmlStream(config_file);
			if (xmlFile != null)
				ret = true;
		} catch (Exception ex) {

		}
		return ret;
	}

	public void destroy() {
		css.destroyed(new ServletContextWrapper(servlet.getServletContext()));
		servlet = null;
	}

}
