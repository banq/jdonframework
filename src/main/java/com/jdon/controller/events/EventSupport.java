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

package com.jdon.controller.events;

import javax.servlet.http.HttpSession;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 带有web层的Action name 和action type
 * 
 * </p>
 * 
 * @author banq
 */

public class EventSupport implements Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6226101656442750472L;
	protected String actionName = null;
	protected int actionType;
	protected String errors = null;
	private HttpSession session = null;

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public int getActionType() {
		return this.actionType;
	}

	public void setErrors(String errors) {
		if (this.errors == null) // 只记录第一个错误
			this.errors = errors;
	}

	public String getErrors() {
		return this.errors;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public HttpSession getSession() {
		return session;
	}

	public String getEventName() {
		return "";
	}

}
