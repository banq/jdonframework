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

package com.jdon.security.web;

import javax.servlet.http.HttpServletRequest;

import com.jdon.controller.model.Model;

/**

 * 该Sample需要两个落实：
 * 1. userPrincipal需要有具体实现
 * 2. getUserModelAfterLogin其实返回是UserModel
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p></p>
 * @author banq
 * @version 1.0
 */

public class LoginHandler {

	//将当前用户登陆后的UserModel实例放入HttpSession
	private AbstractUserPrincipal userPrincipal;

	//    private static UserPrincipal userPrincipal = new UserPrincipalImp();

	public LoginHandler(AbstractUserPrincipal userPrincipal) {
		this.userPrincipal = userPrincipal;

	}

	/**
	 * 获得登陆后的User相关信息
	 * 本方法必须在上面login执行后，或者用户通过j_security_check登陆后才可有效
	 * 首先从HttpSession中查询，HttpSession中资料是由getUserFromPrincipal
	 * 第一次执行生成的。
	 *
	 * @param request
	 * @return　UserModel
	 */
	public Object getUserModelAfterLogin(HttpServletRequest request) {
		Object model = userPrincipal.getUserFromSession(request);
		if (model == null)
			model = userPrincipal.getUserFromPrincipal(request);
		return (Model) model;
	}

	public AbstractUserPrincipal getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(AbstractUserPrincipal userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

}
