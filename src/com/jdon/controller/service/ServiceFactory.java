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

package com.jdon.controller.service;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.RequestWrapper;

/**
 * get a service by its meta definition.
 * 
 * 面向客户端调用的服务接口
 * 
 * 
 */
public interface ServiceFactory {

	public Object getService(String name, AppContextWrapper acw);

	public Object getService(String name, RequestWrapper request);

	public Object getService(TargetMetaDef targetMetaDef, RequestWrapper request);

}
