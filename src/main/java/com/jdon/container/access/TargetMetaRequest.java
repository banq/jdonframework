/*
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.container.access;

import java.io.Serializable;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.container.visitor.data.SessionContext;

/**
 * Every container's user has one  UserTargetMetaDef object
 * this object is event DTO when this user enter businerss proxy.
 * it reduce the method's parameters amount;
 * it's scope is event instance for per request of one user ;
 * TargetMetaDef object's scope is for event service for all users;
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class TargetMetaRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9137088748339607292L;

	private final TargetMetaDef targetMetaDef;
    
    private final ComponentVisitor componentVisitor;
    
    private volatile MethodMetaArgs methodMetaArgs;

    private volatile String visitableName;
    
    private volatile SessionContext sessionContext;

    /**
     *  ComponentVisitor is HttpSessionComponentVisitor created in {@link UserTargetMetaDefFactory#createTargetMetaRequest(TargetMetaDef, com.jdon.controller.context.ContextHolder)}
     * 
     * @param targetMetaDef
     */
    public TargetMetaRequest(TargetMetaDef targetMetaDef, ComponentVisitor componentVisitor) {
        super();
        this.targetMetaDef = targetMetaDef;
        this.componentVisitor = componentVisitor;
    }

    /**
     * @return Returns the targetMetaDef.
     */
    public TargetMetaDef getTargetMetaDef() {
        return targetMetaDef;
    }  

    /**
     * @return Returns the componentVisitor.
     */
    public ComponentVisitor getComponentVisitor() {
        return componentVisitor;
    }

    public String getVisitableName() {
        return this.visitableName;
    }

    /*
     * 1.SessionContextInterceptor:targetMetaRequest.setVisitableName(ComponentKeys.SESSIONCONTEXT_FACTORY);
     * set vistable value be SessionContextFactoryVisitable
     * 
     * 2.StatefulInterceptor: setVisitableName(ComponentKeys.TARGETSERVICE_FACTORY)
     * set vistable value be com.jdon.bussinessproxy.target.TargetServiceFactoryVisitable
     * 
     * 3.MethodInvokerUtil:createTargetObject setVisitableName(ComponentKeys.TARGETSERVICE_FACTORY);
     * set vistable value be com.jdon.bussinessproxy.target.TargetServiceFactoryVisitable
     * 
     * 4. ServiceAccessorImp: getService setVisitableName(ComponentKeys.PROXYINSTANCE_FACTORY);
     * set vistable value be  com.jdon.bussinessproxy.dyncproxy.ProxyInstanceFactoryVisitable
     * 
     * 5. WebServiceDecorator:
     * set vistable value be SessionContextFactoryVisitable
     * 
     * used in ComponentOriginalVisitor#visit
     */
    
    public void setVisitableName(String visitableName) {
        this.visitableName = visitableName;
    }
    

	/**
     * @return Returns the methodMetaArgs.
     */
    public MethodMetaArgs getMethodMetaArgs() {
        return methodMetaArgs;
    }
    /**
     * @param methodMetaArgs The methodMetaArgs to set.
     */
    public void setMethodMetaArgs(MethodMetaArgs methodMetaArgs) {
        this.methodMetaArgs = methodMetaArgs;
    }
    
    /**
     * @return Returns the sessionContext.
     */
    public SessionContext getSessionContext() {
        return sessionContext;
    }
    /**
     * @param sessionContext The sessionContext to set.
     */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }
}
