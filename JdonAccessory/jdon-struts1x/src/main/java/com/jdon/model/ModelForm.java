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

package com.jdon.model;

import com.jdon.strutsutil.ViewForm;


/**
 * the model mapping in the presentation layer.
 * if you need jdon's CRUD, your all actionForm must extends this class.
 * donot need extends Struts's ActionForm again.
 *
 * @author banq
 */
public abstract class ModelForm extends ViewForm {

  /**
	 * 
	 */
	private static final long serialVersionUID = 776395943291461419L;
public final static String VIEW_STR = "view";
  public final static String CREATE_STR = "create";
  public final static String EDIT_STR = "edit";
  public final static String UPDATE_STR = "update";
  public final static String DELETE_STR = "delete";
  public final static String ADD_STR = "add";
  public final static String REMOVE_STR = "remove";


  private String action; 
                         

  public void setAction(String action) {
    this.action = action;
  }
  public String getAction() {
    return action;
  }
  
  public String getJdonActionType() {
    return action;
  }
  public void setJdonActionType(String jdonActionType) {
    this.action = jdonActionType;
  }

  public String getMethod() {
      return action;
    }
    public void setMethod(String method) {
      this.action = method;
    }
  
  
}
