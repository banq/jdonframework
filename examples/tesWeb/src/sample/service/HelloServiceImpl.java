package sample.service;

import sample.domain.User;
import sample.repository.PublisherRole;
import sample.repository.PublisherRoleImp;
import sample.repository.UserRepository;

import com.jdon.annotation.Service;
import com.jdon.annotation.Singleton;
import com.jdon.domain.dci.RoleAssigner;

@Singleton
@Service("helloService")
public class HelloServiceImpl implements HelloService {
	private UserRepository userRepository;
	private RoleAssigner roleAssigner;

	public HelloServiceImpl(UserRepository userRepository, RoleAssigner roleAssigner) {
		this.userRepository = userRepository;
		this.roleAssigner = roleAssigner;
	}

	public void save(User user) {
		PublisherRole publisher = (PublisherRole) roleAssigner.assign(user, new PublisherRoleImp());
		publisher.remember(user);

	}

	public String hello(String name) {
		User user = userRepository.findUser(name);
		System.out.print("call ok");
		return "Hello, " + user.getName();
	}

}
