package sample.service;

import sample.domain.User;
import sample.repository.UserRepository;

import com.jdon.annotation.Service;
import com.jdon.annotation.Singleton;

@Singleton
@Service("helloService")
public class HelloServiceImpl implements HelloService {
	private UserRepository userRepository;
	private User singletonuser;//test @Singleton

	public HelloServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String hello(String name) {
		User user = userRepository.findUser(name);
		System.out.print("call ok");
		return "Hello, " + user.getName();
	}

	public String helloSingleton(String name) {
		if (singletonuser == null) {
			System.out.print("only init one times");
			singletonuser = userRepository.findUser(name);
		}
		System.out.print("call ok");
		return "Hello, " + singletonuser.getName();
	}

}
