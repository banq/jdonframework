/**
 * Copyright 2005 Jdon.com
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

package sample.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jdon.model.ModelForm;
import com.jdon.util.UtilValidate;

/**
 * 
 * @author banq
 * @version JdonFramework 2005 v1.0
 */
public class UserActionForm extends ModelForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String name;

	public String getName() {
		return name;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors actionErrors = null;
		ArrayList errorList = new ArrayList();
		doValidate(mapping, request, errorList);
		request.setAttribute("errors", errorList);
		if (!errorList.isEmpty()) {
			actionErrors = new ActionErrors();
			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("global.error"));
		}
		return actionErrors;
	}

	public void doValidate(ActionMapping mapping, HttpServletRequest request, List errors) {
		addErrorIfStringEmpty(errors, "UserId is required", getUserId());

		if (!UtilValidate.isInteger(getUserId().toString())) {
			errors.add("UserId must be digit (0 .. 9)");
		}
	}

	/* Protected Methods */

	protected void addErrorIfStringEmpty(List errors, String message, String value) {
		if (value == null || value.trim().length() < 1) {
			errors.add(message);
		}
	}

}
