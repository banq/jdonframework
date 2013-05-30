package com.jdon.util;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.*;

/**
 * 解决两个功能：
 * 1. 设置http头部的expire和last-modified
 * 具体应用程序，如论坛的帖子，将帖子最后更新日期保存到一个hashmap中，
 * 本程序从这个hashmap获取更新日期，关键问题是如何确定key?
 *
 * 2. 防止同一个IP在一秒内两次以上访问同一个jsp文件。
 *
 * <filter>
  <filter-name>
   ResponseHeaderFilter</filter-name>
  <filter-class>com.jdon.util.HttpResponseCacheFilter</filter-class>
  <init-param>
    <param-name>Cache-Control</param-name>
    <param-value>max-age=3600</param-value>
  </init-param>
 </filter>
 <filter-mapping>
  <filter-name>ResponseHeaderFilter</filter-name>
  <url-pattern>/logo.png</url-pattern>
 </filter-mapping>
 *   <param-value>
      private,no-cache,no-store</param-value>
 * <p>Copyright: Jdon.com Copyright (c) 2003</p>
 * <p></p>
 * @author banq
 * @version 1.0
 */

public class HttpResponseCacheFilter implements Filter {
  FilterConfig fc;
  public void doFilter(ServletRequest req,
                       ServletResponse res,
                       FilterChain chain) throws IOException,
      ServletException {

    HttpServletResponse response = (HttpServletResponse) res;
    // set the provided HTTP response parameters
    for (Enumeration e = fc.getInitParameterNames(); e.hasMoreElements(); ) {
      String headerName = (String) e.nextElement();
      response.addHeader(headerName, fc.getInitParameter(headerName));
    }
    // pass the request/response on
    chain.doFilter(req, response);
  }

  public void init(FilterConfig filterConfig) {
    this.fc = filterConfig;
  }

  public void destroy() {
    this.fc = null;
  }

}
