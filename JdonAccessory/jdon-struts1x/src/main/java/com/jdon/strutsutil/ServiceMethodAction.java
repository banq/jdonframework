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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.controller.WebAppUtil;
import com.jdon.controller.events.EventModel;
import com.jdon.controller.model.ModelUtil;
import com.jdon.model.ModelForm;
import com.jdon.model.ModelHandler;
import com.jdon.model.handler.HandlerMetaDef;
import com.jdon.util.Debug;

/**
 * Command pattern for service invoke sample: browser url: /aaa.do?method=xxxxx
 * 
 * xxxxx is the service's method, such as:
 * 
 * public interface TestService{ 　　 void xxxxx(EventModel em);　　 }
 * 
 * to implements those function as above, we must confiure the struts-config.xml
 * and jdonframework.xml
 * 
 * <action path="/aaa"
 * type="com.jdon.strutsutil.com.jdon.strutsutil.ServiceMethodAction"
 * 　name="aForm" scope="request"validate="false"> <forward name="xxxxx"
 * path="/xxx.jsp"/> </action>
 * 
 * jdonframework.xml <model key="primary key of your model" class
 * ="your model class"> <actionForm name="aForm"/> <handler> <service
 * ref="testService" /> </handler> </model> <pojoService name="testService" 　　　　
 * 　　　　　　 class="com.jdon.framework.test.service.TestServicePOJOImp"/>
 * 
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public class ServiceMethodAction extends ModelBaseAction {
	private final static String module = ServiceMethodAction.class.getName();

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		intContext(this.getServlet().getServletContext());

		ModelForm modelForm = (ModelForm) actionForm;
		if ((modelForm == null) || (modelForm.getMethod() == null)) {
			Debug.logError("[JdonFramework] no form or no action/method ", module);
			return actionMapping.findForward("forward");
		}
		String formName = FormBeanUtil.getFormName(actionMapping);
		Debug.logVerbose("[JdonFramework]--> enter ServiceMethodAction, formName = " + formName, module);
		ModelHandler modelHandler = modelManager.borrowtHandlerObject(formName);
		EventModel em = new EventModel();
		try {
			Object modelDTO = modelManager.getModelObject(formName);
			modelHandler.formCopyToModelIF(modelForm, modelDTO);

			HandlerMetaDef hm = modelHandler.getModelMapping().getHandlerMetaDef();

			String serviceName = hm.getServiceRef();

			em.setActionName(modelForm.getMethod());
			em.setModelIF(modelDTO);
			Object result = WebAppUtil.callService(serviceName, modelForm.getMethod(), new Object[] { em }, request);
			if (ModelUtil.isModel(result)) {
				modelDTO = result;
				modelHandler.modelIFCopyToForm(modelDTO, modelForm);
			} else if (result != null)
				request.setAttribute(result.getClass().getSimpleName(), result);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]please check your service 、 model or form :" + ex, module);
			throw new Exception("System error! please call system Admin." + ex);
		} finally {
			modelManager.returnHandlerObject(modelHandler);
		}
		if (em.getErrors() != null) {
			Debug.logError("[JdonFramework] error happend " + em.getErrors(), module);
			ActionMessages errors = new ActionMessages();
			ActionMessage error = new ActionMessage(em.getErrors());
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
			saveErrors(request, errors);
		}
		ActionForward sucessaf = actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
		ActionForward actionnameaf = actionMapping.findForward(modelForm.getAction());
		if (sucessaf != null) {
			return sucessaf;
		} else if (actionnameaf != null) {
			return actionnameaf;
		} else {
			Debug.logError("[JdonFramework] forward name is not '" + FormBeanUtil.FORWARD_SUCCESS_NAME + "'", module);
			return null;
		}
	}
}
