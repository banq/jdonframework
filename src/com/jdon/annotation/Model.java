package com.jdon.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Domain Model should normal live in memory not in database.
 * so cache in memory is very important for domain model life cycle.
 * 
 * 
 * @see com.jdon.controller.model.ModelIF
 * @author banQ
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Model  {

	boolean isCacheable() default true;
	
	boolean isModified()  default false;
}
