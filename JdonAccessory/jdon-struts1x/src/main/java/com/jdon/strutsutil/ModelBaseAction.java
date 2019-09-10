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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jdon.controller.WebAppUtil;
import com.jdon.model.ModelHandlerManager;
import com.jdon.strutsutil.util.CreateViewPageUtil;
import com.jdon.strutsutil.util.EditeViewPageUtil;
import com.jdon.strutsutil.util.ViewPageUtil;
import com.jdon.util.Debug;

/**
 * @author <event href="mailto:banqiao@jdon.com">banq</event>
 * 
 */
public abstract class ModelBaseAction extends Action {

	private final static String module = ModelBaseAction.class.getName();

	protected ModelHandlerManager modelManager;
	protected CreateViewPageUtil createViewPage;
	protected EditeViewPageUtil editeViewPage;
	protected ViewPageUtil viewPageUtil;

	public void intContext(ServletContext sc) {
		if (modelManager == null) {
			modelManager = (ModelHandlerManager) WebAppUtil.getComponentInstance("modelHandlerManager", sc);
			createViewPage = new CreateViewPageUtil(modelManager);
			editeViewPage = new EditeViewPageUtil(modelManager);
			viewPageUtil = new ViewPageUtil(modelManager);
		}
	}

	public abstract ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception;

	protected void checkConfigName(ActionMapping actionMapping) throws Exception {
		if (actionMapping.findForward(FormBeanUtil.FORWARD_FAILURE_NAME) == null)
			Debug.logError("not found the forward name '" + FormBeanUtil.FORWARD_FAILURE_NAME + "' in struts-config.xml", module);
		else if (actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME) == null)
			Debug.logError("not found the forward name '" + FormBeanUtil.FORWARD_SUCCESS_NAME + "' in struts-config.xml", module);

	}
}
