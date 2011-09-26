package sample.service;

import sample.domain.User;

public interface HelloService {

	String hello(String name);

	void save(User user);
}
