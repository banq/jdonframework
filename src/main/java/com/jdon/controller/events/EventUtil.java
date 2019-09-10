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

package com.jdon.controller.events;

public class EventUtil {
  public final static String VIEW_STR = "view";
  public final static String CREATE_STR = "create";
  public final static String EDIT_STR = "edit";
  public final static String DELETE_STR = "delete";

  public static int actionTransfer(String actionName) {
    if (actionName.equalsIgnoreCase(CREATE_STR))
      return EventSupport.CREATE;
    else if (actionName.equalsIgnoreCase(EDIT_STR))
      return EventSupport.EDIT;
    else if (actionName.equalsIgnoreCase(DELETE_STR))
      return EventSupport.DELETE;
    else if (actionName.equalsIgnoreCase(VIEW_STR))
      return EventSupport.VIEW;
    else
      return 0;

  }

}
