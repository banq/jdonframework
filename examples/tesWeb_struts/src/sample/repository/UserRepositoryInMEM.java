package sample.repository;

import java.util.HashMap;
import java.util.Set;

import sample.domain.User;

import com.jdon.annotation.Component;
import com.jdon.annotation.Introduce;
import com.jdon.annotation.pointcut.Around;
import com.jdon.controller.model.PageIterator;

@Component
@Introduce("modelCache")
public class UserRepositoryInMEM implements UserRepository {

	private final HashMap<String, User> memDB = new HashMap<String, User>();

	public UserRepositoryInMEM() {

	}

	@Around
	public User findUser(String id) {
		return memDB.get(id);
	}

	public void save(User user) {
		memDB.put(user.getUserId(), user);

	}

	public void delete(String id) {
		memDB.remove(id);

	}

	public PageIterator getUsers(int start, int count) {
		PageIterator pageIterator = null;
		try {
			Set list = memDB.keySet();
			int allCount = memDB.size();
			int currentCount = start + list.size();
			pageIterator = new PageIterator(allCount, list.toArray(), start, (currentCount < allCount) ? true : false);
		} catch (Exception daoe) {

		}
		return pageIterator;

	}

}
