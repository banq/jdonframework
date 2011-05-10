package sample.domain;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;
import com.jdon.async.message.EventMessage;

@Model
public class User {

	private String userId;
	private String name;
	private int age;

	@Inject
	private MyDomainEvents myDomainEvents;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MyDomainEvents getMyDomainEvents() {
		return myDomainEvents;
	}

	public void setMyDomainEvents(MyDomainEvents myDomainEvents) {
		this.myDomainEvents = myDomainEvents;
	}

	public int getAge() {
		if (age == 0) {
			EventMessage message = myDomainEvents.sendMessage(this);
			int ageResult = (Integer) message.getEventResult();
			System.out.println("get result " + ageResult);
		}
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
