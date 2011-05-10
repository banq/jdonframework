package com.jdon.annotation.intercept;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * same as com.jdon.controller.service.Stateful
 * @author banQ
 *
 */

@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface Stateful {

}
