package com.jdon.sample.test.component;

import com.jdon.annotation.Service;

@Service("b")
public class B implements BInterface {
	AInterface a;
	DInterface d;

	public B(AInterface a, DInterface d) {
		this.a = a;
		this.d = d;
	}

	public int bMethod(int i) {
		System.out.println("call A.myMethod1 ");
		i++;
		i = (Integer) a.myMethod(i, null);
		System.out.print("\n\n call A.myMethod1 result=" + i + "  \n\n");

		i++;
		System.out.println("call A.myMethod2 ");
		i = (Integer) a.myMethod2(i);
		System.out.print("\n\n call A.myMethod2 result=" + i + "\n\n");

		i++;
		return (Integer) d.myMethod3(i);
	}
}
