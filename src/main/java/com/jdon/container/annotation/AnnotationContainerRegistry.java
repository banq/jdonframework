package com.jdon.container.annotation;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaDefHolder;
import com.jdon.container.builder.ContainerRegistry;
import com.jdon.container.builder.StartablecomponentsRegistry;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;

public class AnnotationContainerRegistry extends ContainerRegistry {
	public final static String module = AnnotationContainerRegistry.class.getName();

	private ContainerLoaderAnnotation containerLoaderAnnotation;
	private AppContextWrapper context;

	public AnnotationContainerRegistry(AppContextWrapper context, ContainerWrapper containerWrapper,
			ContainerLoaderAnnotation containerLoaderAnnotation) {
		super(containerWrapper);
		this.containerLoaderAnnotation = containerLoaderAnnotation;
		this.context = context;
	}

	public void registerAnnotationComponents() throws Exception {
		Debug.logVerbose("[JdonFramework] <------ register all annotation components(@component('xxx')/@Interceptor)  ------> ", module);
		try {
			AnnotationHolder annotationHolder = containerLoaderAnnotation.loadAnnotationHolder(context, containerWrapper);
			for (String name : annotationHolder.getComponentNames()) {
				Class classz = annotationHolder.getComponentClass(name);
				containerWrapper.register(name, classz);
				StartablecomponentsRegistry scr = (StartablecomponentsRegistry) containerWrapper.lookup(StartablecomponentsRegistry.NAME);
				scr.add(classz, name);
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework] registerAnnotationComponents error:" + e, module);
			throw new Exception(e);
		}
	}

	public void copyTargetMetaDefHolder() {
		TargetMetaDefHolder targetMetaDefHoader = (TargetMetaDefHolder) containerWrapper.lookup(ComponentKeys.SERVICE_METAHOLDER_NAME);
		AnnotationHolder annotationHolder = containerLoaderAnnotation.loadAnnotationHolder(context, containerWrapper);
		targetMetaDefHoader.add(annotationHolder.getTargetMetaDefHolder().loadMetaDefs());
	}

	public ContainerLoaderAnnotation getContainerLoaderAnnotation() {
		return containerLoaderAnnotation;
	}

}
