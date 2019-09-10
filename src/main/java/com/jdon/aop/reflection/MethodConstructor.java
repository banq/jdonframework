/**
 * Copyright 2003-2006 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain event copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jdon.aop.reflection;

import java.lang.reflect.Method;

import com.jdon.bussinessproxy.TargetMetaDef;
import com.jdon.bussinessproxy.meta.MethodMetaArgs;
import com.jdon.bussinessproxy.target.TargetServiceFactory;
import com.jdon.container.access.TargetMetaRequest;
import com.jdon.container.access.TargetMetaRequestsHolder;
import com.jdon.container.finder.ContainerCallback;
import com.jdon.util.Debug;

public class MethodConstructor {

    private final static String module = MethodConstructor.class.getName();

    private final MethodInvokerUtil methodInvokerUtil ;
    
    private final TargetMetaRequestsHolder targetMetaRequestsHolder;
    
    private final ContainerCallback containerCallback;
    
    public MethodConstructor(ContainerCallback containerCallback,
    		TargetMetaRequestsHolder targetMetaRequestsHolder) {
    	this.containerCallback = containerCallback;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
		this.methodInvokerUtil = new MethodInvokerUtil(targetMetaRequestsHolder);
	}

	/**
     * @return Returns the methodInvokerUtil.
     */
    public MethodInvokerUtil getMethodInvokerUtil() {
        return methodInvokerUtil;
    }
    
    /**
     * ejb's method creating must at first get service's EJB Object;
     * pojo's method creating can only need service's class. 
     *  
     * @param targetServiceFactory
     * @param targetMetaRequest
     * @param methodMetaArgs
     * @return
     */
    public Method createMethod(TargetServiceFactory targetServiceFactory) {
        Method method = null;
        Debug.logVerbose("[JdonFramework] enter create the Method " , module);
        try {
        	TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
            if (targetMetaRequest.getTargetMetaDef().isEJB()) { 
                Object obj= methodInvokerUtil.createTargetObject(targetServiceFactory);
                method = createObjectMethod(obj, targetMetaRequest.getMethodMetaArgs());
            }else{
                method = createPojoMethod();
            }
        } catch (Exception ex) {
            Debug.logError("[JdonFramework] createMethod error: " + ex, module);
        }
        
        return method;

    }
    
    
    /**
     * create event method object by its meta definition
     * @param targetMetaDef
     * @param cw
     * @param methodMetaArgs
     */
    public Method createPojoMethod() {
        Method method = null;
        TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
        TargetMetaDef targetMetaDef = targetMetaRequest.getTargetMetaDef();
        MethodMetaArgs methodMetaArgs = targetMetaRequest.getMethodMetaArgs();        
        Debug.logVerbose("[JdonFramework] createPOJO Method :" + methodMetaArgs.getMethodName() + " for target service: " + targetMetaDef.getName(), module);        
        try {       
            Class thisCLass = containerCallback.getContainerWrapper().getComponentClass(targetMetaDef.getName());            
            if (thisCLass == null) return null;
            method = thisCLass.getMethod(methodMetaArgs.getMethodName(),
                    methodMetaArgs.getParamTypes());
        } catch (NoSuchMethodException ne) {
            Debug.logError("[JdonFramework] method name:"
                    + methodMetaArgs.getMethodName() + " or method parameters type don't match with your service's method", module);
            Object types[] = methodMetaArgs.getParamTypes();
            for(int i = 0; i<types.length; i ++){
                Debug.logError("[JdonFramework]service's method parameter type must be:" + types[i] + "; ", module);                
            }
        } catch (Exception ex) {
            Debug.logError("[JdonFramework] createPojoMethod error: " + ex, module);
        }
        
        return method;

    }
    
    /**
     * create event method object by target Object
     * @param ownerClass
     * @param methodMetaArgs
     * @return
     */
    public Method createObjectMethod(Object ownerClass, MethodMetaArgs methodMetaArgs) {
        Method m = null;        
        try {
            m = ownerClass.getClass().getMethod(methodMetaArgs.getMethodName(), 
                                                methodMetaArgs.getParamTypes());
        } catch (NoSuchMethodException nsme) {
            String errS = " NoSuchMethod:" + methodMetaArgs.getMethodName() + " in MethodMetaArgs of className:"
                    + ownerClass.getClass().getName();
            Debug.logError(errS, module);
        } catch (Exception ex) {
            Debug.logError("[JdonFramework] createMethod error:" + ex, module);
        }
        return m;
    }
    
    /**
     * create event method object
     * @param ownerClass
     * @param methodName
     * @param paramTypes
     * @return
     */
    public Method createObjectMethod(Object ownerClass, String methodName,
            Class[] paramTypes) {
        Method m = null;
        try {
            m = ownerClass.getClass().getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException nsme) {
            String errS = " NoSuchMethod:" + methodName + " in className:"
                    + ownerClass.getClass().getName() + " or method's args type error";
            Debug.logError(errS, module);
        } catch (Exception ex) {
            Debug.logError("[JdonFramework] createMethod error:" + ex, module);
        }
        return m;
    }

}
