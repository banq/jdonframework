package com.jdon.strutsutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jdon.controller.WebAppUtil;
import com.jdon.util.Debug;

public class ServiceMethodSimpleAction extends ModelBaseAction {
	private final static String module = ServiceMethodSimpleAction.class.getName();

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String service = request.getParameter("service");
		if (service == null) {
			Debug.logVerbose("need parameter 'service'", module);
			return actionMapping.findForward("failure");
		}
		String method = request.getParameter("method");
		if (method == null) {
			Debug.logVerbose("need parameter 'method'", module);
			return actionMapping.findForward("failure");
		}

		Object result = WebAppUtil.callService(service, method, new Object[] {}, request);
		if (result != null)
			request.setAttribute(result.getClass().getSimpleName(), result);
		ActionForward sucessaf = actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
		ActionForward actionnameaf = actionMapping.findForward(method);
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
