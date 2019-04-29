package ship.domain;


/**
 * @stereotype moment-interval
 */

public class CarrierMovement {
	private String id;

	private ship.domain.Location from;
	
	private ship.domain.Location to;
	
	
	

	public CarrierMovement(String id, Location from, Location to) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
	}
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public ship.domain.Location getFrom() {
		return from;
	}

	public void setFrom(ship.domain.Location from) {
		this.from = from;
	}

	public ship.domain.Location getTo() {
		return to;
	}

	public void setTo(ship.domain.Location to) {
		this.to = to;
	}
	
	
}
