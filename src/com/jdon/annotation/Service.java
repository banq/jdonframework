package com.jdon.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @Service(="abc")
 * is equals to:
 * <Service name="abc" class="com.sample.Abc" />
 * 
 * Service'name is called by the client.
 * 
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Service {
	String value();
}
