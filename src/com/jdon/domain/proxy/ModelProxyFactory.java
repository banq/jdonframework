/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package com.jdon.domain.proxy;

import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import com.jdon.util.Debug;
/**
 * This class is used to create the proxy for models,it uses cglib to create
 *  model proxy.
 * 
 * for example :
 * 
 * @Introduce("message")
 * public class MyModelEvent{
 * 	
 * 	@Send(value="MyModel.findName",asyn=true)
	public DomainMessage asyncFindName(MyModel myModel) {
		return new DomainMessage(myModel);
	}
 * }
 * 
 * For the above class,the MessageInterceptor named "message" will be apply to 
 * MyModelEvent,when the asyncFindName method is invoked,MessageInterceptor will intercept 
 * this invocation,and send the DomainMessage to the Listener named "MyModel.findName".
 *  
 * @author xmuzyu
 *
 */
public class ModelProxyFactory {
	private final static String module = ModelProxyFactory.class.getName();

	public ModelProxyFactory() {
		super();
	}

	public Object create(Object model, List methodInterceptors) {

		Object dynamicProxy = null;
		try {
			if (methodInterceptors == null || methodInterceptors.size() == 0)
				return model;
			Enhancer enhancer = new Enhancer();
			enhancer.setCallback(new ModelCGLIBMethodInterceptorImp(model, methodInterceptors));
			enhancer.setSuperclass(model.getClass());
			dynamicProxy = enhancer.create();
		} catch (Exception e) {
			Debug.logError("create error " + e, module);
		}
		return dynamicProxy;
	}

}
