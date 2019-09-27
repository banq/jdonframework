package ship.domain;


/**
 * @stereotype description
 */

public class DeliverySpec {
	private final Location origin;
	private final Location destination;

	public DeliverySpec(Location origin, Location destination) {
		this.origin = origin;
		this.destination = destination;
	}

	public Location getOrigin() {
		return origin;
	}
	public Location getDestination() {
		return destination;
	}

	

}
