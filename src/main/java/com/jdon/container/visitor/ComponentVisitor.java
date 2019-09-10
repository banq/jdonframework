/**
 * Copyright 2003-2005 the original author or authors.
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

package com.jdon.container.visitor;

import java.io.Serializable;

import com.jdon.container.visitor.data.SessionContext;

/**
 * Visitor pattern
 * ComponentVisitor is event actor of Visitor, it do not need
 * many access method, such as:
 *   visitAcomponent();
 *   visitBcomponent();
 *   ...
 * because all these components has registered in container,
 * we can get them from container.
 * 
 * there are two concrete classes:
 * @see com.jdon.container.visitor.ComponentOriginalVisitor
 * @see com.jdon.container.visitor.http.HttpSessionProxyComponentVisitor
 *  @author <event href="mailto:banqiao@jdon.com">banq</event>
 */
public interface ComponentVisitor extends Serializable{
  
  /**
   * using this method, the component that implements Visitable interface
   * will be executed, and return the running result.
   * 
   * @param targetMetaDef target service meta
   * @return  the result of the component run 
   */
  Object visit();
  
  SessionContext createSessionContext();
  
}
