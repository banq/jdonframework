/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.jdon.aop.interceptor.InterceptorsChain;
import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.builder.ContainerRegistryBuilder;
import com.jdon.container.finder.ContainerFinderImp;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.RequestWrapper;
import com.jdon.controller.context.web.HttpServletRequestWrapper;
import com.jdon.controller.context.web.ServletContextWrapper;
import com.jdon.controller.service.Service;
import com.jdon.controller.service.ServiceFacade;
import com.jdon.controller.service.ServiceFactory;
import com.jdon.util.Debug;

/**
 * Using WebAppUtil, framework's user can get his businesss service object that
 * defined in jdonframework.xml
 * 
 * this is main and important client class for framework's user.
 * 
 * 
 * @author banq
 */
public class WebAppUtil {
	private final static String module = WebAppUtil.class.getName();

	private final static ContainerFinderImp scf = new ContainerFinderImp();

	/**
	 * get a service from jdonframework.xml's service configure. the service
	 * maybe is a pojo service or a ejb service.
	 * 
	 * <p>
	 * if user has a business interface, so the interface can has two
	 * implemention:pojo or ejb, if is ejb, the ejb's local/remote interface
	 * must inherit the business interface. so the application's MVC will
	 * completely seperate from his business lay
	 * 
	 * <p>
	 * usage: in jdonframework.xml: <pojoService name="userJdbcDao"
	 * class="news.container.UserJdbcDao"> <constructor value="java:/NewsDS"/>
	 * </pojoService>
	 * 
	 * <p>
	 * UserDao ud = (UserDao)WebAppUtil.getService(“userJdbcDao”, request);
	 * 
	 * UserDao is a interface.
	 * 
	 * @param name
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @return Object
	 * @throws Exception
	 */
	public static Object getService(String name, HttpServletRequest request) {
		ServiceFacade serviceFacade = new ServiceFacade();
		ServletContext sc = request.getSession().getServletContext();
		ServiceFactory serviceFactory = serviceFacade.getServiceFactory(new ServletContextWrapper(sc));
		RequestWrapper requestW = new HttpServletRequestWrapper(request);
		return serviceFactory.getService(name, requestW);
	}

	/**
	 * Difference with getComponentInstance method: 1. this method return new
	 * service instance each times.
	 * 
	 * 2. call methods of the return service instance, will active interceptors.
	 * execept some interceptors about session, such as Stateful disable.
	 * 
	 * @param name
	 * @param sc
	 * @return
	 */
	public static Object getService(String name, ServletContext sc) {
		ServiceFacade serviceFacade = new ServiceFacade();
		AppContextWrapper acw = new ServletContextWrapper(sc);
		ServiceFactory serviceFactory = serviceFacade.getServiceFactory(acw);
		return serviceFactory.getService(name, acw);
	}

	public static Object getService(TargetMetaDef targetMetaDef, HttpServletRequest request) {
		ServiceFacade serviceFacade = new ServiceFacade();
		ServletContext sc = request.getSession().getServletContext();
		ServiceFactory serviceFactory = serviceFacade.getServiceFactory(new ServletContextWrapper(sc));
		RequestWrapper requestW = new HttpServletRequestWrapper(request);
		return serviceFactory.getService(targetMetaDef, requestW);
	}

	/**
	 * get a component that registered in container. the component is not
	 * different from the service. the component instance is single instance Any
	 * intercepter will be disable
	 * 
	 */
	public static Object getComponentInstance(String name, HttpServletRequest request) {
		ServletContext sc = request.getSession().getServletContext();
		ContainerWrapper containerWrapper = scf.findContainer(new ServletContextWrapper(sc));
		if (!containerWrapper.isStart()) {
			Debug.logError("JdonFramework not yet started, please try later ", module);
			return null;
		}
		return containerWrapper.lookup(name);
	}

	/**
	 * get a component that registered in container. the component is not
	 * different from the service. the component instance is single instance Any
	 * intercepter will be disable
	 * 
	 * @param sc
	 * @return
	 */
	public static Object getComponentInstance(String name, ServletContext sc) {
		ContainerWrapper containerWrapper = scf.findContainer(new ServletContextWrapper(sc));
		if (!containerWrapper.isStart()) {
			Debug.logError("JdonFramework not yet started, please try later ", module);
			return null;
		}
		return containerWrapper.lookup(name);
	}

	/**
	 * get a EJB service directly.
	 * 
	 * @param name
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @return Object
	 * @throws Exception
	 */
	public static Object getEJBService(String name, HttpServletRequest request) throws Exception {
		return getService(name, request);
	}

	/**
	 * Command pattern for service invoke sample: browser url:
	 * /aaa.do?method=xxxxx
	 * 
	 * xxxxx is the service's method, such as:
	 * 
	 * public interface TestService{ void xxxxx(EventModel em); }
	 * 
	 * @see com.jdon.strutsutil.ServiceMethodAction
	 * 
	 * @param serviceName
	 *            the service name in jdonframework.xml
	 * @param methodName
	 *            the method name
	 * @param request
	 * @param model
	 *            the method parameter must be packed in a ModelIF object. if no
	 *            method parameter, set it to null;
	 * @return return the service dealing result
	 * @throws Exception
	 * 
	 */
	public static Object callService(String serviceName, String methodName, Object[] methodParams, HttpServletRequest request) throws Exception {
		Debug.logVerbose("[JdonFramework] call the method: " + methodName + " for the service: " + serviceName, module);
		Object result = null;
		try {
			MethodMetaArgs methodMetaArgs = AppUtil.createDirectMethod(methodName, methodParams);

			ServiceFacade serviceFacade = new ServiceFacade();
			ServletContext sc = request.getSession().getServletContext();
			Service service = serviceFacade.getService(new ServletContextWrapper(sc));
			RequestWrapper requestW = new HttpServletRequestWrapper(request);
			result = service.execute(serviceName, methodMetaArgs, requestW);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] serviceAction Error: " + ex, module);
			throw new Exception(" serviceAction Error:" + ex);
		}
		return result;
	}

	/**
	 * get the key for the application container user can directly get his
	 * container from servletcontext by the key.
	 * 
	 * @return String
	 */
	public static String getContainerKey() {
		return ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME;
	}

	/**
	 * get the key for the interceptor, by the key, use can add his interceptor
	 * to the container
	 * 
	 * @return String
	 */
	public static String getInterceptorKey() {
		return InterceptorsChain.NAME;
	}

	/**
	 * get this Web application's container
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return ContainerWrapper
	 * @throws Exception
	 */
	public static ContainerWrapper getContainer(HttpServletRequest request) throws Exception {
		ContainerFinderImp scf = new ContainerFinderImp();
		ServletContext sc = request.getSession().getServletContext();
		return scf.findContainer(new ServletContextWrapper(sc));
	}

	public static ContainerWrapper getContainer(ServletContext sc) throws Exception {
		ContainerFinderImp scf = new ContainerFinderImp();
		return scf.findContainer(new ServletContextWrapper(sc));
	}

	/**
	 * add a MethodInterceptor for all services how to implements a
	 * MethodInterceptor,reference:
	 * 
	 * 
	 * public static void addInterceptor(MethodInterceptor interceptor,
	 * HttpServletRequest request) throws Exception {
	 * addInterceptor(Pointcut.TARGET_PROPS_SERVICES, interceptor, request); }
	 * 
	 * /** add a MethodInterceptor for pointcut
	 * 
	 * @param pointcut
	 *            Pointcut values
	 * @param interceptor
	 * @param request
	 * @throws Exception
	 * 
	 *             public static void addInterceptor(String pointcut,
	 *             MethodInterceptor interceptor, HttpServletRequest request)
	 *             throws Exception { ContainerWrapper cw =
	 *             getContainer(request); if (cw == null) { throw new
	 *             Exception("Please at first init the JdonFramework!"); }
	 *             InterceptorsChain interceptorsChain = (InterceptorsChain)
	 *             cw.lookup(ComponentKeys.INTERCEPTOR_CHAIN);
	 *             interceptorsChain.addInterceptor(pointcut, interceptor); }
	 */
}
