/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jdon.strutsutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.model.ModelForm;
import com.jdon.util.Debug;

/**
 * 专门显示Model类。
 * 
 * 为安全起见，单独设立一个类。同时加入缓存机制。 request的nocache参数表示失效缓存机制。 modelDispAction.do?nocache
 * 
 * <p>
 * Copyright: Jdon.com Copyright (c) 2003
 * </p>
 * <p>
 * </p>
 * 
 * @author banq
 * @version 1.0
 */
public class ModelDispAction extends  ModelBaseAction  {

    private final static String module = ModelDispAction.class.getName();

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        Debug.logVerbose("[JdonFramework]--> enter ModelDispAction process ", module);
        intContext(this.getServlet().getServletContext());
        
        ModelForm modelForm = FormBeanUtil.getModelForm(actionMapping, actionForm, request);

        Object model = viewPageUtil.getModelForEdit(actionMapping, modelForm, request);

        if (model == null) { //如果查询失败，显示出错信息
            ActionMessages errors = new ActionMessages();
            Debug.logError("[JdonFramework]id.notfound in database", module);
            ActionMessage error = new ActionMessage("id.notfound");
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            ActionForward af = actionMapping.findForward(FormBeanUtil.FORWARD_FAILURE_NAME);
            if (af != null)
                return af;
            else
                return actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
        } else {
            return actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
        }

    }
    
    

}
