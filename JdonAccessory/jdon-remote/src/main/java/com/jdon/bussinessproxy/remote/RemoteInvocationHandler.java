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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.remote.auth.Authenticator;
import com.jdon.bussinessproxy.remote.http.HttpClient;
import com.jdon.util.Debug;

public class RemoteInvocationHandler implements InvocationHandler {

  private final static String module = RemoteInvocationHandler.class.getName();
  private final static HttpClient httpClient =  HttpClient.getInstance();

  private TargetMetaDef targetMetaDef = null;

  public RemoteInvocationHandler(TargetMetaDef targetMetaDef) {
    this.targetMetaDef = targetMetaDef;
  }

  public Object invoke(Object p_proxy, Method method, Object[] args) throws
      Throwable {
    Debug.logVerbose("[JdonFramework]method:" + method.getName(), module);

    if (method.getName().equals(Authenticator.AUTH_METHOD_NAME))
      return httpClient.invokeAuth(args);
    else
       return httpClient.invoke(targetMetaDef, method, args);
  }



}
