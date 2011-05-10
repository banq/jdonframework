package com.jdon.container.annotation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.jdon.container.access.TargetMetaDefHolder;

public class AnnotationHolder {

	public final static String NAME = AnnotationHolder.class.getSimpleName();

	// key component name, class is Component class
	private Map<String, Class> classes;

	// key Component class, class is component name
	private Map<Class, String> xClasses;
	private Map<String, Class> interceptors;
	private TargetMetaDefHolder targetMetaDefHolder;

	public AnnotationHolder() {
		this.classes = new LinkedHashMap();
		this.xClasses = new LinkedHashMap();
		this.interceptors = new LinkedHashMap();
		this.targetMetaDefHolder = new TargetMetaDefHolder();
	}

	public void addComponent(String name, Class componentClass) {
		classes.put(name, componentClass);
		xClasses.put(componentClass, name);
	}

	public Set<String> getComponentNames() {
		return classes.keySet();
	}

	public Class getComponentClass(String name) {
		return classes.get(name);
	}

	public String getComponentName(Class componentClass) {
		return xClasses.get(componentClass);
	}

	public Map<String, Class> getInterceptors() {
		return interceptors;
	}

	public Map<Class, String> getxClasses() {
		return xClasses;
	}

	public TargetMetaDefHolder getTargetMetaDefHolder() {
		return targetMetaDefHolder;
	}

	public void setTargetMetaDefHolder(TargetMetaDefHolder targetMetaDefHolder) {
		this.targetMetaDefHolder = targetMetaDefHolder;
	}

}
