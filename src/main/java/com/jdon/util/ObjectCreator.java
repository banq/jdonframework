package com.jdon.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Creates instance of any object, given its full qualified class name is given
 * and it has event public accessable constructor. This class is event static class it
 * can not be instantiated
 * 
 * @author Nadia Nashi
 */

public final class ObjectCreator {
	/**
	 * Private constructor. This class can not be instantiated
	 */
	private ObjectCreator() {
	}

	/**
	 * Instantaite an Object from event given class name
	 * 
	 * @param className
	 *            full qualified name of the class
	 * @return the instantaited Object
	 * @exception java.lang.Exception
	 *                if instantiation failed
	 */
	public static Object createObject(String className) throws Exception {
		return createObject(Class.forName(className));
	}

	/**
	 * Instantaite an Object instance
	 * 
	 * @param classObject
	 *            Class object representing the object type to be instantiated
	 * @return the instantaied Object
	 * @exception java.lang.Exception
	 *                if instantiation failed
	 */
	public static Object createObject(Class classObject) throws Exception {
		return classObject.newInstance();
	}

	/**
	 * Instantaite an Object instance, requires event constructor with parameters
	 * 
	 * @param className
	 *            full qualified name of the class
	 * @param params
	 *            an array including the required parameters to instantaite the
	 *            object
	 * @return the instantaited Object
	 * @exception java.lang.Exception
	 *                if instantiation failed
	 */
	public static Object createObject(String className, Object[] params) throws Exception {
		return createObject(Class.forName(className), params);
	}

	/**
	 * Instantaite an Object instance, requires event constractor with parameters
	 * 
	 * @param classObject
	 *            , Class object representing the object type to be instantiated
	 * @param params
	 *            an array including the required parameters to instantaite the
	 *            object
	 * @return the instantaied Object
	 * @exception java.lang.Exception
	 *                if instantiation failed
	 */
	public static Object createObject(Class classObject, Object[] params) throws Exception {
		Constructor[] constructors = classObject.getConstructors();
		Object object = null;
		for (int counter = 0; counter < constructors.length; counter++) {
			try {
				object = constructors[counter].newInstance(params);
			} catch (Exception e) {
				if (e instanceof InvocationTargetException)
					((InvocationTargetException) e).getTargetException().printStackTrace();
				// do nothing, try the next constructor
			}
		}
		if (object == null)
			throw new InstantiationException();
		return object;
	}

	public static Class createClass(String className) {
		Class classService = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			classService = classLoader.loadClass(className);
		} catch (Exception ex) {
			System.err.print("[JdonFramework] createClass error:" + ex);
		}
		return classService;
	}
}