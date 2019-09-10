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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.struts.action.ActionMapping;

import com.jdon.controller.events.EventModel;
import com.jdon.controller.model.ModelUtil;
import com.jdon.domain.model.cache.ModelKey;
import com.jdon.model.ModelForm;
import com.jdon.model.ModelHandler;
import com.jdon.model.ModelHandlerManager;
import com.jdon.model.config.ModelMapping;
import com.jdon.strutsutil.FormBeanUtil;
import com.jdon.util.Debug;

/**
 * prepare for push event editable jsp page. this class will call the service to get
 * event model that has datas. this work will delegate the ModelHandler class
 * 
 * 
 * @author banq
 */
public class EditeViewPageUtil {
	private final static String module = EditeViewPageUtil.class.getName();

	protected ModelHandlerManager modelManager;

	public EditeViewPageUtil(ModelHandlerManager modelManager) {
		this.modelManager = modelManager;
	}

	/**
	 * two things: 1. create event ModelForm null instance 2. obtain event existed Model
	 * instance copy the Model instance to the ModelForm instance
	 * 
	 */
	public Object getModelForEdit(ActionMapping actionMapping, ModelForm modelForm, HttpServletRequest request, ServletContext sc) throws Exception {
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
			Debug.logVerbose("[JdonFramework] got event ModelForm ... ", module);

			Debug.logVerbose("[JdonFramework] prepare to fetch event Model from service layer", module);
			model = fetchModel(request, sc, formName, modelHandler);
			Debug.logVerbose("[JdonFramework] got the Model data successfully..", module);

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
			Object keyValue = getParamKeyValue(request, modelHandler);
			clearModelCache(formName, keyValue, modelHandler);
			if (request.getSession(false) == null)
				model = modelHandler.findModelIF(keyValue, sc);
			else
				model = modelHandler.findModelIF(keyValue, request);
			if (model == null) {
				Debug.logError("[JdonFramework] Error: got event NULL Model..", module);
				throw new Exception("got event NULL Model");
			} else {
				// addModelCache(formName, keyValue, modelHandler, model);
			}
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] the method 'findModelByKey' of your handler or 'getMethod' of service happened error: " + ex, module);
			throw new Exception(ex);
		}
		return model;
	}

	private void clearModelCache(String formName, Object keyValue, ModelHandler modelHandler) {
		Object model = null;
		try {
			ModelKey modelKey = new ModelKey(keyValue, formName);
			model = modelManager.getCache(modelKey);
			if (model != null) {// clear the cache
				modelManager.removeCache(keyValue);
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework] clearModelCache error: " + e);
		}
	}

	public void addModelCache(String formName, Object keyValue, ModelHandler modelHandler, Object model) {
		ModelKey modelKey = new ModelKey(keyValue, formName);
		if (ModelUtil.isModel(model.getClass()))
			modelManager.addCache(modelKey, model);
	}

	/**
	 * 获得参数key值 例如： /admin/productAction.do?action=edit&productId=1721
	 * 缺省:productId为product的modelmapping.xml中key定义值
	 * 
	 * 对于如下调用： /admin/productAction.do?action=edit&userId=16
	 * userId不是modelmapping.xml中key定义值，则需要override本方法，
	 * 
	 * 
	 * @param actionMapping
	 * @param request
	 * @return 参数key值
	 * @throws java.lang.Exception
	 */
	public Object getParamKeyValue(HttpServletRequest request, ModelHandler modelHandler) {

		Object keyValue = null;
		try {
			ModelMapping modelMapping = modelHandler.getModelMapping();
			String keyName = modelMapping.getKeyName();
			Debug.logVerbose("[JdonFramework] the keyName is  " + keyName, module);
			String keyValueS = request.getParameter(keyName);
			Debug.logVerbose("[JdonFramework] got the keyValue is  " + keyValueS, module);
			if (keyValueS == null) {
				Debug.logVerbose("[JdonFramework]the keyValue is null", module);
			}
			Class keyClassType = modelMapping.getKeyClassType();
			if (keyClassType.isAssignableFrom(String.class)) {
				keyValue = keyValueS;
			} else {
				Debug.logVerbose("[JdonFramework] convert String keyValue to" + keyClassType.getName(), module);
				keyValue = ConvertUtils.convert(keyValueS, keyClassType);
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework] getParamKeyValue error: " + e);
		}
		return keyValue;
	}

}
