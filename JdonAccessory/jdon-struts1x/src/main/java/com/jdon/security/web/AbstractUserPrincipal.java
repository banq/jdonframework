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

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jdon.controller.model.Model;
import com.jdon.controller.pool.Poolable;
import com.jdon.util.Debug;

/**
 *  有关获得登陆用户的任何信息。
 *
 * @author banq
 */
public abstract class AbstractUserPrincipal implements UserPrincipal, Poolable {

  public final static String USERMODEL = "USERMODEL";

  private final static String module = AbstractUserPrincipal.class.getName();

  /**
   * 将UserModel从登陆的Principal中获取后，
   * Principal当用户访问被授权区域，会由Web容器自动产生。
   * 保存在HttpSession中，供其它Web层组件使用。
   * @param request
   */
  public Object getUserFromPrincipal(HttpServletRequest request) {
    Debug.logVerbose("[JdonFramework]--> enter getUserFromPrincipal", module);
    Principal principal = request.getUserPrincipal();
    if (principal == null) {
      Debug.logVerbose("[JdonFramework]principal is null, not login by JAAS", module);
      return null;
    }
    String username = principal.getName();
    Debug.logVerbose("[JdonFramework]--> find the logined username=" + username, module);
    HttpSession session = request.getSession();
    Object model =  session.getAttribute(USERMODEL);
    if (model == null) {
      model = findUserModelIFByName(request, username);
      session.setAttribute(USERMODEL, model);
    }
    return model;
  }

  public void saveModeltoSession(HttpServletRequest request, Object model) {
    HttpSession session = request.getSession();
    session.setAttribute(USERMODEL, model);
  }

  /**
   * 直接从Session获取，假设Session中已经存在
   * @param request HttpServletRequest
   * @return Model
   */
  public Object getUserFromSession(HttpServletRequest request) {
    Debug.logVerbose("[JdonFramework]--> enter getUserFromSession", module);
    HttpSession session = request.getSession();
    Object model = session.getAttribute(USERMODEL);
    return model;
  }

  public Object findUserModelIFByName(HttpServletRequest request,
                                            String key){
      return findUserModelByName(request, key);
  }
  
  public abstract Model findUserModelByName(HttpServletRequest request,
          String key);
}
