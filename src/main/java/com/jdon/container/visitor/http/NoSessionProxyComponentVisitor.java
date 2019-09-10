/*
 * Copyright 2003-2009 the original author or authors.
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
package com.jdon.container.visitor.http;

import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.visitor.ComponentVisitor;
import com.jdon.container.visitor.data.SessionContext;
import com.jdon.util.Debug;

public class NoSessionProxyComponentVisitor implements ComponentVisitor {
	private final static String module = NoSessionProxyComponentVisitor.class.getName();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ComponentVisitor componentVisitor;

	private final TargetMetaRequestsHolder targetMetaRequestsHolder;

	public NoSessionProxyComponentVisitor(ComponentVisitor componentVisitor, TargetMetaRequestsHolder targetMetaRequestsHolder) {
		this.componentVisitor = componentVisitor;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;

	}

	public Object visit() {
		Object o = null;
		try {
			TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
			StringBuilder sb = new StringBuilder(targetMetaRequest.getTargetMetaDef().getCacheKey());
			sb.append(targetMetaRequest.getVisitableName());
			Debug.logVerbose("[JdonFramework] get the optimized instance for the key " + sb.toString(), module);
			o = componentVisitor.visit();
		} catch (Exception e) {
			Debug.logError("[JdonFramework]visit error: " + e);
		}
		return o;
	}

	public SessionContext createSessionContext() {
		return null;
	}

}
