package com.jdon.container.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.scannotation.ClasspathUrlFinder;
import org.scannotation.WarUrlFinder;

import com.jdon.controller.context.AppContextWrapper;
import com.jdon.controller.context.web.ServletContextWrapper;
import com.jdon.util.Debug;

public class AnnotationUtil {
	public final static String module = AnnotationUtil.class.getName();

	public static URL[] scanAnnotation(AppContextWrapper context) {
		List<URL> lists = new ArrayList();
		try {

			URL[] urls = ClasspathUrlFinder.findClassPaths(); // scan
			// java.class.path
			if (!(context instanceof ServletContextWrapper)) {
				return urls;
			}
			if (urls != null)
				lists.addAll(Arrays.asList(urls));

			ServletContextWrapper scw = (ServletContextWrapper) context;

			URL classURl = WarUrlFinder.findWebInfClassesPath(scw.getServletContext());
			if (classURl != null)
				lists.add(classURl);

			List libURls = findWebInfLibClasspaths(scw.getServletContext());
			if (libURls != null)
				lists.addAll(libURls);

			URL[] jdonurls = ClasspathUrlFinder.findResourceBases("com/jdon/container/annotation/AnnotationUtil.class");
			if (jdonurls != null)
				lists.addAll(Arrays.asList(jdonurls));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lists.toArray(new URL[lists.size()]);

	}

	public static List findWebInfLibClasspaths(ServletContext servletContext) {
		ArrayList<URL> list = new ArrayList<URL>();
		Set libJars = servletContext.getResourcePaths("/WEB-INF/lib");
		if (libJars == null)
			return null;
		for (Object jar : libJars) {
			try {
				list.add(servletContext.getResource((String) jar));
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		return list;
	}

	public static boolean methodParameterContainAnnotation(Method method, Class annotationClass) {
		boolean found = false;
		try {
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			for (Annotation[] annotations : parameterAnnotations) {
				for (Annotation annotation : annotations) {
					if (annotation.annotationType().isAssignableFrom(annotationClass)) {
						found = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			Debug.logError("getInputparameter" + e, module);
		}

		return found;
	}

}
