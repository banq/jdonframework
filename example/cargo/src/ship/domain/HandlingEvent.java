package ship.domain;

import java.util.Comparator;
import java.util.Date;

/**
 * @stereotype moment-interval
 */

public class HandlingEvent {

	public static final Comparator<HandlingEvent> BY_COMPLETION_TIME_COMPARATOR = new Comparator<HandlingEvent>() {
		public int compare(final HandlingEvent o1, final HandlingEvent o2) {
			return o1.getCompletionTime().compareTo(o2.getCompletionTime());
		}
	};

	private final ship.domain.CarrierMovement lnkCarriermovent;
	private final ship.domain.Cargo lnkCargo;

	private final Location location;
	private final Date completionTime;
	private final Date registrationTime;

	private final Type type;

	public enum Type {
		LOAD(true), UNLOAD(true), RECEIVE(false), CLAIM(false), CUSTOMS(false);

		private boolean carrierMovementRequired;

		/**
		 * Private enum constructor.
		 * 
		 * @param carrierMovementRequired
		 *            whether or not a carrier movement is associated with this
		 *            event type
		 */
		private Type(final boolean carrierMovementRequired) {
			this.carrierMovementRequired = carrierMovementRequired;
		}

		/**
		 * @return True if a carrier movement association is required for this
		 *         event type.
		 */
		public boolean requiresCarrierMovement() {
			return carrierMovementRequired;
		}

		/**
		 * @return True if a carrier movement association is prohibited for this
		 *         event type.
		 */
		public boolean prohibitsCarrierMovement() {
			return !requiresCarrierMovement();
		}
	}

	public HandlingEvent(CarrierMovement lnkCarriermovent, Cargo lnkCargo, Location location, Date completionTime, Date registrationTime, Type type) {
		super();
		this.lnkCarriermovent = lnkCarriermovent;
		this.lnkCargo = lnkCargo;
		this.location = location;
		this.completionTime = completionTime;
		this.registrationTime = registrationTime;
		this.type = type;
	}

	public ship.domain.CarrierMovement getLnkCarriermovent() {
		return lnkCarriermovent;
	}

	public ship.domain.Cargo getLnkCargo() {
		return lnkCargo;
	}

	public Location getLocation() {
		return location;
	}

	public Date getCompletionTime() {
		return completionTime;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public Type getType() {
		return type;
	}

}
