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

import javax.servlet.http.*;

import com.jdon.util.RequestUtil;
import com.jdon.util.StringUtil;
import java.net.*;
import com.jdon.util.Debug;

public class CookieUtil {
  private final static String module = CookieUtil.class.getName();

  public static void deleteAllCookie(HttpServletRequest request,
                                     HttpServletResponse response) {
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

  public static void setUsername(HttpServletResponse response, String username) {

    RequestUtil.setCookie(response, "username",
                          StringUtil.encodeString(username),
                          "/");

  }

  public static String getUsername(HttpServletRequest request) {
    String username = "";
    try {
      Cookie userCookie = RequestUtil.getCookie(request, "username");
      username = (userCookie != null)
          ? StringUtil.decodeString(userCookie.getValue()) : null;
    } catch (Exception e) {
      System.err.print("getUsername from cookie" + e);
    }

    return username;
  }

  public static void setUPassword(HttpServletResponse response, String password) {
    RequestUtil.setCookie(response, "password",
                          StringUtil.encodeString(password),
                          "/");

  }

  public static String getPassword(HttpServletRequest request) {
    String password = "";
    try {
      Cookie passCookie = RequestUtil.getCookie(request, "password");
      password = (passCookie != null)
          ? StringUtil.decodeString(passCookie.getValue()) : null;
    } catch (Exception e) {
      System.err.print("getpassCookie from cookie" + e);
    }

    return password;

  }

  public static void setRememberMe(HttpServletResponse response) {
    RequestUtil.setCookie(response, "rememberMe", "true", "/");
  }

  public static String getRememberMe(HttpServletRequest request) {
    String rememberMe = "";
    try {
      Cookie rememberMeie = RequestUtil.getCookie(request, "rememberMe");
      rememberMe = (rememberMeie != null)
          ? URLDecoder.decode(rememberMeie.getValue(), "UTF-8") : null;
    } catch (Exception e) {
      System.err.print("getrememberMe from cookie" + e);
    }
    Debug.logVerbose("[JdonFramework]--> get RememberMe from cookier :" + rememberMe, module);
    return rememberMe;

  }

}
