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
package com.jdon.controller.service;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.RequestWrapper;

/**
 * excute the service and get the run result
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public interface Service {

	/**
	 * this method will create httpsession
	 * 
	 * @param name
	 * @param methodMetaArgs
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Object execute(String name, MethodMetaArgs methodMetaArgs, RequestWrapper request) throws Exception;

	public Object execute(TargetMetaDef targetMetaDef, MethodMetaArgs methodMetaArgs, RequestWrapper request) throws Exception;

	/**
	 * this method without httpsession and will disable all sesson function.
	 * 
	 * @param name
	 * @param methodMetaArgs
	 * @param acw
	 * @return
	 * @throws Exception
	 */
	public Object execute(String name, MethodMetaArgs methodMetaArgs, AppContextWrapper acw) throws Exception;

	public Object execute(TargetMetaDef targetMetaDef, MethodMetaArgs methodMetaArgs, AppContextWrapper acw) throws Exception;

}
