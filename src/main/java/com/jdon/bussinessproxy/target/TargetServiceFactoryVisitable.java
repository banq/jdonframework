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
package com.jdon.bussinessproxy.target;

import com.jdon.container.visitor.Visitable;

/**
 * this class supply accessing TargetServiceFactory by cache visitor if
 * ServiceMethodInvoker call this class, ejb service instance or pojo instance
 * will be cached.
 * 
 * active it by two steps: 1. configure in container.xml <component
 * name="targetServiceFactoryVisitable"
 * class="com.jdon.bussinessproxy.target.TargetServiceFactoryVisitable" /> 2.
 * uncomment the line in AopClient's invoke method
 * this.methodInvoker.setVisitor(cm);
 * 
 * this.methodInvoker.setVisitor(cm);
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class TargetServiceFactoryVisitable implements Visitable {

	private final TargetServiceFactory targetServiceFactory;

	public TargetServiceFactoryVisitable(TargetServiceFactory targetServiceFactory) {
		this.targetServiceFactory = targetServiceFactory;
	}

	public Object accept() {
		return targetServiceFactory.create();
	}
}
