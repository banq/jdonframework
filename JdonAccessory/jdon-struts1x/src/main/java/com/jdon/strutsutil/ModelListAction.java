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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.jdon.controller.model.Model;
import com.jdon.controller.model.ModelUtil;
import com.jdon.controller.model.PageIterator;
import com.jdon.domain.model.cache.ModelKey;
import com.jdon.util.Debug;

/**
 * batch query action
 * 
 * @author banq
 */
public abstract class ModelListAction extends ModelBaseAction {

	private final static String module = ModelListAction.class.getName();

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		try {
			intContext(this.getServlet().getServletContext());

			ModelListForm listForm = getModelListForm(actionMapping, actionForm, request);
			int start = listForm.getStart();
			int count = listForm.getCount();

			PageIterator pageIterator = getPageIterator(request, start, count);
			if (pageIterator != null) {// by pageIterator
				if (pageIterator.isElementsTypeIsKey()) {
					setModellistByKey(listForm, pageIterator, request);
				} else {
					setModellistByModel(listForm, pageIterator, request);
				}
			} else {
				Debug.logError("getPageIterator not be implemented, you must implement either of them ", module);
				listForm.setList(new ArrayList());
			}

			listForm.setOneModel(setupOneModelIF(request));
			customizeListForm(actionMapping, actionForm, request, listForm);
		} catch (Exception e) {
			Debug.logError(e);
			return actionMapping.findForward(FormBeanUtil.FORWARD_FAILURE_NAME);
		}

		if (actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME) == null)
			Debug.logError("not found the forward name '" + FormBeanUtil.FORWARD_SUCCESS_NAME + "' in struts-config.xml", module);

		return actionMapping.findForward(FormBeanUtil.FORWARD_SUCCESS_NAME);
	}

	private void setModellistByKey(ModelListForm listForm, PageIterator pageIterator, HttpServletRequest request) {
		Debug.logVerbose("[JdonFramework] setModellistByKey  AllCount=" + pageIterator.getAllCount(), module);
		Collection c = null;
		try {
			listForm.setAllCount(pageIterator.getAllCount());
			if (pageIterator.getCount() != 0)
				listForm.setCount(pageIterator.getCount());// for block
			// pageIterator
			c = getModelList(request, pageIterator);
			Debug.logVerbose("[JdonFramework] listForm 's property: getList size is " + c.size(), module);
			pageIterator.reset();
		} catch (Exception e) {
			Debug.logError(" setModellistByKey error " + e, module);
			c = new ArrayList();
		}
		listForm.setList(c);
	}

	/*
	 * the elements in pageIterator is Model type we directly add them in result
	 */
	private void setModellistByModel(ModelListForm listForm, PageIterator pageIterator, HttpServletRequest request) {
		Collection c = null;
		try {
			listForm.setAllCount(pageIterator.getAllCount());
			if (pageIterator.getCount() != 0)
				listForm.setCount(pageIterator.getCount());
			c = new ArrayList(pageIterator.getSize());
			while (pageIterator.hasNext()) {
				Object o = pageIterator.next();
				if (o != null)
					c.add(o);
			}
			Debug.logVerbose("[JdonFramework] listForm 's property: getList size is " + c.size(), module);
			pageIterator.reset();
		} catch (Exception e) {
			Debug.logError(" setModellistByModel error " + e, module);
			c = new ArrayList();
		}
		listForm.setList(c);
	}

	/**
	 * 获得ModelListForm实例
	 * 
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @return
	 * @throws java.lang.Exception
	 */
	protected ModelListForm getModelListForm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request) throws Exception {
		ModelListForm modelListForm = null;
		if (actionForm == null) {
			modelListForm = new ModelListForm();
			FormBeanUtil.saveActionForm(modelListForm, actionMapping, request);
		} else if (actionForm instanceof ModelListForm)
			modelListForm = (ModelListForm) actionForm;

		if (modelListForm == null)
			throw new Exception("not found the bean of com.jdon.strutsutil.ModelListForm");
		else
			return modelListForm;

	}

	/**
	 * 根据PageIterator中ID集合，获得相应的Model集合
	 * 
	 * @param request
	 * @param pageIterator
	 * @return Model集合
	 * @throws java.lang.Exception
	 */
	protected List getModelList(HttpServletRequest request, PageIterator pageIterator) throws Exception {
		Debug.logVerbose("[JdonFramework] getModelList  page size=" + pageIterator.getSize(), module);
		List list = new ArrayList(pageIterator.getSize());
		Object model = null;
		Class modelClass = null;
		while (pageIterator.hasNext()) {
			Object dataKey = pageIterator.next();
			if ((modelClass == null) || (!isEnableCache())) {
				Debug.logVerbose("[JdonFramework] getCache from db. ", module);
				model = fetchModel(request, dataKey);
				if (model != null)
					modelClass = model.getClass();
				else
					continue;
			} else {
				// 首先从缓存中获取
				ModelKey modelKey = new ModelKey(dataKey, modelClass);
				model = modelManager.getCache(modelKey);
				if (model == null) {
					model = fetchModel(request, dataKey);
					if (model != null && ModelUtil.isModel(model.getClass()))
						modelManager.addCache(modelKey, model);
				}
			}
			if (model != null)
				list.add(model);
		}
		return list;
	}

	/**
	 * 定制ModelListForm
	 * 
	 * 缺省ModelListForm是只有一个List，包含一种Model集合
	 * 有的应用可能是两种Model集合，可以继承ModelListForm实现Map-backed ActionForms
	 * 再继承本Action，实现本方法。
	 * 
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param modelListForm
	 * @throws java.lang.Exception
	 */
	public void customizeListForm(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, ModelListForm modelListForm)
			throws Exception {

	}

	/**
	 * 
	 * decide if enable cacche? the sub-class can unable cache by overrid this
	 * method
	 * 
	 * @param enable
	 */
	protected boolean isEnableCache() {
		return true;
	}

	private Object fetchModel(HttpServletRequest request, Object dataKey) throws Exception {
		if (dataKey == null)
			return null;
		Object model = null;
		try {
			model = findModelIFByKey(request, dataKey);
			if (model == null) {
				Debug.logWarning("[JdonFramework] the model is null for the primary key=" + dataKey + " and data type="
						+ dataKey.getClass().getName() + ", reasons:", module);
				Debug.logWarning("[JdonFramework] 1. maybe the data in database was deleted ", module);
				Debug.logWarning("[JdonFramework] 2. event error occured in the findModelByKey method of the class " + this.getClass().getName(), module);
			}
		} catch (Exception ex) {
			Debug.logError(ex, module);
		}
		return model;
	}

	protected Object setupOneModelIF(HttpServletRequest request) {
		return setupOneModel(request);
	}

	protected Model setupOneModel(HttpServletRequest request) {
		return null;
	}

	/**
	 * get event PageIterator from the service
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param start
	 *            int
	 * @param count
	 *            int
	 * @return PageIterator
	 */
	public abstract PageIterator getPageIterator(HttpServletRequest request, int start, int count);

	/**
	 * obtain event Model instance from service layer
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param key
	 *            Object the primary key of the Model
	 * @return ModelIF return the Model object from service layer
	 */
	public Object findModelIFByKey(HttpServletRequest request, Object key) {
		return findModelByKey(request, key);
	}

	/**
	 * for old version
	 */
	public Model findModelByKey(HttpServletRequest request, Object key) {
		return null;
	}

}
