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


/**
 * the component definition in container.xml
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class ComponentMetaDef {
    
    protected String name;
    protected String className;
    protected String[] constructors;
            

    /**
     * @param name
     * @param className
     * @param constructors
     */
    public ComponentMetaDef(String name, String className, String[] constructors) {
        super();
        this.name = name;
        this.className = className;
        this.constructors = constructors;
    }    
    
    
    /**
     * @param name
     * @param className
     */
    public ComponentMetaDef(String name, String className) {
        super();
        this.name = name;
        this.className = className;
    }
          
    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }
    /**
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }
    /**
     * @return Returns the constructors.
     */
    public String[] getConstructors() {
        return constructors;
    }
    
    /**
     * @param constructors The constructors to set.
     */
    public void setConstructors(String[] constructors) {
        this.constructors = constructors;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
