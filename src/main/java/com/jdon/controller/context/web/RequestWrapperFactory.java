/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.controller.context.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.ContextHolder;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.controller.context.SessionWrapper;

public class RequestWrapperFactory {

	/**
	 * create event HttpServletRequestWrapper with session supports. this method
	 * will create HttpSession.
	 * 
	 * @param request
	 * @return
	 */
	public static RequestWrapper create(HttpServletRequest request) {

		HttpSession session = request.getSession();
		AppContextWrapper acw = new ServletContextWrapper(session.getServletContext());
		SessionWrapper sw = new HttpSessionWrapper(session);
		ContextHolder contextHolder = new ContextHolder(acw, sw);

		return new HttpServletRequestWrapper(request, contextHolder);
	}

}
