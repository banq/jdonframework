package sample.service;

import sample.domain.IntelligentRobot;
import sample.domain.IntelligentRole;
import sample.domain.Robot;
import sample.repository.PublisherRole;
import sample.repository.PublisherRoleImp;
import sample.repository.RobotRepository;

import com.jdon.annotation.Service;
import com.jdon.annotation.Singleton;
import com.jdon.domain.dci.RoleAssigner;

@Singleton
@Service("context")
public class TestContext implements Context {
	private RobotRepository robotRepository;
	private RoleAssigner roleAssigner;

	public TestContext(RobotRepository robotRepository, RoleAssigner roleAssigner) {
		this.robotRepository = robotRepository;
		this.roleAssigner = roleAssigner;
	}

	public void save(Robot robot) {
		PublisherRole publisher = (PublisherRole) roleAssigner.assign(robot, new PublisherRoleImp());
		publisher.remember(robot);

	}

	public String hello(String id) {
		Robot robot = robotRepository.find(id);
		IntelligentRole intelligentRobot = (IntelligentRole) roleAssigner.assign(robot, new IntelligentRobot());
		return "Hello, " + intelligentRobot.hear();
	}

	public String touch(String id) {
		Robot robot = robotRepository.find(id);
		IntelligentRole intelligentRobot = (IntelligentRole) roleAssigner.assign(robot, new IntelligentRobot());
		return "Hello, " + intelligentRobot.feel();
	}

}
