package com.jdon.annotation.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Domain Model should normal live in memory not in database. so cache in memory
 * is very important for domain model life cycle.
 * 
 * 
 * @see com.jdon.controller.model.ModelIF
 * @author banQ
 * 
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface Inject {
	String value() default "";
}
