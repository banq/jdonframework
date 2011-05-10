package DomainEvents;

/**
 * @url element://model:project::TestModel/design:node:::-y8nsowg3jhksde9xvdkv:-yrgd8eg3jhkhhg456po3
 */

public class UserCountValueObject {
	private UserModel user;
	private int count = -1;
	private DomainMessage ageAsyncResult;

	public UserCountValueObject(UserModel user) {
		this.user = user;
	}

	public void preloadData() {
		if (ageAsyncResult == null)
			ageAsyncResult = user.getUserDomainEvents().computeCount(user);
	}

	public int getCount() {
		if (count == -1) { // lazy load
			preloadData();
			count = (Integer) ageAsyncResult.getEventResult();
		}
		return count;
	}

	public int getCountByAsync() {
		if (count == -1) { // lazy load
			if (ageAsyncResult == null)
				ageAsyncResult = user.getUserDomainEvents().computeCount(user);
			else
				count = (Integer) ageAsyncResult.getEventResult();
		}
		return count;
	}

	public void addCount(int count) {
		this.count = getCount() + count;
	}

}
