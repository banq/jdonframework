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
package com.jdon.container.config.aspect;

import com.jdon.container.config.ComponentMetaDef;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class AspectComponentsMetaDef extends ComponentMetaDef{
    
    private final String pointcut;
    
    public AspectComponentsMetaDef(String name, String className, String[] constructors, String pointcut) {        
       super(name, className, constructors);
       this.pointcut = pointcut;
    }    
    
    
    /**
     * @param name
     * @param className
     */
    public AspectComponentsMetaDef(String name, String className, String pointcut) {
        super(name, className);
        this.pointcut = pointcut;        
    }

    /**
     * @return Returns the target.
     */
    public String getPointcut() {
        return pointcut;
    }
   
}
