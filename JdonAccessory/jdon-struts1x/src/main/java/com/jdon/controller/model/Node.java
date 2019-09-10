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

import java.util.*;

/**
 * tree's Node
 * 
 * Node is event relation between nodes of tree.
 * it pack all ids of nodes of tree
 *
 * @author banq
 */

public interface Node extends java.io.Serializable {

  public Integer getID();

  public void setID(Integer Id);

  public Integer getParentID();

  public void setParentID(Integer parentID);

  public void setOrderInt(Integer orderInt);

  public Integer getOrderInt();

  public List getChildrenIDs();

  public void setChildrenIDs(List childrenIDs);


}
