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

package com.jdon.security.ejb;

import java.security.Principal;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;

import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.callback.SecurityAssociationHandler;

import com.jdon.util.Debug;

/**
 * please config Login.Module.Name in web.xml
 *
 * 该方法login只对EJB访问有效，login方法实行后，可以访问被授权的EJB了。
 * 该类适合肥客户端等通过RMI对EJB实行访问。
 * 1. 在login-config.xml配置ClientLoginModule（缺省）
 * 2. 在login-config.xml中配置Login.Module.Name ，如下，这样和自己的数据库
 *    实现了联系：
 *  <application-policy name = "SecurityRealm">
       <authentication>
          <login-module code = "org.jboss.security.auth.spi.DatabaseServerLoginModule"  flag = "required">
             <module-option name = "dsJndiName">java:/Security</module-option>
             <module-option name="principalsQuery">SELECT password FROM User WHERE name = ?</module-option>
             <module-option name="rolesQuery">SELECT RL.name, 'Roles' FROM role as RL, user as U ,  users_roles as RU WHERE U.userid = RU.userid and RU.roleid = RL.roleid  and U.name = ?</module-option>
             <module-option name = "managedConnectionFactoryName">jboss.jca:service=LocalTxCM,name=Security</module-option>
          </login-module>
       </authentication>
  </application-policy>
 * 3. 在客户端调用本login，即可实现对授权EJB访问。
 *
 *
 *
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p></p>
 * @author banq
 * @version 1.0
 */

public class LoginClientUtil {
  private final static String module = LoginClientUtil.class.getName();

  /**
   * 实现j_security_check功能
   * Login_Module_Name : SecurityRealm (Jboss login-config.xml)
   * @param username
   * @param password
   * @return boolean
   */
  public static boolean login(String username, String password,
                              String Login_Module_Name) {
    Subject subject = null;
    try {
      //jboss
      CallbackHandler pch = getJBossCallbackHandler(username, password);
      LoginContext loginCtx = new LoginContext(Login_Module_Name, pch);
      Debug.logVerbose("[JdonFramework] begin to login ", module);
      loginCtx.login();

      Debug.logVerbose("[JdonFramework] login successfully, subject=" + subject, module);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      Debug.logError(e, module);
      return false;
    }

  }

  private static CallbackHandler getJBossCallbackHandler(String username,
      String password) {
    SecurityAssociationHandler pch = new SecurityAssociationHandler();
    Principal user = getJBossPrincipal(username);
    pch.setSecurityInfo(user, password.toCharArray());
    return pch;
  }

  private static Principal getJBossPrincipal(String username) {
    return new SimplePrincipal(username);
  }


}
