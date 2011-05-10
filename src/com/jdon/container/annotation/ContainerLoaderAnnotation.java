package com.jdon.container.annotation;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.jdon.annotation.Component;
import com.jdon.annotation.Interceptor;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.Service;
import com.jdon.annotation.Singleton;
import com.jdon.bussinessproxy.meta.POJOTargetMetaDef;
import com.jdon.bussinessproxy.meta.SingletonPOJOTargetMetaDef;
import com.jdon.container.ContainerWrapper;
import com.jdon.container.access.TargetMetaDefHolder;
import com.jdon.container.finder.ComponentKeys;
import com.jdon.container.interceptor.IntroduceInfoHolder;
import com.jdon.container.pico.ConfigInfo;
import com.jdon.controller.context.AppContextWrapper;
import com.jdon.util.Debug;
import com.jdon.util.UtilValidate;
import com.jdon.util.scanAnnotation.ScanAnnotationDB;

/**
 * load all Annotation components
 * 
 * @author banq
 * 
 */
public class ContainerLoaderAnnotation {
	public final static String module = ContainerLoaderAnnotation.class.getName();

	private FutureTask<ScanAnnotationDB> ft;
	private ScanAnnotationDB db;
	private ConfigInfo configInfo;
	private AnnotationHolder annotationHolder;

	public ContainerLoaderAnnotation() {
		configInfo = new ConfigInfo();
	}

	public Map<String, Set<String>> scanAnnotation(AppContextWrapper context) {
		if (db != null)
			return db.getAnnotationIndex();

		try {
			db = ft.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return db.getAnnotationIndex();
	}

	public void startScan(final AppContextWrapper context) {
		this.ft = new FutureTask(new Callable<ScanAnnotationDB>() {
			public ScanAnnotationDB call() throws Exception {
				ScanAnnotationDB db = new ScanAnnotationDB();
				URL[] urls = AnnotationUtil.scanAnnotation(context);
				try {
					db.scanArchives(urls);
				} catch (Exception e) {
					Debug.logError("[JdonFramework] scanAnnotation error:" + e, module);
				}
				return db;
			}
		});
		ft.run();
	}

	public AnnotationHolder loadAnnotationHolder(AppContextWrapper context, ContainerWrapper containerWrapper) {
		if (annotationHolder != null)
			return annotationHolder;
		Debug.logVerbose("[JdonFramework] load all Annotation components ", module);
		annotationHolder = new AnnotationHolder();
		loadAnnotationServices(annotationHolder, context);
		loadAnnotationComponents(annotationHolder, context);
		loadAnnotationIntroduceInfos(annotationHolder, context, containerWrapper);
		loadAnnotationInterceptors(annotationHolder, context);
		registerAnnotationInfo(containerWrapper);
		return annotationHolder;
	}

	public void registerAnnotationInfo(ContainerWrapper containerWrapper) {
		try {
			containerWrapper.register(AnnotationHolder.NAME, annotationHolder);
			containerWrapper.register(IntroduceInfoHolder.NAME, configInfo.getIntroduceInfoHolder());
		} catch (Exception e) {
			Debug.logError("registerAnnotationInfo error=" + e, module);
		}
	}

	public void loadAnnotationServices(AnnotationHolder annotationHolder, AppContextWrapper context) {
		Set<String> classes = scanAnnotation(context).get(Service.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation components size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationServiceClass((String) className, annotationHolder);
		}
	}

	public void loadAnnotationComponents(AnnotationHolder annotationHolder, AppContextWrapper context) {
		Set<String> classes = scanAnnotation(context).get(Component.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation components size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationComponentClass((String) className, annotationHolder);
		}
	}

	public void loadAnnotationIntroduceInfos(AnnotationHolder annotationHolder, AppContextWrapper context, ContainerWrapper containerWrapper) {
		Set<String> classes = scanAnnotation(context).get(Introduce.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation IntroduceInfo size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationIntroduceInfoClass((String) className, annotationHolder, containerWrapper);
		}
	}

	public void loadAnnotationInterceptors(AnnotationHolder annotationHolder, AppContextWrapper context) {
		Set<String> classes = scanAnnotation(context).get(Interceptor.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[JdonFramework] found Annotation Interceptor size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationInterceptor((String) className, annotationHolder);
		}
	}

	public void createAnnotationServiceClass(String className, AnnotationHolder annotationHolder) {
		try {
			Class cclass = createClass(className);
			Service serv = (Service) cclass.getAnnotation(Service.class);
			Debug.logVerbose("[JdonFramework] load Annotation service name:" + serv.value() + " class:" + className, module);

			String name = UtilValidate.isEmpty(serv.value()) ? cclass.getName() : serv.value();
			annotationHolder.addComponent(name, cclass);
			createPOJOTargetMetaDef(name, className, annotationHolder.getTargetMetaDefHolder(), cclass);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationserviceClass error:" + e, module);
		}
	}

	public void createPOJOTargetMetaDef(String name, String className, TargetMetaDefHolder targetMetaDefHolder, Class cclass) {
		POJOTargetMetaDef pojoMetaDef = null;
		if (cclass.isAnnotationPresent(Singleton.class)) {
			pojoMetaDef = new SingletonPOJOTargetMetaDef(name, className);
		} else {
			pojoMetaDef = new POJOTargetMetaDef(name, className);
		}
		targetMetaDefHolder.add(name, pojoMetaDef);
	}

	public void createAnnotationComponentClass(String className, AnnotationHolder annotationHolder) {
		try {
			Class cclass = createClass(className);
			Component cp = (Component) cclass.getAnnotation(Component.class);
			Debug.logVerbose("[JdonFramework] load Annotation component name:" + cclass.getName() + " class:" + className, module);

			String name = UtilValidate.isEmpty(cp.value()) ? cclass.getName() : cp.value();
			annotationHolder.addComponent(name, cclass);
			POJOTargetMetaDef pojoMetaDef = new POJOTargetMetaDef(name, className);
			annotationHolder.getTargetMetaDefHolder().add(name, pojoMetaDef);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationComponentClass error:" + e + className, module);

		}
	}

	public void createAnnotationIntroduceInfoClass(String className, AnnotationHolder annotationHolder, ContainerWrapper containerWrapper) {
		try {
			Class targetclass = createClass(className);
			Introduce cp = (Introduce) targetclass.getAnnotation(Introduce.class);

			String[] adviceName = cp.value();
			configInfo.getIntroduceInfoHolder().addIntroduceInfo(adviceName, targetclass);
			String targetName = annotationHolder.getComponentName(targetclass);
			if (targetName == null) {// iterate xml component
				TargetMetaDefHolder targetMetaDefHolder = (TargetMetaDefHolder) containerWrapper.lookup(ComponentKeys.SERVICE_METAHOLDER_NAME);
				targetName = targetMetaDefHolder.lookupForName(targetclass.getName());
			}
			configInfo.getIntroduceInfoHolder().addTargetClassNames(targetclass, targetName);
			Debug.logVerbose("[JdonFramework] load Annotation IntroduceInfo name:" + adviceName + " target class:" + className, module);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationIntroduceInfoClass error:" + e + className, module);
		}
	}

	public void createAnnotationInterceptor(String className, AnnotationHolder annotationHolder) {
		try {
			Class cclass = createClass(className);
			Interceptor inter = (Interceptor) cclass.getAnnotation(Interceptor.class);

			String name = cclass.getName();
			if (!UtilValidate.isEmpty(inter.value())) {
				name = inter.value();
			} else if (!UtilValidate.isEmpty(inter.name())) {
				name = inter.name();
			}

			annotationHolder.addComponent(name, cclass);
			annotationHolder.getInterceptors().put(name, cclass);
			if (!UtilValidate.isEmpty(inter.pointcut())) {
				String[] targets = inter.pointcut().split(",");
				for (int i = 0; i < targets.length; i++) {
					Class targetClass = annotationHolder.getComponentClass(targets[i]);
					if (targetClass != null)
						configInfo.getIntroduceInfoHolder().addTargetClassNames(targetClass, targets[i]);
				}
			}
			POJOTargetMetaDef pojoMetaDef = new POJOTargetMetaDef(name, className);
			annotationHolder.getTargetMetaDefHolder().add(name, pojoMetaDef);
			Debug.logVerbose("[JdonFramework] load Annotation Interceptor name:" + name + " target class:" + className, module);
		} catch (Exception e) {
			Debug.logError("[JdonFramework] createAnnotationInterceptorClass error:" + e + className, module);
		}
	}

	private Class createClass(String className) {
		Class classService = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			classService = classLoader.loadClass(className);
		} catch (Exception ex) {
			Debug.logError("[JdonFramework] createClass error:" + ex, module);
		}
		return classService;
	}

	public ConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(ConfigInfo configInfo) {
		this.configInfo = configInfo;
	}

}
