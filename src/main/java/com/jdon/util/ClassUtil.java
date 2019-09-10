/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain event copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jdon.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassUtil {

	public static Class[] getParentAllInterfaces(Class pojoClass) {
		Class[] interfaces = null;
		try {
			List interfacesL = new ArrayList();
			while (pojoClass != null) {
				for (int i = 0; i < pojoClass.getInterfaces().length; i++) {
					Class ifc = pojoClass.getInterfaces()[i];
					// not add jdk interface
					if (!ifc.getName().startsWith("java."))
						interfacesL.add(ifc);
				}
				pojoClass = pojoClass.getSuperclass();
			}
			if (interfacesL.size() == 0) {
				throw new Exception();
			}
			interfaces = (Class[]) interfacesL.toArray(new Class[interfacesL.size()]);
		} catch (Exception e) {
		}
		return interfaces;
	}

	public static Class[] getAllInterfaces(Class clazz) {
		if (clazz == null) {
			return new Class[0];
		}
		Set<Class> classList = new HashSet<Class>();
		while (clazz != null) {
			Class[] interfaces = clazz.getInterfaces();
			for (Class interf : interfaces) {
				if (!classList.contains(interf)) {
					classList.add(interf);
				}
				Class[] superInterfaces = getAllInterfaces(interf);
				for (Class superIntf : superInterfaces) {
					if (!classList.contains(superIntf)) {
						classList.add(superIntf);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
		return classList.toArray(new Class[classList.size()]);
	}

	public static Field[] getAllDecaredFields(Class clazz) {
		List<Field> fields = new ArrayList<Field>();
		// fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		Class[] superClasses = getAllSuperclasses(clazz);
		for (Class superClass : superClasses) {
			fields.addAll(Arrays.asList(superClass.getDeclaredFields()));
		}
		return fields.toArray(new Field[fields.size()]);
	}

	public static Class finddAnnotationForMethod(Class clazz, Class annotationClass) {
		Class[] superClasses = getAllSuperclasses(clazz);
		if (superClasses != null)
			for (Class superClass : superClasses) {
				if (superClass.isAnnotationPresent(annotationClass)) {
					return superClass;
				}
			}

		superClasses = getParentAllInterfaces(clazz);
		if (superClasses != null)
			for (Class superClass : superClasses) {
				if (superClass.isAnnotationPresent(annotationClass)) {
					return superClass;
				}
			}
		return null;

	}

	public static Method finddAnnotationForMethod(Method m, Class annotationClass) {
		try {
			Class[] superClasses = getAllSuperclasses(m.getDeclaringClass());
			if (superClasses != null)
				for (Class superClass : superClasses) {
					for (Method ms : superClass.getDeclaredMethods()) {
						if (ms.isAnnotationPresent(annotationClass)) {
							if (ms.getName() == m.getName()) {
								return ms;
							}
						}
					}
				}

			superClasses = getParentAllInterfaces(m.getDeclaringClass());
			if (superClasses != null)
				for (Class superClass : superClasses) {
					for (Method ms : superClass.getDeclaredMethods()) {
						if (ms.isAnnotationPresent(annotationClass)) {
							if (ms.getName() == m.getName()) {
								return ms;
							}

						}
					}
				}

		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Method[] getAllDecaredMethods(Class clazz) {
		List<Method> methods = new ArrayList<Method>();
		try {

			// fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

			Class[] superClasses = getAllSuperclasses(clazz);
			for (Class superClass : superClasses) {
				methods.addAll(Arrays.asList(superClass.getDeclaredMethods()));
			}

			// superClasses = getParentAllInterfaces(clazz);
			// for (Class superClass : superClasses) {
			// methods.addAll(Arrays.asList(superClass.getDeclaredMethods()));
			// }

		} catch (Exception e) {
		}
		return methods.toArray(new Method[methods.size()]);
	}

	public static Class[] getAllSuperclasses(Class cls) {
		if (cls == null) {
			return new Class[0];
		}
		List<Class> classList = new ArrayList<Class>();
		Class superClass = cls;
		while (superClass != null && !Object.class.equals(superClass) && !Class.class.equals(superClass)) {
			classList.add(superClass);
			superClass = superClass.getSuperclass();
		}
		return classList.toArray(new Class[classList.size()]);
	}

	public static Field getDecaredField(Class clazz, String name) throws NoSuchFieldException {
		Field field = null;
		Class[] superClasses = getAllSuperclasses(clazz);
		for (Class superClass : superClasses) {
			try {
				field = superClass.getDeclaredField(name);
				break;
			} catch (NoSuchFieldException e) {
				// ignore
			}
		}
		if (field == null) {
			throw new NoSuchFieldException("No such declared field " + name + " in " + clazz);
		}
		return field;
	}

}
