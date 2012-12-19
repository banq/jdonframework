package sample.repository;

import sample.domain.Robot;

public interface RobotRepository {
	Robot find(String id);

	void save(Robot robot);

}
