package com.jdon.container.factory;

import java.util.Map;

import com.jdon.container.config.ContainerComponents;
import com.jdon.container.config.ContainerComponentsXmlLoader;
import com.jdon.container.config.aspect.AopInterceptorsXmlLoader;
import com.jdon.controller.config.XmlParser;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;

public class ContainerLoaderXML  {
	public final static String module = ContainerLoaderXML.class.getName();

	private static final String DEFAULT_CONTAINER_CONFIGURE_FILENAME = "container.xml";
	private static final String DEFAULT_ASPECT_CONFIGURE_FILENAME = "aspect.xml";

	private static final String USER_CONTAINER_CONFIGURE_FILENAME = "mycontainer.xml";
	private static final String USER_ASPECT_CONFIGURE_FILENAME = "myaspect.xml";

	private static final String CONTAINER_CONFIG_PARAM = "containerConfigure";
	private static final String ASPECT_CONFIG_PARAM = "aspectConfigure";


	public ContainerLoaderXML() {
	}
	

	/* (non-Javadoc)
	 * @see com.jdon.container.factory.ContainerLoader#loadAllContainerConfig(com.jdon.controller.context.AppContextWrapper)
	 */
	public ContainerComponents loadAllContainerConfig(AppContextWrapper context) {
		Debug.logVerbose("[JdonFramework] 1. read container components from:" + DEFAULT_CONTAINER_CONFIGURE_FILENAME, module);
		ContainerComponents configComponents = loadBasicComponents(DEFAULT_CONTAINER_CONFIGURE_FILENAME);
		loadContextContainerConfig(configComponents, context);
		loadUserContainerConfig(configComponents, context);
		return configComponents;
	}

	public void loadContextContainerConfig(ContainerComponents configComponents, AppContextWrapper context) {
		String container_configFile = context.getInitParameter(CONTAINER_CONFIG_PARAM);
		if (container_configFile == null)
			return;
		Debug.logVerbose("[JdonFramework] 2. read container components from:" + container_configFile, module);
		ContainerComponents appconfigComponents = loadBasicComponents(container_configFile);
		configComponents.addComponents(appconfigComponents.getComponents());
	}

	public void loadUserContainerConfig(ContainerComponents configComponents, AppContextWrapper context) {
		// read user fraemwork's configure
		Debug.logVerbose("[JdonFramework] 3. read container components from:" + USER_CONTAINER_CONFIGURE_FILENAME, module);
		ContainerComponents userconfigComponents = loadBasicComponents(USER_CONTAINER_CONFIGURE_FILENAME);
		configComponents.addComponents(userconfigComponents.getComponents());
	}

	/* (non-Javadoc)
	 * @see com.jdon.container.factory.ContainerLoader#loadAllAspectConfig(com.jdon.controller.context.AppContextWrapper)
	 */
	public ContainerComponents loadAllAspectConfig(AppContextWrapper context) {
		// read user fraemwork's configure
		Debug.logVerbose("[JdonFramework]1. read apspect interceptors from:" + USER_ASPECT_CONFIGURE_FILENAME, module);
		ContainerComponents aspectConfigComponents = loadAspectComponents(context, USER_ASPECT_CONFIGURE_FILENAME);

		loadContextAspectConfig(aspectConfigComponents, context);
		loadUserAspectConfig(aspectConfigComponents, context);
		return aspectConfigComponents;
	}

	public void loadContextAspectConfig(ContainerComponents aspectConfigComponents, AppContextWrapper context) {
		String aspect_configFile = context.getInitParameter(ASPECT_CONFIG_PARAM);
		if (aspect_configFile == null) {
			return;
		}
		Debug.logVerbose("[JdonFramework]2. read apspect interceptors from:" + aspect_configFile, module);
		ContainerComponents appaspectConfigComponents2 = loadAspectComponents(context, aspect_configFile);
		aspectConfigComponents.addComponents(appaspectConfigComponents2.getComponents());

	}

	public void loadUserAspectConfig(ContainerComponents aspectConfigComponents, AppContextWrapper context) {
		Debug.logVerbose("[JdonFramework] 3. read apspect interceptors from:" + DEFAULT_ASPECT_CONFIGURE_FILENAME, module);
		ContainerComponents aspectConfigComponents3 = loadAspectComponents(context, DEFAULT_ASPECT_CONFIGURE_FILENAME);
		aspectConfigComponents.addComponents(aspectConfigComponents3.getComponents());
		Debug.logVerbose("[JdonFramework] aspectConfigComponents size:" + aspectConfigComponents.size(), module);

	}
	
	public ContainerComponents loadBasicComponents(String configFile) {
		// load container.xml and parse it into BasicComponents
		XmlParser xmlParser = new ContainerComponentsXmlLoader();
		Map components = xmlParser.load(configFile);
		Debug.logVerbose("[JdonFramework] found components in " + configFile + " size:" + components.size(), module);

		ContainerComponents configComponents = new ContainerComponents(components);
		return configComponents;
	}

	public ContainerComponents loadAspectComponents(AppContextWrapper context, String configFile) {
		// load aspect.xml and parse it into Aspect Components
		XmlParser xmlParser = null;
		if (context != null)
			xmlParser = new AopInterceptorsXmlLoader(context);
		else
			xmlParser = new AopInterceptorsXmlLoader();
			
		Map components = xmlParser.load(configFile);
		Debug.logVerbose("[JdonFramework] found aspect components in " + configFile + " size:" + components.size(), module);

		ContainerComponents configComponents = new ContainerComponents(components);
		return configComponents;
	}

}
