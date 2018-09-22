package com.jdon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * the @Service(="abc")
 * is equals to:
 * {@code
 *   <Service name="abc" class="com.sample.Abc" />
 * }
 * Service'name is called by the client.
 * 
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Service {
	String value();
}
