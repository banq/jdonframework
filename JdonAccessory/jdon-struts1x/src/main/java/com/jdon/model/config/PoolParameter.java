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
package com.jdon.model.config;

/**
 * pack the pool size parameters 
 * create this class is for configuring in container.xml
 * @author <event href="mailto:banqJdon<AT>jdon.com">banq</event>
 *
 */

public class PoolParameter {
    
    private int handlerPoolSize;
    private int modelPoolSize;
    
    
    

    /**
     * @param handlerPoolSize
     * @param modelPoolSize
     */
    public PoolParameter(String handlerPoolSize, String modelPoolSize) {
        this.handlerPoolSize = Integer.parseInt(handlerPoolSize);
        this.modelPoolSize = Integer.parseInt(modelPoolSize);
    }
    
    
    
    
    
    /**
     * @return Returns the handlerPoolSize.
     */
    public int getHandlerPoolSize() {
        return handlerPoolSize;
    }
    /**
     * @param handlerPoolSize The handlerPoolSize to set.
     */
    public void setHandlerPoolSize(int handlerPoolSize) {
        this.handlerPoolSize = handlerPoolSize;
    }
    /**
     * @return Returns the modelPoolSize.
     */
    public int getModelPoolSize() {
        return modelPoolSize;
    }
    /**
     * @param modelPoolSize The modelPoolSize to set.
     */
    public void setModelPoolSize(int modelPoolSize) {
        this.modelPoolSize = modelPoolSize;
    }
}
