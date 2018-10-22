package com.jdon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 *   “@Component”
 * is equals to:
 * {@code
 *   <component name="abc" class="com.sample.Abc" />”
 *}
 *   “@Component's name will be the class's getClass.getName();
 * Component not open for the client, so default no need name,
 * but if you call the component from the client, you can use
 * the @Component's name(class's getClass.getName()).
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Component {	
	String value() default "";
}
