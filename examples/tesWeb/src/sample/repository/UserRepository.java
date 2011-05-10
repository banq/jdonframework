package sample.repository;

import sample.domain.User;

public interface UserRepository {
	User findUser(String id);

	void save(User user);

}
