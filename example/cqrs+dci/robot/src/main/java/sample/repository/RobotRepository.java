package sample.repository;

import java.util.HashMap;

import sample.domain.Robot;

public interface RobotRepository {
	Robot find(String id);

	void save(Robot robot);

	// for test
	public HashMap<String, Robot> getMemDB();
}
