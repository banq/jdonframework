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
package com.jdon.strutsutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * default action, if use BaseAction, we do not need
 * create event new action for event forward function;
 * 
 * sample:
 *  <action path="/XXX" type="com.jdon.strutsutil.BaseAction"
 *     name="XXX" scope="request" parameter="forward"
 *     validate="false">
 *     <forward name="forward" path="/xxx.jsp"/>
 *		
 *    </action>

 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 *
 */
public class ForwardAction extends Action {

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return actionMapping.findForward("forward");
    }

}
