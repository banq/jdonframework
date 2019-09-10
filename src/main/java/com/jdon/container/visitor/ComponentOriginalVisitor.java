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
package com.jdon.container.visitor;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.container.visitor.data.SessionContext;
import com.jdon.util.Debug;

/**
 * 
 * original impleplements for target service components that will be cached
 * this is default concrete class of componentVisitor, configured in contain.xml
 * @see ComponentVisitor
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class ComponentOriginalVisitor implements ComponentVisitor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4109265173603639132L;

	private final static String module = ComponentOriginalVisitor.class.getName();

	private final ContainerCallback containerCallback;
	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public ComponentOriginalVisitor(ContainerCallback containerCallback, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.containerCallback = containerCallback;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
	}
	
	/**
	 * find the visitable component from container, and execute it's accept
	 * method, the return result is the tager service object.
	 * 
	 */
	public Object visit() {
		Object o = null;
		try {
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
			Debug.logVerbose("[JdonFramework] ComponentOriginalVisitor active:" + targetMetaRequest.getVisitableName(), module);
			//targetMetaRequest.setVisitableName change the value
			Visitable vo = (Visitable) containerWrapper.lookup(targetMetaRequest.getVisitableName());
			o = vo.accept();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] ComponentOriginalVisitor active error: " + ex);
		}
		return o;
	}

	public SessionContext createSessionContext() {
		SessionContext sc = null;
		try {
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			Visitable vo = (Visitable) containerWrapper.lookup(ComponentKeys.SESSIONCONTEXT_FACTORY);
			sc = (SessionContext) vo.accept();
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] ComponentOriginalVisitor createSessionContext error: " + ex);
		}
		return sc;
	}

}
