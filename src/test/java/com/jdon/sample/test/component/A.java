package com.jdon.sample.test.component;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.After;
import com.jdon.annotation.pointcut.Before;
import com.jdon.annotation.pointcut.method.Input;
import com.jdon.annotation.pointcut.method.Returning;

@Component("event")
@Introduce("c")
public class A implements AInterface {

	@Before("testOne")
	public Object myMethod(@Input() Object inVal, @Returning() Object returnVal) {
		System.out.println("this is A.myMethod is active!!!! ");
		int i = (Integer) inVal + 1;
		return i;
	}

	@After("testWo")
	public Object myMethod2(Object inVal) {
		System.out.println("this is A.myMethod2 is active!!!! ");
		int i = (Integer) inVal + 1;
		return i;
	}

}
