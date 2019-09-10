package com.jdon.container.visitor.http;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.access.UserTargetMetaDefFactory;
import com.jdon.container.pico.Startable;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.container.visitor.VisitorFactory;
import com.jdon.controller.context.SessionWrapper;
import com.jdon.util.Debug;

/**
 * the factory of ComponentVisitor's one sub-class HttpSessionComponentVisitor
 * if you replace this class with your Session Implementations , modify
 * contain.xml about this class
 * 
 * call this class by name
 * :com.jdon.container.finder.ComponentKeys.VISITOR_FACTORY
 * 
 * used in
 * {@link UserTargetMetaDefFactory#createTargetMetaRequest(TargetMetaDef, com.jdon.controller.context.ContextHolder)}
 * 
 * ComponentVisitor is ComponentOriginalVisitor that configured in contain.xml
 * 
 * @author banq
 * 
 */
public class HttpSessionVisitorFactoryImp implements VisitorFactory, Startable {
	private final static String module = VisitorFactory.class.getName();

	private ComponentVisitor componentVisitor;
	private HttpSessionVisitorFactorySetup httpSessionVisitorFactorySetup;
	private TargetMetaRequestsHolder targetMetaRequestsHolder;

	/**
	 * @param componentVisitor
	 */
	public HttpSessionVisitorFactoryImp(ComponentVisitor componentVisitor, TargetMetaRequestsHolder targetMetaRequestsHolder,
			HttpSessionVisitorFactorySetup httpSessionVisitorFactorySetup) {
		this.componentVisitor = componentVisitor;
		this.httpSessionVisitorFactorySetup = httpSessionVisitorFactorySetup;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}

	/**
	 * return event ComponentVisitor with cache. the httpSession is used for
	 * optimizing the component performance
	 * 
	 * @param request
	 * @param targetMetaDef
	 * @return
	 */
	public ComponentVisitor createtVisitor(SessionWrapper session, TargetMetaDef targetMetaDef) {
		if (session != null)
			return createtSessionVisitor(session, targetMetaDef);
		else
			return new NoSessionProxyComponentVisitor(componentVisitor, targetMetaRequestsHolder);

	}

	public ComponentVisitor createtSessionVisitor(SessionWrapper session, TargetMetaDef targetMetaDef) {
		ComponentVisitor cm = (ComponentVisitor) session.getAttribute("HttpSessionProxyVisitor");
		if (cm == null) {
			Debug.logVerbose("[JdonFramework] first time get ComponentVisitor ", module);
			cm = new HttpSessionProxyComponentVisitor(componentVisitor, targetMetaRequestsHolder, httpSessionVisitorFactorySetup);
			session.setAttribute("HttpSessionProxyVisitor", cm);
		}
		return cm;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		if (targetMetaRequestsHolder != null)
			targetMetaRequestsHolder.clear();
		targetMetaRequestsHolder = null;
		httpSessionVisitorFactorySetup = null;
		componentVisitor = null;

	}
}
