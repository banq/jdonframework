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

package com.jdon.container.builder;

import com.jdon.container.ContainerWrapper;
import com.jdon.util.Debug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * container director register order: 1. register all components in
 * container.xml 2. register all components in aspect.xml 3. startup above all
 * components 4. register user services in jdonframework.xml
 * 
 * 
 * @author banq
 */
public class ContainerDirector {
	public final static String module = ContainerDirector.class.getName();
	private final static Logger logger = LogManager.getLogger("JdonFramework");

	private final ContainerRegistryBuilder cb;

	public ContainerDirector(ContainerRegistryBuilder cb) {
		this.cb = cb;
	}

	/**
	 * prepare the applicaition configure files
	 * 
	 * @param configureFileName
	 */
	public synchronized void prepareAppRoot(String configureFileName) throws Exception {
		if (!cb.isKernelStartup()) {
			cb.registerAppRoot(configureFileName);
			logger.info(configureFileName + " is ready.");
		}
	}

	public void startup() throws StartupException {
		Debug.logVerbose("[JdonFramework] <======== JdonFramework beigin to startup =========>", module);
		if (!cb.isKernelStartup())
			synchronized (cb) {
				if (!cb.isKernelStartup()) {
					try {
						Debug.logVerbose("[JdonFramework] <------ register the basic components in container.xml ------> ", module);
						cb.registerComponents();
						ContainerWrapper cw = cb.getContainerWrapper();
						Debug.logVerbose("[JdonFramework] <------ started micro container ------> ", module);
						cw.start();

						cb.setKernelStartup(true);

						Debug.logVerbose("[JdonFramework] <------ register the pojo services in application's xml ------> ", module);
						cb.registerUserService();

						Debug.logVerbose("[JdonFramework] <------ register the aspect components in container.xml ------> ", module);
						cb.registerAspectComponents();

						cb.startApp();

						cb.doAfterStarted();
						logger.info("Jdon Framework started successfully! ");

						cw.setStart(true);
					} catch (Exception ex) {
						Debug.logError("[JdonFramework] startup container error: " + ex, module);
						throw new StartupException();
					}
				}
			}

	}

	public void shutdown() throws StartupException {
		Debug.logVerbose("[JdonFramework] <======== JdonFramework beigin to shutdown =========>", module);
		if (cb.isKernelStartup())
			synchronized (cb) {
				if (cb.isKernelStartup()) {
					try {
						ContainerWrapper cw = cb.getContainerWrapper();
						cb.stopApp();
						cw.stop();
						cw.setStart(false);
						cb.setKernelStartup(false);
						logger.info("Jdon Framework shutdown successfully! ");
					} catch (Exception ex) {
						Debug.logError("[JdonFramework] shutdown container error: " + ex, module);
						throw new StartupException();
					}
				}
			}
	}

}
