package com.jdon.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Introduce {
	String[] value();
}
