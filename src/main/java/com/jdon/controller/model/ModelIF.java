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

package com.jdon.controller.model;

import java.io.Serializable;

import com.jdon.controller.cache.Cacheable;

/**
 * Domain Model should normal live in memory not in database.
 * so cache in memory is very important for domain model life cycle.
 * 
 * the class  be cached or setModified is important, this method can be
 * used to refresh the cache. the controller is in com.jdon.model.cache.ModelCacheManager
 * 
 * the difference with setModified and setCacheable;
 * setCacheable to false, the model will never be saved in the cache.
 * 
 * setModified to true, if the model exists in the cache, the client will not 
 * get it from cache, and in the meantime framework delete the model from the cache.
 * 
 * deleting the model from cache must have event condition that the deleting operator
 * can access the cache of the container, if it cann't access the container, 
 * it cann't delete the model from cache. such it is EJB. 
 * 
 * 
 * 
 * @author banq
 * @see  com.jdon.domain.model.cache.ModelCacheManager
 */
public interface ModelIF extends Cacheable, Cloneable, Serializable {

    /**
     * in the past version, this method name is isCacheble,
     * now change it after 1.3 !
     */
    public boolean isCacheable();
    
    /**
     * in the past version, this method name is setCacheble,
     * now change it  after 1.3 !
     */
    public void setCacheable(boolean cacheable);
    
    public boolean isModified();

    /**
     * set the property has been modified such as : setName(String name){
     * this.name = name; setModified(true); }
     * 
     */
    public void setModified(boolean modified) ;
    
    

}
