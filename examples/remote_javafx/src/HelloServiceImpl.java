package sample;

import com.jdon.annotation.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService {

	@Override
	public String hello(String name) {
		System.out.print("call ok");
        return "Hello, " + name;
	}

}
