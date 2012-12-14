package sample.service;

import sample.domain.Robot;

public interface Context {

	String hello(String name);

	String touch(String name);

	void save(Robot robot);
}
