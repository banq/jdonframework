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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.controller.events.EventModel;
import com.jdon.controller.model.ModelUtil;
import com.jdon.model.ModelForm;
import com.jdon.model.ModelHandler;
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
public class ModelDispAction extends ModelBaseAction {

	private final static String module = ModelDispAction.class.getName();

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Debug.logVerbose("[JdonFramework]--> enter ModelDispAction process ", module);
		intContext(this.getServlet().getServletContext());

		ModelForm modelForm = FormBeanUtil.getModelForm(actionMapping, actionForm, request);

		Object model = getModel(actionMapping, modelForm, request, this.getServlet().getServletContext());

		if (model == null) { // 如果查询失败，显示出错信息
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

	public Object getModel(ActionMapping actionMapping, ModelForm modelForm, HttpServletRequest request, ServletContext sc) throws Exception {
		Object model = null;
		ModelHandler modelHandler = null;
		try {
			String formName = FormBeanUtil.getFormName(actionMapping);
			modelHandler = modelManager.borrowtHandlerObject(formName);

			ModelForm form = modelHandler.initForm(request);
			if (form != null) {
				form.setAction(ModelForm.EDIT_STR);
				FormBeanUtil.saveActionForm(form, actionMapping, request);
			} else {
				form = modelForm;
			}
			model = fetchModel(request, sc, formName, modelHandler);
			if (model != null) {
				if (ModelUtil.isModel(model)) {
					modelHandler.modelIFCopyToForm(model, form);
				} else if (EventModel.class.isAssignableFrom(model.getClass())) {
					// EventModel getMyModel(); <getMethod name="getMyModel()"/>
					model = ((EventModel) model).getModelIF();
					if (ModelUtil.isModel(model))
						modelHandler.modelIFCopyToForm(model, form);
				}
			}

		} catch (Exception ex) {
			Debug.logError("[JdonFramework]please check your service 、 model or form, error is: " + ex, module);
		} finally {
			modelManager.returnHandlerObject(modelHandler); // 返回modelhandler再用
		}
		return model;
	}

	protected Object fetchModel(HttpServletRequest request, ServletContext sc, String formName, ModelHandler modelHandler) throws Exception {
		Object model = null;
		try {
			Object keyValue = viewPageUtil.getParamKeyValue(request, modelHandler);
			if (request.getSession(false) == null)
				model = modelHandler.findModelIF(keyValue, sc);
			else
				model = modelHandler.findModelIF(keyValue, request);
			if (model == null) {
				Debug.logError("[JdonFramework] Error: got event NULL Model..", module);
				throw new Exception("got event NULL Model");
			} else {
				// viewPageUtil.addModelCache(formName, keyValue, modelHandler,
				// model);
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] the method 'findModelByKey' of your handler or 'getMethod' of service happened error: " + ex, module);
			throw new Exception(ex);
		}
		return model;
	}

}
