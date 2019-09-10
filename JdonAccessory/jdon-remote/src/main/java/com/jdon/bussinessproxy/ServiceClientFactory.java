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

import com.jdon.util.Debug;
import com.jdon.bussinessproxy.remote.auth.AuthException;
import com.jdon.bussinessproxy.remote.http.HttpServerParam;
/**
 *
 * 远程客户端调用本类，通过具体实现进行http访问主机。
 *
 * @author banq
 */
public abstract class ServiceClientFactory {

  private final static String module = ServiceClientFactory.class.getName();

  private static Object initLock = new Object();
  private static String className =
      "com.jdon.bussinessproxy.remote.ServiceHTTPImp";
  private static ServiceClientFactory factory = null;

  public static ServiceClientFactory getInstance() {
    if (factory == null) {
      synchronized (initLock) {
        if (factory == null) {
          try {
            //Load the class and create an instance.
            Class c = Class.forName(className);
            factory = (ServiceClientFactory) c.newInstance();
          } catch (Exception e) {
            Debug.logError("[JdonFramework] get factory instance error:" + e, module);
            return null;
          }
        }
      }
    }
    return factory;
  }

  public abstract void setHttpServerParam(HttpServerParam httpServerParam);

  public abstract String login(String loginName, String password) throws AuthException;

  public abstract Object getService(TargetMetaDef targetMetaDef);




}
