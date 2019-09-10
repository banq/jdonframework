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

package com.jdon.bussinessproxy.remote;


import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.Map;

import com.jdon.bussinessproxy.ServiceClientFactory;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.bussinessproxy.remote.auth.AuthException;
import com.jdon.bussinessproxy.remote.auth.Authenticator;
import com.jdon.bussinessproxy.remote.http.HttpClient;
import com.jdon.bussinessproxy.remote.http.HttpServerParam;
import com.jdon.util.Debug;


public class ServiceHTTPImp extends ServiceClientFactory {

  private final static String module = ServiceHTTPImp.class.getName();
  private final static HttpClient httpClient =  HttpClient.getInstance();
  private final static Map _proxyCache = new Hashtable();

  public void setHttpServerParam(HttpServerParam httpServerParam) {
     httpClient.setHttpServerParam(httpServerParam);
  }

  /**
   * 首先从缓冲中获得代理实例，如果没有，通过动态代理生成。
   * @param EJBDefinition
   * @return
   */
  public Object getService(TargetMetaDef targetMetaDef) {

    Debug.logVerbose("[JdonFramework] --> enter getService from dynamicProxy", module);

    Object dynamicProxy = _proxyCache.get(targetMetaDef);
    if (dynamicProxy == null) {
      dynamicProxy = getServiceFromProxy(targetMetaDef);
      _proxyCache.put(targetMetaDef, dynamicProxy);
    }
    return dynamicProxy;

  }

  /**
   * 登陆验证
   * @param loginName
   * @param password
   * @throws AuthException
   */
  public String login(String loginName, String password) throws AuthException{
    String loginResult = null;
    try{
      Debug.logVerbose("[JdonFramework] --> enter login", module);

      TargetMetaDef targetMetaDef = new POJOTargetMetaDef("authenticator",
           "com.jdon.bussinessproxy.remote.auth.Authenticator");

      Authenticator authenticator = (Authenticator) getService(targetMetaDef);
      loginResult = authenticator.login(loginName, password);
    }catch(Exception e){
      throw new AuthException(e);
    }
    return loginResult;

  }
  /**
   * 通过动态代理获得代理实例
   * @param EJBDefinition
   * @return
   */
  public Object getServiceFromProxy(TargetMetaDef targetMetaDef) {

    RemoteInvocationHandler handler = null;
    Object dynamicProxy = null;

    try {
      Debug.logVerbose("[JdonFramework] ---> create event new ProxyInstance", module);
      handler = new RemoteInvocationHandler(targetMetaDef);

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      Class serviceClass = classLoader.loadClass(targetMetaDef.getClassName());

      dynamicProxy =
          Proxy.newProxyInstance(
          classLoader,
          new Class[] {serviceClass},
          handler);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return dynamicProxy;

  }


}
