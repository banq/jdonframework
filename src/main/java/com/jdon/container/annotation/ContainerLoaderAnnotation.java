package com.jdon.container.annotation;

import com.jdon.container.ContainerWrapper;
import com.jdon.container.annotation.type.AnnotationScaner;
import com.jdon.container.annotation.type.ComponentLoader;
import com.jdon.container.annotation.type.ConsumerLoader;
import com.jdon.container.annotation.type.InroduceLoader;
import com.jdon.container.annotation.type.InterceptorLoader;
import com.jdon.container.annotation.type.ModelConsumerLoader;
import com.jdon.container.annotation.type.ServiceLoader;
import com.jdon.container.interceptor.IntroduceInfoHolder;
import com.jdon.container.pico.ConfigInfo;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;

/**
 * load all Annotation components
 * 
 * add all annotated components into AnnotationHolder then ContainerBuilder load
 * all components from AnnotationHolder and register into picocontainer.
 * 
 * @author banq
 * 
 */
public class ContainerLoaderAnnotation {
	public final static String module = ContainerLoaderAnnotation.class.getName();

	private AnnotationScaner annotationScaner;
	private ConfigInfo configInfo;
	private AnnotationHolder annotationHolder; // lazy

	public ContainerLoaderAnnotation() {
		configInfo = new ConfigInfo();
		annotationScaner = new AnnotationScaner();
	}

	public void startScan(final AppContextWrapper context) {
		annotationScaner.startScan(context);

	}

	public AnnotationHolder loadAnnotationHolder(AppContextWrapper context, ContainerWrapper containerWrapper) {
		if (annotationHolder != null)
			return annotationHolder;

		Debug.logVerbose("[JdonFramework] load all Annotation components ", module);
		annotationHolder = new AnnotationHolder();

		ConsumerLoader consumerLoader = new ConsumerLoader(annotationScaner);
		consumerLoader.loadAnnotationConsumers(annotationHolder, context, containerWrapper);

		ModelConsumerLoader modelConsumerLoader = new ModelConsumerLoader(annotationScaner);
		modelConsumerLoader.loadAnnotationModels(annotationHolder, context, containerWrapper);

		ServiceLoader serviceLoader = new ServiceLoader(annotationScaner, consumerLoader);
		serviceLoader.loadAnnotationServices(annotationHolder, context, containerWrapper);

		ComponentLoader componentLoader = new ComponentLoader(annotationScaner, consumerLoader);
		componentLoader.loadAnnotationComponents(annotationHolder, context, containerWrapper);

		InroduceLoader inroduceLoader = new InroduceLoader(annotationScaner, this.configInfo.getIntroduceInfoHolder());
		inroduceLoader.loadAnnotationIntroduceInfos(annotationHolder, context, containerWrapper);

		InterceptorLoader interceptorLoader = new InterceptorLoader(annotationScaner, configInfo.getIntroduceInfoHolder());
		interceptorLoader.loadAnnotationInterceptors(annotationHolder, context);

		containerWrapper.register(AnnotationHolder.NAME, annotationHolder);
		containerWrapper.register(IntroduceInfoHolder.NAME, configInfo.getIntroduceInfoHolder());
		return annotationHolder;
	}

	public ConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(ConfigInfo configInfo) {
		this.configInfo = configInfo;
	}

}
