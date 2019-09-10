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
package com.jdon.container.config;

import java.util.Iterator;
import java.util.Map;


/**
 * the components that loaded from container.xml
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class ContainerComponents {
    public final static String NAME = ContainerComponents.class.getName();
    
    private final Map components ;
          
    /**
     * @param components
     */
    public ContainerComponents(Map components) {
        this.components = components;
    }
    
    public void addComponent(String name, ComponentMetaDef componentMetaDef){
        this.components.put(name, componentMetaDef);
    }
    
    public void addComponents(Map components){
        this.components.putAll(components);
    }
    
    public ComponentMetaDef getComponentMetaDef(String name){
        return (ComponentMetaDef)this.components.get(name);
    }
    
    public Iterator iterator(){
        return this.components.keySet().iterator();
    }
    
   public int size(){
       return this.components.size();
   }
   
   

    
    /**
     * @return Returns the components.
     */
    public Map getComponents() {
        return this.components;
    }
}