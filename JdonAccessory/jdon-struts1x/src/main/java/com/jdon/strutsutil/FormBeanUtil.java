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

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.controller.events.EventModel;
import com.jdon.controller.events.EventSupport;
import com.jdon.model.ModelForm;
import com.jdon.util.Debug;

/**
 * 工具类，相关ActionForm 或Model之类的工具箱
 * 
 * <p>
 * Copyright: Jdon.com Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author banq
 * @version 1.0
 */
public final class FormBeanUtil {

	public final static String module = FormBeanUtil.class.getName();

	public final static String FORWARD_SUCCESS_NAME = "success";

	public final static String FORWARD_FAILURE_NAME = "failure";

	/**
	 * 将ActionForm保存在struts_config.xml定义的attribute中
	 * 
	 * @param form
	 * @param mapping
	 * @param request
	 */
	public static void saveActionForm(ActionForm form, ActionMapping mapping, HttpServletRequest request) {
		if ((form != null) && (mapping.getAttribute() != null)) {
			if ("request".equals(mapping.getScope())) {
				request.setAttribute(mapping.getAttribute(), form);
			} else {
				HttpSession session = request.getSession();
				session.setAttribute(mapping.getAttribute(), form);
				request.setAttribute(mapping.getAttribute(), form);
			}
		}
	}

	/**
	 * 将保存在struts_config.xml定义的attribute中ActionForm取出
	 * 
	 * @param form
	 * @param mapping
	 * @param request
	 */
	public static ActionForm loadActionForm(ActionMapping mapping, HttpServletRequest request) {
		if ("request".equals(mapping.getScope())) {
			return (ActionForm) request.getAttribute(mapping.getAttribute());
		} else {
			HttpSession session = request.getSession();
			return (ActionForm) session.getAttribute(mapping.getAttribute());
		}
	}

	/**
	 * lookup ActionForm in
	 * 
	 * @param request
	 * @return
	 */
	public static ActionForm lookupActionForm(HttpServletRequest request, String formName) {
		ActionForm actionForm = null;
		actionForm = (ActionForm) request.getAttribute(formName);
		if (actionForm == null && request.getSession(false) != null) {
			HttpSession session = request.getSession(false);
			actionForm = (ActionForm) session.getAttribute(formName);
		}
		return actionForm;
	}

	/**
	 * 删除保存在attribute中的ActionForm实例
	 * 
	 * @param mapping
	 * @param request
	 */
	public static void removeActionForm(ActionMapping mapping, HttpServletRequest request) {
		if (mapping.getAttribute() != null) {
			if ("request".equals(mapping.getScope()))
				request.removeAttribute(mapping.getAttribute());
			else {
				HttpSession session = request.getSession();
				session.removeAttribute(mapping.getAttribute());
				request.removeAttribute(mapping.getAttribute());
			}
		}
	}

	public static String getFormName(ActionMapping mapping) throws Exception {
		String formName = "NoFormName Error!";
		if (mapping.getName() != null)
			formName = mapping.getName();
		else if ((mapping.getAttribute() != null))
			formName = mapping.getAttribute();
		else
			throw new Exception("not found the actionForm name in action configure");
		return formName;
	}

	public static ModelForm getModelForm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request) throws Exception {

		if (actionForm == null) {
			String msg = " not found the actionForm name in action configure";
			Debug.logError(msg);
			throw new Exception(msg);
			// modelForm = createModelFormNow(actionMapping, actionForm,
			// request);
		}

		ModelForm modelForm = null;
		try {
			modelForm = (ModelForm) actionForm;
		} catch (ClassCastException e) {
			String msg = "your class:" + actionForm.getClass().getName() + " isn't the subclass of com.jdon.model.ModelForm";
			Debug.logVerbose(msg, module);
			throw new Exception(msg);
		}

		return modelForm;
	}

	/**
	 * 根据struts-config.xml配置立即创建ActionForm
	 * 
	 * @param actionMapping
	 *            ActionMapping
	 * @param actionForm
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param moduleConfig
	 *            ModuleConfig
	 * @return ModelForm
	 * @throws Exception
	 *             private static ModelForm createModelFormNow(ActionMapping
	 *             actionMapping, ActionForm actionForm, HttpServletRequest
	 *             request) throws Exception {
	 * 
	 * 
	 *             Debug.logVerbose(
	 *             "[JdonFramework] not found event existed ModelForm, create it now"
	 *             , module); ModuleConfig moduleConfig =
	 *             moduleUtils.getModuleConfig(request,
	 *             request.getSession().getServletContext()); ModelForm form =
	 *             null; String formName = null; String formClass = null; try {
	 *             formName = getFormName(actionMapping); FormBeanConfig
	 *             formConfig = moduleConfig.findFormBeanConfig(formName); if
	 *             (formConfig == null) { throw new
	 *             Exception(" not found config for " + formName); } formClass =
	 *             formConfig.getType();
	 * 
	 *             ClassLoader classLoader =
	 *             Thread.currentThread().getContextClassLoader(); form =
	 *             (ModelForm) classLoader.loadClass(formClass).newInstance();
	 * 
	 *             String action = request.getParameter("action"); if (action ==
	 *             null) action = request.getParameter("method");
	 *             form.setAction(action);
	 * 
	 *             request.setAttribute(formName, form); } catch (Exception ex)
	 *             { Debug.logError("[JdonFramework] formName:" + formName +
	 *             "formClass create error :" + formClass + ex, module); }
	 *             return form; }
	 */
	public static boolean validateAction(String actionName, ActionMapping mapping) {
		boolean res = true;
		int result = actionTransfer(actionName); // 如果没有使用规定名称
		if (result == 0)
			res = false;

		if (mapping.findForward(actionName) == null) // 如果配置文件没有该名称
			res = false;

		return res;

	}

	public static String getName(HttpServletRequest request) throws Exception {
		Principal principal = request.getUserPrincipal();
		if (principal == null) {
			Debug.logError("[JdonFramework] No Principal", module);
			throw new Exception(" No Principal");
		}
		return principal.getName();
	}

	public static ActionErrors notNull(Object object, String errorsInfo) {
		ActionErrors errors = new ActionErrors();
		if (object == null) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorsInfo));
		}
		return errors;
	}

	/**
	 * create event EventModel from event existed ModelForm. it is only for
	 * create/edit/delete of ModelSaveAction
	 */
	public static EventModel createEvent(ModelForm form, Object model) throws Exception {
		EventModel em = new EventModel();
		try {
			PropertyUtils.copyProperties(model, form);
			em.setModelIF(model);
			String action = form.getAction();
			em.setActionName(action);
			em.setActionType(FormBeanUtil.actionTransfer(action));
		} catch (Exception ex) {
			Debug.logError("[JdonFramework]create Event error:" + ex, module);
			throw new Exception(ex);
		}
		return em;
	}

	public static int actionTransfer(String actionName) {
		if (actionName.equalsIgnoreCase(ModelForm.CREATE_STR))
			return EventSupport.CREATE;
		else if (actionName.equalsIgnoreCase(ModelForm.EDIT_STR))
			return EventSupport.EDIT;
		else if (actionName.equalsIgnoreCase(ModelForm.UPDATE_STR))
			return EventSupport.EDIT;
		else if (actionName.equalsIgnoreCase(ModelForm.DELETE_STR))
			return EventSupport.DELETE;
		else
			return 0;
	}

}
