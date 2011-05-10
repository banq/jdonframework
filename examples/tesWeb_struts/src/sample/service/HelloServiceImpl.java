package sample.service;

import sample.domain.User;
import sample.repository.UserRepository;

import com.jdon.annotation.Service;
import com.jdon.annotation.intercept.Poolable;
import com.jdon.controller.events.EventModel;
import com.jdon.controller.model.PageIterator;

@Poolable
@Service("helloService")
public class HelloServiceImpl implements HelloService {
	private UserRepository userRepository;

	public HelloServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public void create(EventModel em) {
		User user = (User) em.getModelIF();
		userRepository.save(user);
	}

	public void update(EventModel em) {
		User user = (User) em.getModelIF();
		userRepository.save(user);
	}

	public void delete(EventModel em) {
		User user = (User) em.getModelIF();
		userRepository.delete(user.getUserId());
	}

	public User getUser(String id) {
		return userRepository.findUser(id);
	}

	public User init(EventModel em) {
		User user = new User();
		user.setName("Client");
		user.setUserId("999");
		return user;
	}

	public String hello(String name) {
		User user = userRepository.findUser(name);
		System.out.print("call ok");
		return "Hello, " + user.getName();
	}
	
	public PageIterator getAllUsers(int start, int count){
		return userRepository.getUsers(start, count);
	}

}
