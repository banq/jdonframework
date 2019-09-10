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

package com.jdon.strutsutil;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * Model collection class that will be iterated by jsp this is event subclass of
 * struts's ActionForm it include all model colletion that fit for query
 * condition
 * 
 * this class always collaborate with the concrete class of ModelListAction
 * 
 * 
 * 
 * this class must be configured in struts-config.xml
 * 
 * <form-bean name="listForm" type="com.jdon.strutsutil.ModelListForm" />
 * 
 * @author banq
 * @see ModellistAction PageIterator
 *  
 */
public class ModelListForm extends ActionForm {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5752036311335027328L;

	/**
     * the count of all models that fit for query condition
     */
    protected int allCount = 0;

    /**
     * current page start sequence
     */
    protected int start = 0;

    /**
     * the count of current page models
     */
    protected int count = 30;
   

    /**
     * event model that express 1:N N is below model collection
     */
    protected Object oneModel = null;
    
    /**
     * models collection of current page
     */
    protected Collection list = new ArrayList();

    public Collection getList() {
        return list;
    }

    public void setList(Collection list) {
        this.list = list;
    }

    public int getCount() {
        return (this.count);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAllCount() {
        return (this.allCount);
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getStart() {
        return (this.start);
    }

    public void setStart(int start) {
        this.start = start;
    }

  
    public Object getOneModel() {
        return oneModel;
    }

    public void setOneModel(Object oneModel) {
        this.oneModel = oneModel;
    }
    

}
