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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.container.startup.ContainerSetupScript;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.controller.context.web.HttpServletRequestWrapper;
import com.jdon.controller.context.web.ServletContextWrapper;
import com.jdon.controller.service.Service;
import com.jdon.controller.service.ServiceFacade;
import com.jdon.util.Debug;
import com.jdon.util.StringUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 本类是服务器端处理远程客户端发送的调用EJB2请求。
 * 
 * 本类功能也可以使用Struts的Action实现。 权限验证是使用J2EE的基于Http的Basic Auth.
 * 
 * 使用本类可以作为一个单独的EJB网关服务器。
 * 
 * @author banq
 */
public class InvokerServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8717375504982278003L;
	public final static String module = InvokerServlet.class.getName();
	private ContainerSetupScript css = new ContainerSetupScript();

	/**
	 * 使用Picocontainer 将jdonframework.xml配置在web.xml中即可
	 * 
	 * @throws ServletException
	 */
	public void init() throws ServletException {
		ServletContext sc = this.getServletContext();
		String configList = this.getServletConfig().getInitParameter("configList");
		String[] configs = StringUtil.split(configList, ",");
		for (int i = 0; i < configs.length; i++) {
			Debug.logVerbose("[JdonFramework] locate event configuration in web.xml :" + configs[i], module);
			css.prepare(configs[i], new ServletContextWrapper(sc));
		}
	}

	public void destroy() {
		css.destroyed(new ServletContextWrapper(this.getServletContext()));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = new PrintWriter(response.getOutputStream());
		out.println("<html>");
		out.println("<head><title>InvokerServlet</title></head>");
		out.println("<body> ");
		out.println(request.getRemoteUser());
		out.println(" Welcome to login in !!</body></html>");
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("login") != null) {
			// 如果是login 显示欢迎信息或其它更新通知等信息
			doGet(request, response);
			return;
		}
		// 从HttpServletRequest中获得传送的对象
		HttpRequest httpRequest = getHttpServiceRequest(request);
		if (httpRequest == null)
			return;
		HttpResponse httpResponse = null;
		try {
			TargetMetaDef targetMetaDef = httpRequest.getTargetMetaDef();
			String p_methodName = httpRequest.getMethodName();
			Class[] paramTypes = httpRequest.getParamTypes();
			Object[] p_args = httpRequest.getArgs();

			ServiceFacade serviceFacade = new ServiceFacade();
			ServletContext sc = this.getServletContext();
			Service service = serviceFacade.getService(new ServletContextWrapper(sc));

			// 将上述参数打包到eJBMetaDef中
			MethodMetaArgs methodMetaArgs = new MethodMetaArgs(p_methodName, paramTypes, p_args);
			RequestWrapper requestW = new HttpServletRequestWrapper(request);
			Object object = service.execute(targetMetaDef, methodMetaArgs, requestW);
			httpResponse = new HttpResponse(object);
		} catch (Exception e) {
			Debug.logError(e, module);
			httpResponse = new HttpResponse(new Throwable(e));
		} catch (Throwable te) {
			Debug.logError(te, module);
			httpResponse = new HttpResponse(te);
		}

		// Write the result in the http stream
		HttpSession session = request.getSession(true);
		response.addHeader("jsessionid", session.getId());
		writeHttpServiceResponse(response, httpResponse);
	}

	/**
	 * 序列化处理结果
	 */
	private void writeHttpServiceResponse(HttpServletResponse response, HttpResponse httpResponse) throws IOException {
		OutputStream outputStream = response.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outputStream);
		oos.writeObject(httpResponse);
		oos.close();
	}

	/**
	 * 从请求信息中反序列化
	 */
	private HttpRequest getHttpServiceRequest(HttpServletRequest request) throws IOException {
		HttpRequest httpServiceRequest = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(request.getInputStream());
			httpServiceRequest = (HttpRequest) ois.readObject();
		} catch (Exception e) {
			Debug.logError(e, module);
		} finally {
			if (ois != null)
				ois.close();
		}
		return httpServiceRequest;
	}

}
