package com.jdon.controller.model;

/**
 * the class control ModelIF interface and annotation Model
 * 
 * @author banq
 * @see com.jdon.annotation.Model
 * @see com.jdon.controller.model.ModelIF
 */
public class ModelUtil {

	public static boolean isModel(Object model) {
		if (model == null) return false;
		return isModel(model.getClass());
	}
	
	public static boolean isModel(Class clazz) {
		boolean isModelIF = false;
		if (ModelIF.class.isAssignableFrom(clazz) || clazz.isAnnotationPresent(com.jdon.annotation.Model.class))
			isModelIF = true;
		return isModelIF;
	}

	public static boolean isCachable(Object model) {
		boolean isCachable = false;
		if (model == null)
			return isCachable;
		if (ModelIF.class.isAssignableFrom(model.getClass())) {
			ModelIF modelc = (ModelIF) model;
			if (modelc.isCacheable())
				isCachable = true;
		} else if (model.getClass().isAnnotationPresent(com.jdon.annotation.Model.class)) {
			com.jdon.annotation.Model modela = model.getClass().getAnnotation(com.jdon.annotation.Model.class);
			if (modela.isCacheable())
				isCachable = true;
		}
		return isCachable;
	}
	
	public static void setCachable(Object model, boolean cachable){
		if (model == null) return;
		if (ModelIF.class.isAssignableFrom(model.getClass())) {
			ModelIF modelc = (ModelIF) model;
			modelc.setCacheable(cachable);
		} 
	}

	public static boolean isModified(Object model) {
		boolean isModified = false;
		if (model == null)
			return isModified;
		if (ModelIF.class.isAssignableFrom(model.getClass())) {
			ModelIF modelc = (ModelIF) model;
			if (modelc.isModified())
				isModified = true;
		} else if (model.getClass().isAnnotationPresent(com.jdon.annotation.Model.class)) {
			com.jdon.annotation.Model modela = model.getClass().getAnnotation(com.jdon.annotation.Model.class);
			if (modela.isModified())
				isModified = true;
		}
		return isModified;
	}
	
	public static void setModified(Object model, boolean modified){
		if (model == null) return;
		if (ModelIF.class.isAssignableFrom(model.getClass())) {
			ModelIF modelc = (ModelIF) model;
			modelc.setModified(modified);
		} 
	}

}
