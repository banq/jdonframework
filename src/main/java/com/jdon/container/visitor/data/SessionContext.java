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
package com.jdon.container.visitor.data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * this class inlude those datas that read from the web container or others.
 * these datas is stateful, and it's scope is one instance per client.
 * usually they are saved in httpSession of the web container.
 * 
 * but these datas are different from the components that saved in httpsession.
 * it is like the stateful components. it include state data.
 * 
 *  the way of visiting the SessionContext that saved in httpSession  
 * 
 *     targetMetaRequest.setVisitableName(ComponentKeys.SESSIONCONTEXT_FACTORY);        
 *     SessionContext sessionContext = (SessionContext)cm.visit(targetMetaRequest);
 * 

 * @see SessionContextFactoryVisitable#accept create event new SessionContext
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */

public class SessionContext implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8469381462547988166L;

	public final static String NAME = SessionContext.class.getName();
    
    private final Map arrtibutes;
    
    private final int maxSize;
    
    public SessionContext(int maxSize){
    	arrtibutes = new  ConcurrentHashMap();
    	this.maxSize = maxSize;
    }
    
    public void setArrtibute(String key, Object object){
    	if (arrtibutes.size() > maxSize){
    		System.err.print("sessionContext exceed maxSize =" + maxSize);
    		System.err.print("setup contain.xml sessionContextFactoryVisitable" );
    		arrtibutes.clear();
    	}
        arrtibutes.put(key, object);
    }
    
    public Object getArrtibute(String key){
        return arrtibutes.get(key);
    }
    
    public void remove(String key){
        arrtibutes.remove(key);
    }
    
    public void removeAll(){
        arrtibutes.clear();
    }
    
    
    
}
