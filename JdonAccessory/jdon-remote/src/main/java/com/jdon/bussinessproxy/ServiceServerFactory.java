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

package com.jdon.bussinessproxy;

import javax.servlet.http.HttpServletRequest;

import com.jdon.controller.WebAppUtil;

/**
 *
 * 本类为兼容旧版本
 *
 * @author banq
 */
public class ServiceServerFactory {

	private final static ServiceServerFactory ssf = new ServiceServerFactory();

	public static ServiceServerFactory getInstance() {
		return ssf;
	}

	private ServiceServerFactory() {
	}

	public Object getService(TargetMetaDef targetMetaDef,
			HttpServletRequest request) {
		Object service = null;
		try {
			service = WebAppUtil.getService(targetMetaDef, request);
		} catch (Exception ex) {

		}
		return service;
	}
}
