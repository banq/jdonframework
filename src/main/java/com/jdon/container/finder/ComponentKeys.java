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
package com.jdon.container.finder;

/**
 * the important keys in container.xml
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public interface ComponentKeys {

	String WEBSERVICE = "webService";

	String WEBSERVICE_FACTORY = "webServiceFactory";

	String VISITOR_FACTORY = "visitorFactory";

	String SESSIONCONTEXT_SETUP = "sessionContextSetup";

	String INTERCEPTOR_CHAIN = "interceptorsChain";

	String INSTANCE_CACHE = "instanceCache";

	String INTERCEPTOR_CHAIN_FACTORY = "advisorChainFactory";

	String DOMAIN_PROXY_FACTORY = "domainProxyFactory";

	/**
	 * the SERVICE_METALOADER_NAME value must be the value of container.xml
	 * 
	 */
	String SERVICE_METALOADER_NAME = "targetMetaDefLoader";

	/**
	 * the SERVICE_METAHOADER_NAME value must be the value of container.xml
	 * 
	 */
	String SERVICE_METAHOLDER_NAME = "targetMetaDefHolder";

	/**
	 * the AOP_CLIENT value must be the value of container.xml
	 * 
	 */
	String AOP_CLIENT = "aopClient";

	String MODEL_MANAGER = "modelManager";

	String PROXYINSTANCE_FACTORY = "proxyInstanceFactoryVisitable";

	String TARGETSERVICE_FACTORY = "targetServiceFactoryVisitable";

	String SESSIONCONTEXT_FACTORY = "sessionContextFactoryVisitable";

}
