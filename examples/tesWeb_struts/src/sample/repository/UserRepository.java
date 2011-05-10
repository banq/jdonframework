package sample.repository;

import com.jdon.controller.model.PageIterator;

import sample.domain.User;

public interface UserRepository {
	User findUser(String id);

	void save(User user);
	
	void delete(String id);
	
	PageIterator getUsers(int start, int count);

}
