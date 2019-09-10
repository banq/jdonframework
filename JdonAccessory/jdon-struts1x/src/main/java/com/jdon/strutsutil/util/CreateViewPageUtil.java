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

package com.jdon.strutsutil.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jdon.controller.events.EventModel;
import com.jdon.model.ModelForm;
import com.jdon.model.ModelHandler;
import com.jdon.model.ModelHandlerManager;
import com.jdon.strutsutil.FormBeanUtil;
import com.jdon.util.Debug;

/**
 * prepare for push event event create/insert view page, we need do some ready works.
 * 
 * 
 * @author banq
 */
public class CreateViewPageUtil {
	private final static String module = CreateViewPageUtil.class.getName();

	private ModelHandlerManager modelManager;

	public CreateViewPageUtil(ModelHandlerManager modelManager) {
		this.modelManager = modelManager;
	}

	/**
	 * create event ModelForm object
	 * 
	 * two things: 1. create event ModelForm null instance 2. initial ModelForm
	 * instance value, obtain event inital Model instance copy the Model instance to
	 * the ModelForm instance
	 * 
	 * obtaining datas two ways: 1.call ModelHandler initForm method; 2.call
	 * Modelhandler initModel method;
	 * 
	 */
	public void doCreate(ActionMapping actionMapping, ModelForm modelForm, HttpServletRequest request) throws Exception {
		Debug.logVerbose("[JdonFramework] enter doCreate... ", module);
		String formName = FormBeanUtil.getFormName(actionMapping);
		ModelHandler modelHandler = modelManager.borrowtHandlerObject(formName);
		try {

			ModelForm form = modelHandler.initForm(request);
			if (form != null) {
				form.setAction(ModelForm.CREATE_STR);
				FormBeanUtil.saveActionForm(form, actionMapping, request);
			} else {
				form = modelForm;
			}
			Debug.logVerbose("[JdonFramework] got event ModelForm ... ", module);

			Object modelDTO = modelManager.getModelObject(formName);
			modelHandler.formCopyToModelIF(form, modelDTO);
			EventModel em = new EventModel();
			em.setActionName(form.getAction());
			em.setModelIF(modelDTO);

			modelDTO = initModel(em, request, modelHandler, form);
			// copy initial model to form , intial form;
			if (modelDTO != null) { // the model can be null
				Debug.logVerbose("[JdonFramework] got event Model From the 'initModel' method of the service", module);
				modelHandler.modelIFCopyToForm(modelDTO, form);
			}

		} catch (Exception ex) {
			Debug.logError("[JdonFramework]please check your service 、 model or form, error is: " + ex, module);
			throw new Exception("System error! please call system admin. " + ex);
		} finally {
			modelManager.returnHandlerObject(modelHandler); // 返回modelhandler再用
		}
	}

	private Object initModel(EventModel em, HttpServletRequest request, ModelHandler modelHandler, ModelForm form) throws Exception {
		Object model = null;
		try {
			model = modelHandler.initModelIF(em, form, request);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] the method 'initForm' of your handler or 'initMethod' of your service happened error: " + ex, module);
			throw new Exception(ex);
		}
		return model;
	}

}
