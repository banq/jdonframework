/*
 * Exadel Flamingo
 * Copyright (C) 2008 Exadel, Inc.
 * 
 * This file is part of Exadel Flamingo.
 * 
 * Exadel Flamingo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation
 * 
 * LazyUtil.java
 * 
 * Last modified by: $Author: klebed $
 * $Revision: 1539 $   $Date: 2008-05-22 10:19:29 +0300 (Чт, 22 май 2008) $
 */
package com.jdon.bussinessproxy.remote.hessian.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Helper class for prevent 
 * lazy initialization exception 
 * 
 * @author klebed
 */
public class LazyUtil {
	/*
	 * class reference
	 */
    private static final String HIBERNATE_CLASS = "org.hibernate.Hibernate";
	
	/*
	 * method reference 
	 */
    private static final String IS_INITIALIZED = "isInitialized";
	
    /**
     * Return Hibernate class instance
	 * @return Hibernate class instance
	 * if it exist 
	 */
    private static Class < ? > getHibernateClass() {
    	Class < ? > cl = null;
    	
    	try {
            cl = Class.forName(HIBERNATE_CLASS);
    	} catch (ClassNotFoundException e) {
            // in this case jar which contain 
    		// Hibernate class not found
        }
    	return cl;
    }
	
	/**
	 * Return "isInitialized" Hibernate static method
	 * @param cl - "Hibernate" class instance
	 * @return "isInitialized" Hibernate static method
	 */
    private static Method getInitializeMethod(Class< ? > cl) {
        Method method = null;
        
        try {
            method = cl.getDeclaredMethod(IS_INITIALIZED, new Class[]{Object.class});
        } catch (NoSuchMethodException e) {
			//in this case 'isInitialized' method can't be null
        } 
        return method;		
    }
	
	/**
	 * Check is current property was initialized
	 * @param method - hibernate static method, which check
	 * is initilized property
	 * @param obj - object which need for lazy check
	 * @return boolean value
	 */
    private static boolean checkInitialize(Method method, Object obj) {
        boolean isInitialized = true;
        
        try {
            isInitialized = (Boolean) method.invoke(null, new Object[] {obj});
        } catch (IllegalArgumentException e) {
		   // do nothing
        } catch (IllegalAccessException e) {
		   // do nothing
        } catch (InvocationTargetException e) {
		   // do nothing
        }
        return isInitialized;
    }
    
    /**
     * Check is current object was initialized
     * @param object - object, which need check
     * @return boolean value
     */
    public static boolean isPropertyInitialized(Object object) {
    	Class < ? > cl =  getHibernateClass();
    	
        if (cl == null) {
            return true;
        }
        
        Method method = getInitializeMethod(cl);
        return checkInitialize(method, object);
    } 
}
