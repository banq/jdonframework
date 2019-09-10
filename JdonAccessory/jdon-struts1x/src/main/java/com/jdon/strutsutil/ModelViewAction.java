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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.model.ModelForm;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;

/**
 * this class can be invoked in browser's url this class will push create/insert
 * view page or editable view page.
 * 
 * the push result is according action's value: 1. null or create ; will push event
 * view page that user can create; 2. edit; will push event editbale view page that
 * has existed data;
 * 
 * how to use this class? 1. this class can be configured in struts-config.xml
 * 
 * <action name="productForm" type="com.jdon.strutsutil.ModelViewAction"
 * validate="false" scope="request" path="/admin/productAction"> <forward
 * name="create" path="/customer.jsp" /> <forward name="edit"
 * path="/customer.jsp" /> </action> forward's value is same as action's value
 * if the action value is not create or edit, the action will directly forward
 * the action forward page, example: the action vaule is XXXX: <forward
 * name="XXXX" path="/xxxx.jsp" />
 * 
 * 2. setup in ApplicationResources.propeties id.notfound =not found the record
 * (in your language)
 * 
 * @author banq
 */
public class ModelViewAction extends ModelBaseAction {

	private final static String module = ModelViewAction.class.getName();

	/**
	 * accept the form submit, action parameter must be : create or edit; if
	 * not, will directly forward the jsp page mapping for the action value;
	 * 
	 * 
	 */
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Debug.logVerbose("[JdonFramework]--> enter ModelViewAction process ", module);
		intContext(this.getServlet().getServletContext());

		ModelForm modelForm = FormBeanUtil.getModelForm(actionMapping, actionForm, request);

		if ((UtilValidate.isEmpty(modelForm.getAction())) || modelForm.getAction().equalsIgnoreCase(ModelForm.CREATE_STR)) {
			Debug.logVerbose("[JdonFramework]--> enter create process ", module);
			modelForm.setAction(ModelForm.CREATE_STR);
			createViewPage.doCreate(actionMapping, modelForm, request);

		} else if (modelForm.getAction().equalsIgnoreCase(ModelForm.EDIT_STR)) {

			Debug.logVerbose("[JdonFramework]--> enter " + ModelForm.EDIT_STR + " process ", module);
			Object model = editeViewPage.getModelForEdit(actionMapping, modelForm, request, this.servlet.getServletContext());
			if (model == null) // not found the model
				return errorsForward(modelForm.getAction(), actionMapping, request);

		} else if (modelForm.getAction().equalsIgnoreCase(ModelForm.VIEW_STR)) {

			Debug.logVerbose("[JdonFramework]--> enter " + ModelForm.VIEW_STR + " process ", module);
			Object model = editeViewPage.getModelForEdit(actionMapping, modelForm, request, this.servlet.getServletContext());
			if (model == null) // not found the model
				return errorsForward(modelForm.getAction(), actionMapping, request);

		} else {// other regard as create
			Debug.logVerbose("[JdonFramework]-->action value not supported, enter create process2 ", module);
			modelForm.setAction(ModelForm.CREATE_STR);
			createViewPage.doCreate(actionMapping, modelForm, request);
		}
		Debug.logVerbose("[JdonFramework]--> push the jsp that forward name is '" + modelForm.getAction() + "'", module);
		return actionMapping.findForward(modelForm.getAction());
	}

	private ActionForward errorsForward(String action, ActionMapping actionMapping, HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
		ActionMessage error = new ActionMessage("id.notfound");
		errors.add(ActionErrors.GLOBAL_MESSAGE, error);
		saveErrors(request, errors);
		if (actionMapping.findForward(FormBeanUtil.FORWARD_FAILURE_NAME) != null)
			return actionMapping.findForward(FormBeanUtil.FORWARD_FAILURE_NAME);
		else
			return actionMapping.findForward(action);
	}

}
