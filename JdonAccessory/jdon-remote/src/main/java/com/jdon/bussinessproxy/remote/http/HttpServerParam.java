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

package com.jdon.bussinessproxy.remote.http;

/**
 *
 * host port servletPath是必须设置的。
 *
 * </p>
 * @author banq
 */
public class HttpServerParam {

  /** Default Server Host*/
  private static final String SERVER_HOST = "localhost";

  /** Default Server Port*/
  private static final int SERVER_PORT = 8080;

  /** Default Server File*/
  private static final String SERVICE_PATH = "/AuthTest/auth/EJBInvokerServlet";

  protected static final String LOGIN_PATH = "/AuthTest/auth/EJBInvokerServlet";

  private String host;

  private String servletPath;

  /** Http Port used */
  private int port = -1;

  private String loginPath;
  
  private boolean debug = false;

  public HttpServerParam() {

  }

  public HttpServerParam(String host, int port, String servletPath) {
    this.host = host;
    this.port = port;
    this.servletPath = servletPath;
  }

  
  
/**
 * @return Returns the debug.
 */
public boolean isDebug() {
    return debug;
}
/**
 * @param debug The debug to set.
 */
public void setDebug(boolean debug) {
    this.debug = debug;
}

  public String getHost() {
    return (this.host == null) ? SERVER_HOST : this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getServletPath() {
    return (this.servletPath == null) ? SERVICE_PATH : this.servletPath;
  }

  public void setServletPath(String servletPath) {
    this.servletPath = servletPath;

  }

  public String getLoginPath() {
    return (this.loginPath == null) ? LOGIN_PATH : this.loginPath;
  }

  public void setLoginPath(String loginPath) {
    this.loginPath = loginPath;

  }

  public int getPort() {
    return (this.port == -1) ? SERVER_PORT : this.port;
  }

  public void setPort(int port) {
    this.port = port;
  }

}
