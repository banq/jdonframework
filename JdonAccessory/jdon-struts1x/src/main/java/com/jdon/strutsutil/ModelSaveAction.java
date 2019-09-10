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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.controller.events.EventModel;
import com.jdon.model.ModelForm;
import com.jdon.model.ModelHandler;
import com.jdon.model.config.ModelMapping;
import com.jdon.util.Debug;

/**
 * Accept the datas that user submited, and handle them to service layer,
 * service maybe persistence them. it is just like event handler. all functions
 * delegate Modelhandler
 * 
 * 
 * configure in jdonframework.xml:
 * 
 * <action name="productForm" type="com.jdon.strutsutil.ModelSaveAction"
 * input="/admin/product.jsp" scope="request" path="/admin/saveProductAction">
 * <forward name="success" path="/admin/productOk.jsp" /> <forward
 * name="failure" path="/admin/productOk.jsp" /> </action>
 * 
 * notie: the action parameter of the ModelForm(ActionForm) must be: 1. create ,
 * this class will call the create method defined in jdonframework.xml 2. edit ,
 * this class will call the edit method defined in jdonframework.xml 3. delete,
 * this class will call the delete method defined in jdonframework.xml if not
 * above, this class will call the action's value method of the service
 * interface. example: action's value : deleteAll so, your service interface
 * must havs event method: public void deleteAll(EventModel em);
 * 
 * this class will directly call the deleteAll method of the service.
 * 
 * 
 * @author banq
 */
public class ModelSaveAction extends ModelBaseAction {

	private final static String module = ModelSaveAction.class.getName();

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		intContext(this.getServlet().getServletContext());

		checkConfigName(actionMapping);

		EventModel em = null;
		String formName = FormBeanUtil.getFormName(actionMapping);

		ModelHandler modelHandler = modelManager.borrowtHandlerObject(formName);
		try {
			ModelForm form = getModelForm(modelHandler, actionForm, request);
			Object model = makeModel(actionMapping, actionForm, request, modelHandler);
			modelHandler.formCopyToModelIF(form, model);

			em = new EventModel();
			em.setActionName(form.getAction());
			em.setModelIF(model);
			em.setActionType(FormBeanUtil.actionTransfer(form.getAction()));

			Debug.logVerbose("[JdonFramework] save data to database ... ", module);
			// deleagte the Modelhandler's serviceAction
			modelHandler.serviceAction(em, request);

			modelHandler.modelIFCopyToForm(em.getModelIF(), form);

		} catch (Exception ex) {
			Debug.logError("[JdonFramework]please check your service 、 model or form :" + ex, module);
			throw new Exception("System error! please call system Admin." + ex);
		} finally {
			modelManager.returnHandlerObject(modelHandler);
		}

		if (em.getErrors() != null) {
			Debug.logError("[JdonFramework] save error!! " + em.getErrors(), module);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage(em.getErrors());
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
			saveErrors(request, errors);

			ActionForward af = actionMapping.findForward(FormBeanUtil.FORWARD_FAILURE_NAME);
			if (af != null)
				return af;
			else
				return actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
		} else {
			Debug.logVerbose("[JdonFramework] save successfully ... ", module);
			return actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
		}

	}

	/**
	 * get event ModelForm or create it.
	 */
	protected ModelForm getModelForm(ModelHandler modelHandler, ActionForm actionForm, HttpServletRequest request) throws Exception {

		if (actionForm == null) {
			throw new Exception(" must define form-bean as 'action' name in struts-config.xml ");
		}
		ModelForm strutsForm = (ModelForm) actionForm;
		String action = strutsForm.getAction();
		if ((action == null) || (action.length() == 0))
			throw new Exception(" Need event field : <html:hidden property=action /> in jsp's form! ");

		return strutsForm;

	}

	/**
	 * create event Model from the jdonframework.xml
	 * 
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @return Model
	 * @throws java.lang.Exception
	 */
	protected Object makeModel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, ModelHandler modelHandler)
			throws Exception {
		Object model = null;
		try {
			String formName = actionMapping.getName();
			if (formName == null)
				throw new Exception("no define the FormName in struts_config.xml");

			ModelMapping modelMapping = modelHandler.getModelMapping();
			String keyName = modelMapping.getKeyName();
			String keyValue = request.getParameter(keyName);
			if (keyValue == null) {
				Debug.logError("[JdonFramework]Need event model's key field : <html:hidden property=MODEL KEY /> in jsp's form! ", module);
			}

			modelManager.removeCache(keyValue);
			Debug.logVerbose("[JdonFramework] no model cache, keyName is " + keyName, module);
			model = modelManager.getModelObject(formName);

		} catch (Exception e) {
			Debug.logError("[JdonFramework] makeModel error: " + e);
			throw new Exception(e);
		}
		return model;
	}

}
