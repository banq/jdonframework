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

import java.io.Serializable;

import com.jdon.bussinessproxy.TargetMetaDef;


public class HttpRequest implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 6025235945666383518L;
	private TargetMetaDef targetMetaDef;
    private String methodName;
    private String[] paramTypesName;
    private Object[] args;

    public HttpRequest(TargetMetaDef targetMetaDef, String methodToCall,
                              Class[] paramTypes, Object[] args) {
        this.targetMetaDef = targetMetaDef;
        this.methodName = methodToCall;
        setParamTypes(paramTypes);
        this.args = args;
    }


    public boolean isStatefull() {
        return true;
    }


    public TargetMetaDef getTargetMetaDef() {
        return targetMetaDef;
    }

    public void setTargetMetaDef(TargetMetaDef targetMetaDef) {
        this.targetMetaDef = targetMetaDef;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return The array of params of the invoked method.
     */
    public Class[] getParamTypes() {
        Class[] result = new Class[paramTypesName.length];
        for (int i = 0; i < paramTypesName.length; i++) {
            String type = paramTypesName[i];
            Class arg = null;
            if (type == null)
                arg = null;
            else if (type.equals("int"))
                arg = Integer.TYPE;
            else if (type.equals("boolean"))
                arg = Boolean.TYPE;
            else if (type.equals("float"))
                arg = Float.TYPE;
            else if (type.equals("byte"))
                arg = Byte.TYPE;
            else if (type.equals("short"))
                arg = Short.TYPE;
            else if (type.equals("char"))
                arg = Character.TYPE;
            else if (type.equals("long"))
                arg = Long.TYPE;
            else if (type.equals("double"))
                arg = Double.TYPE;
            else
                try {
                    arg = Class.forName(type);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getLocalizedMessage());
                }
            result[i] = arg;
        }
        return result;
    }

    public void setParamTypes(Class[] paramTypes) {
        paramTypesName = new String[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Class type = paramTypes[i];
            paramTypesName[i] = type.getName();
        }
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
