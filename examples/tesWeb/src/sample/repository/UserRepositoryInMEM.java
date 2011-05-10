package sample.repository;

import java.util.HashMap;

import sample.domain.User;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.Around;

@Component
@Introduce("modelCache")
public class UserRepositoryInMEM implements UserRepository {

	private final HashMap<String, User> memDB = new HashMap<String, User>();

	public UserRepositoryInMEM() {
		User user = new User();
		user.setName("Client");
		user.setUserId("999");
		memDB.put(user.getUserId(), user);
	}

	@Around
	public User findUser(String id) {
		return memDB.get(id);
	}

	public void save(User user) {
		memDB.put(user.getUserId(), user);

	}

}
