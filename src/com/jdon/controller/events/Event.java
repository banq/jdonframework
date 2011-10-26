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

package com.jdon.controller.events;

import javax.servlet.http.HttpSession;

/**
 * 
 * Event是Web层和Ejb层之间的传递者 包含actioName 是WEB层的action名称； actionType是action的具体类型。
 * 例如：com.jdon.security.service.SaveSignUpAction是actionName
 * 在这个Action中，可能会发生几个类型，如新增 创建 或修改。
 * actionName是大类，actionType是小类，这样Event就可以包含所有的事件
 * 
 * Errors是保存处理结果的出错信息。可以和前台的struts的Application.properies 信息设置一致。
 * 
 * @author banq
 */
public interface Event extends java.io.Serializable {

	public static final int VIEW = 1;
	public static final int EDIT = 2;
	public static final int CREATE = 3;
	public static final int DELETE = 4;
	public static final int ADD = 5;
	public static final int REMOVE = 6;
	public static final int UPDATE = 7;

	// 响应客户端的actionName
	public void setActionName(String actionName);

	public String getActionName();

	// 响应客户端的ActionType
	public void setActionType(int actionType);

	public int getActionType();

	// Event处理出错信息
	public void setErrors(String errors);

	public String getErrors();

	// 可能使用Session作为缓存，对于使用EJB Service是必须的，加快速度。
	public void setSession(HttpSession session);

	public HttpSession getSession();

	public String getEventName();

}
