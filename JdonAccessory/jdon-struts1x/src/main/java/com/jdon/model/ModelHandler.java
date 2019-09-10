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

package com.jdon.model;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;

import com.jdon.controller.events.EventModel;
import com.jdon.controller.model.Model;
import com.jdon.model.config.ModelMapping;
import com.jdon.strutsutil.util.CreateViewPageUtil;
import com.jdon.util.Debug;

/**
 * Presentation layer delegateion class Presentation layer need three common
 * works collaboration with service layer:
 * 
 * 1. Presentation layer need event ModelForm instance that include some initially
 * data, these data must obtain from service.
 * 
 * 2. Presentation layer must obtain event existed ModelForm instance from service
 * 
 * 3. Presentation layer submit the data ModelForm instance that user created or
 * update, service layer must persistence them
 * 
 * Framework's user can extends this ModelHandler, and configure your
 * jdonframework.xml, so ask framework active it: <model ...> <handler
 * class="your ModelHandler concrete class" /> </model>
 * 
 * @author banq
 */

public abstract class ModelHandler implements ServiceHandler {

	private final static String module = ModelHandler.class.getName();

	protected ModelMapping modelMapping;

	public void setModelMapping(ModelMapping modelMapping) {
		this.modelMapping = modelMapping;
	}

	public ModelMapping getModelMapping() {
		return modelMapping;
	}

	/**
	 * 
	 * Presentation layer need event ModelForm instance that include some initially
	 * data, these data must obtain from service. this method implements these
	 * functions in presentation layer's ModelHanlder concrete class;
	 * 
	 * this method is available in pushing create or edit view page
	 * 
	 * @param request
	 * @return ModelForm instance
	 * @throws java.lang.Exception
	 * 
	 */
	public ModelForm initForm(HttpServletRequest request) throws Exception {
		return null;
	}

	/**
	 * for old version below 1.4
	 */
	public Model initModel(HttpServletRequest request) throws Exception {
		return null;
	}

	/**
	 * Presentation layer need event ModelForm instance that include some initially
	 * data, these data must obtain from service. this method implements these
	 * functions by deleagating service.
	 * 
	 * this mehtod is only availabe in pushing create view page in pushing
	 * editable view page, the model is obtained by it's key, not from this
	 * method;
	 * 
	 * @param request
	 * @return Model instance
	 * @throws Exception
	 * @see CreateViewPageUtil
	 */
	public Object initModelIF(EventModel em, ModelForm form, HttpServletRequest request) throws Exception {
		return initModel(request);
	}

	public abstract Object initModelIF(EventModel em, ModelForm form, ServletContext sc) throws Exception;

	/**
	 * for old version below 1.4
	 */
	public Model findModelByKey(String keyValue, HttpServletRequest request) throws Exception {
		return null;

	}

	/**
	 * obtain event existed Model instance by his primtive key.
	 * 
	 * 
	 * @param keyValue
	 *            primtive key
	 * @param request
	 * @return Model
	 * @throws java.lang.Exception
	 */
	public Object findModelIF(Object keyValue, HttpServletRequest request) throws Exception {
		return findModelByKey((String) keyValue, request); // for old version
	}

	public abstract Object findModelIF(Object keyValue, ServletContext sc) throws Exception;

	/**
	 * package the Model instance that has user's input data to EventModel
	 * object, and submit them to servier layer;
	 * 
	 * @param em
	 *            package Model instance
	 * @param request
	 * @throws java.lang.Exception
	 */
	public abstract void serviceAction(EventModel em, HttpServletRequest request) throws Exception;

	public abstract void serviceAction(EventModel em, ServletContext sc) throws Exception;

	/**
	 * for old version below 1.4
	 */
	public void modelCopyToForm(Model model, ModelForm form) throws Exception {
		try {
			PropertyUtils.copyProperties(form, model);
		} catch (Exception e) {
			String error = " Model:" + model.getClass().getName() + " copy To ModelForm:" + form.getClass().getName() + " error:" + e;
			Debug.logError(error, module);
			throw new Exception(error);
		}
	}

	/**
	 * Model object's data transfer to ModelForm object
	 * 
	 * default implemention is copy mapping between with them; another
	 * implemention is : PropertyUtils.setProperty extends this class , and
	 * override this method
	 * 
	 * @param model
	 * @param form
	 * @throws java.lang.Exception
	 */
	public void modelIFCopyToForm(Object model, ModelForm form) throws Exception {
		if (model == null || form == null)
			return;
		if (model instanceof Model) { // for below 1.4 version
			modelCopyToForm((Model) model, form);
			return;
		}
		try {
			PropertyUtils.copyProperties(form, model);
		} catch (Exception e) {
			String error = "error happend:" + e + " in copy Model:" + model.getClass().getName() + " To ModelForm:" + form.getClass().getName()
					+ " there maybe are exceptions in setter/getter method of your ModelForm or Model, add try..catch in them. ";
			Debug.logError(error, module);
			throw new Exception(error);
		}
	}

	/**
	 * for old version below 1.4
	 */
	public void formCopyToModel(ModelForm form, Model model) throws Exception {
		try {
			PropertyUtils.copyProperties(model, form);
		} catch (Exception e) {
			String error = " ModelForm:" + form.getClass().getName() + " copy To Model:" + model.getClass().getName() + " error:" + e;
			Debug.logError(error, module);
			throw new Exception(error);
		}
	}

	/**
	 * ModelForm object's data transfer to Model object
	 * 
	 * default implemention is copy mapping between with them;
	 * 
	 * another implemention: String propertyName =
	 * StringUtil.getLastString(model.getClass().getName()); Model hasDataModel
	 * = PropertyUtils.getProperty(form, propertyName); model = hasDataModel;
	 * 
	 * extends this class , and override this method
	 * 
	 * @param model
	 * @param form
	 * @throws java.lang.Exception
	 */
	public void formCopyToModelIF(ModelForm form, Object model) throws Exception {
		if (model == null || form == null)
			return;
		if (model instanceof Model) { // for below 1.4 version
			formCopyToModel(form, (Model) model);
			return;
		}
		try {
			PropertyUtils.copyProperties(model, form);
		} catch (InvocationTargetException ie) {
			String error = "error happened in getXXX method of ModelForm:" + form.getClass().getName() + " error:" + ie;
			Debug.logError(error, module);
			throw new Exception(error);

		} catch (Exception e) {
			String error = " ModelForm:" + form.getClass().getName() + " copy To Model:" + model.getClass().getName() + " error:" + e;
			Debug.logError(error, module);
			throw new Exception(error);
		}
	}
}
