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

package com.jdon.container.visitor.http;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.access.UserTargetMetaDefFactory;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.container.visitor.ComponentsboxsInSession;
import com.jdon.container.visitor.data.SessionContext;
import com.jdon.util.Debug;

/**
 * using HttpSession as those components that need be cached
 * 
 * now there are three kinds type: ComponentKeys.PROXYINSTANCE_FACTORY
 * ComponentKeys.TARGETSERVICE_FACTORY; ComponentKeys.SESSIONCONTEXT_FACTORY;
 * 
 * PROXYINSTANCE_FACTORY and TARGETSERVICE_FACTORY are the factorys that create
 * components that need be optimized, if every time create these components, it
 * will cost performance.
 * 
 * ComponentKeys.SESSIONCONTEXT_FACTORY is the factory of the state data from
 * the web container.
 * 
 * Proxy patterns.
 * 
 * created in
 * {@link HttpSessionVisitorFactoryImp#createtVisitor(com.jdon.controller.context.SessionWrapper, com.jdon.bussinessproxy.TargetMetaDef)}
 * used in
 * {@link UserTargetMetaDefFactory#createTargetMetaRequest(com.jdon.bussinessproxy.TargetMetaDef, com.jdon.controller.context.ContextHolder)}
 * ComponentVisitor is ComponentOriginalVisitor that configured in contain.xml
 * 
 * @see HttpSessionVisitorFactoryImp
 * @author banq
 */
public class HttpSessionProxyComponentVisitor implements ComponentVisitor, HttpSessionBindingListener, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5064552004049128373L;

	private final static String module = HttpSessionProxyComponentVisitor.class.getName();

	/**
	 * in this box there are the result objects of the components running that
	 * need be optimized. note" not the components itself. the key = components
	 * name + VisitableName(such as :ComponentKeys.PROXYINSTANCE_FACTORY); the
	 * value is the result of components factory creating.
	 * 
	 * samples: key: NewsManagerproxyInstanceFactoryVisitable value: the dynamic
	 * proxy instance for NewsManager object
	 * 
	 * key:NewsManagertargetServiceFactoryVisitable value: the NewsManager
	 * object
	 * 
	 */

	private final ComponentsboxsInSession componentsboxsInSession;

	private ComponentVisitor componentVisitor;

	private TargetMetaRequestsHolder targetMetaRequestsHolder;

	private final boolean dynamiceProxyisCached;

	public HttpSessionProxyComponentVisitor(ComponentVisitor componentVisitor, TargetMetaRequestsHolder targetMetaRequestsHolder,
			HttpSessionVisitorFactorySetup httpSessionVisitorFactorySetup) {
		this.componentVisitor = componentVisitor;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
		this.componentsboxsInSession = new ComponentsboxsInSession(httpSessionVisitorFactorySetup);
		this.dynamiceProxyisCached = httpSessionVisitorFactorySetup.isDynamiceProxyisCached();

	}

	public void valueBound(HttpSessionBindingEvent event) {
		Debug.logVerbose("[JdonFramework] valueBound active, sessionId :" + event.getSession().getId(), module);
		componentsboxsInSession.clear();

	}

	/**
	 * session destroyed. remove all references;
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {
		String sessionId = event.getSession().getId();
		Debug.logVerbose("[JdonFramework] unvalueBound active, sessionId :" + sessionId, module);
		Debug.logVerbose("[JdonFramework] unvalueUnbound active, componentsboxs size" + componentsboxsInSession.size(), module);
		// removeObjects();
		componentsboxsInSession.clear();
		if (targetMetaRequestsHolder != null)
			targetMetaRequestsHolder.clear();
		targetMetaRequestsHolder = null;
		componentVisitor = null;
	}

	/**
	 * the object type saved in componentsboxs is decided by the method"
	 * visitableFactory.createVisitable. only ejb service need cached, pojo
	 * service not need.
	 * 
	 * @param targetMetaDef
	 *            TargetMetaDef
	 * @return Object
	 */
	public Object visit() {
		Object o = null;
		try {
			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
			StringBuilder sb = new StringBuilder(targetMetaRequest.getTargetMetaDef().getCacheKey());
			sb.append(targetMetaRequest.getVisitableName());
			Debug.logVerbose("[JdonFramework] get the optimized instance for the key " + sb.toString(), module);
			o = componentsboxsInSession.get(sb.toString());
			if (o == null) {
				Debug.logVerbose("[JdonFramework] first time visit: " + targetMetaRequest.getTargetMetaDef().getClassName(), module);
				// com.jdon.container.visitor.ComponentOriginalVisitor#visit
				o = componentVisitor.visit();
				if (dynamiceProxyisCached)
					componentsboxsInSession.add(sb.toString(), o);
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework]visit error: " + e);
		}
		return o;
	}

	public SessionContext createSessionContext() {
		SessionContext sessionContext = (SessionContext) componentsboxsInSession.get(SessionContext.NAME);
		if (sessionContext == null) {
			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
			Debug.logVerbose("[JdonFramework] first time visit sessionContext: " + targetMetaRequest.getVisitableName(), module);
			// com.jdon.container.visitor.ComponentOriginalVisitor#createSessionContext
			sessionContext = componentVisitor.createSessionContext();
			componentsboxsInSession.add(SessionContext.NAME, sessionContext);
		}
		return (SessionContext) sessionContext;
	}

	/**
	 * remove all ejb references
	 */
	public void removeObjects() {
		/**
		 * 
		 * try { Iterator iter = componentsboxs.keySet().iterator(); while
		 * (iter.hasNext()) { String key = (String) iter.next(); Object ccEjb =
		 * (Object) componentsboxs.get(key); removeEJBObject(ccEjb); } } catch
		 * (Exception ex) { Debug.logWarning(ex, module); } finally {
		 * componentsboxs.clear(); }
		 */

	}

	/**
	 * destroy the EJB.
	 * 
	 * private void removeEJBObject(Object ccEjb) { if (ccEjb == null) return;
	 * try { //这个EJB的remove方法需要被任何角色访问，因为此时principle为空 if (ccEjb instanceof
	 * EJBLocalObject) { EJBLocalObject eo = ( (EJBLocalObject) ccEjb);
	 * eo.remove(); } else if (ccEjb instanceof EJBObject) { EJBObject eo = (
	 * (EJBObject) ccEjb); eo.remove(); } } catch (Exception re) { } }
	 */
}
