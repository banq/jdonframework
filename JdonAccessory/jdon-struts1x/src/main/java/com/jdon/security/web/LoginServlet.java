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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jdon.util.Debug;
import com.jdon.util.RequestUtil;
import com.jdon.util.StringUtil;

/**
 * web.xml: <servlet> <servlet-name>jaaslogin</servlet-name>
 * <servlet-class>com.jdon.security.web.LoginServlet</servlet-class>
 * <init-param> <param-name>login</param-name> <param-value>/account/login.jsp</param-value>
 * </init-param> <init-param> <param-name>logout</param-name>
 * <param-value>/account/logout.jsp</param-value> </init-param>
 * <load-on-startup>2</load-on-startup> </servlet>
 * 
 * <servlet-mapping> <servlet-name>jaaslogin</servlet-name>
 * <url-pattern>/jaaslogin</url-pattern> </servlet-mapping>
 * 
 * <login-config> <auth-method>FORM</auth-method> <form-login-config>
 * <form-login-page>/jaaslogin</form-login-page>
 * <form-error-page>/account/login_error.jsp</form-error-page>
 * </form-login-config> </login-config>
 * 
 * login.jsp: <form method="POST" action="<%=request.getContextPath()%>/login"
 * ..... </form>
 * 
 * logout url: /login?logout
 * 
 * @author banq
 * @version 1.0
 */
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9179143247292671107L;

	private final static String module = LoginServlet.class.getName();

	public final static String form_login_page_param = "login";

	public final static String form_error_page_param = "login_error";

	public final static String logout_param = "logout";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Debug.logVerbose("[JdonFramework]enter LoginServlet", module);
		initCharacterEncoding(request, response);
		String username = request.getParameter("j_username");
		String password = request.getParameter("j_password");
		if ((username != null) && (password != null)) {
			Debug.logVerbose("[JdonFramework] username and password is not null", module);
			if (request.getParameter("rememberMe") != null) {
				saveCookie(username, password, request, response);
			} else
				deleteAllCookie(request, response);
		} else {
			Debug.logVerbose("[JdonFramework] check cookie", module);
			if (request.getParameterMap().containsKey(logout_param)) {// /login?logout
				logout(request, response);
				return;
			} else {// call /login
				username = CookieUtil.getUsername(request);
				password = CookieUtil.getPassword(request);
				Debug.logVerbose("[JdonFramework]get username from cookie username=" + username, module);
				if ((username == null) || (password == null)) {// no cookie,
					// push
					// login.jsp
					forwardLogin(request, response);
					return;
				}
			}
		}
		String route = request.getContextPath() + "/j_security_check?j_username=" + username + "&j_password=" + password;
		Debug.logVerbose("[JdonFramework] forward " + route, module);
		response.sendRedirect(response.encodeRedirectURL(route));
	}

	private void initCharacterEncoding(HttpServletRequest request, HttpServletResponse response) {
		if (request.getCharacterEncoding() != null) {
			// response.setCharacterEncoding(request.getCharacterEncoding());
		} else {
			// response.setCharacterEncoding("UTF-8");
		}
	}

	private void saveCookie(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		Debug.logVerbose("[JdonFramework] save cookie", module);
		RequestUtil.setCookie(response, "rememberMe", "true", "/");
		RequestUtil.setCookie(response, "username", StringUtil.encodeString(username), "/");
		RequestUtil.setCookie(response, "password", StringUtil.encodeString(password), "/");
	}

	private void deleteAllCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie rememberMe = RequestUtil.getCookie(request, "rememberMe");
		if (rememberMe != null)
			RequestUtil.deleteCookie(response, rememberMe, "/");
		Cookie userCookie = RequestUtil.getCookie(request, "username");
		if (userCookie != null)
			RequestUtil.deleteCookie(response, userCookie, "/");
		Cookie passCookie = RequestUtil.getCookie(request, "password");
		if (passCookie != null)
			RequestUtil.deleteCookie(response, passCookie, "/");

	}

	private void logout(HttpServletRequest request, HttpServletResponse response) {
		Debug.logVerbose("[JdonFramework]logout, session.invalidate ", module);
		try {
			request.getSession().invalidate();
			deleteAllCookie(request, response);
			String logoutUrl = this.getInitParameter(logout_param);
			Debug.logVerbose("[JdonFramework]delete all cookie, push logout jsp=" + logoutUrl, module);
			// request.getRequestDispatcher(logoutUrl).forward(request,
			// response);
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + logoutUrl));
		} catch (IOException e) {
			Debug.logError(e, module);
		}
	}

	private void forwardLogin(HttpServletRequest request, HttpServletResponse response) {
		String loginUrl = this.getInitParameter(form_login_page_param);
		Debug.logVerbose("[JdonFramework] not found cookie= push login jsp=" + loginUrl, module);
		try {
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + loginUrl));
		} catch (IOException e) {
			Debug.logError(e, module);
		}
	}

}
