package ship.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @stereotype thing
 */

public class DeliveryHistory {

	private Set<HandlingEvent> events;

	public static final DeliveryHistory EMPTY_DELIVERY_HISTORY = new DeliveryHistory(Collections.EMPTY_SET);

	public DeliveryHistory(final Collection<HandlingEvent> events) {
		this.events = new HashSet<HandlingEvent>(events);
	}

	public void addHandlingEvent(HandlingEvent event) {
		events.add(event);
	}

	/**
	 * @return An <b>unmodifiable</b> list of handling events, ordered by the
	 *         time the events occured.
	 */
	public List<HandlingEvent> eventsOrderedByCompletionTime() {
		final List<HandlingEvent> eventList = new ArrayList<HandlingEvent>(events);
		Collections.sort(eventList, HandlingEvent.BY_COMPLETION_TIME_COMPARATOR);
		return Collections.unmodifiableList(eventList);
	}

	/**
	 * @return The last event of the delivery history, or null is history is
	 *         empty.
	 */
	public HandlingEvent lastEvent() {
		if (events.isEmpty()) {
			return null;
		} else {
			final List<HandlingEvent> orderedEvents = eventsOrderedByCompletionTime();
			return orderedEvents.get(orderedEvents.size() - 1);
		}
	}

	public StatusCode status() {
		if (lastEvent() == null)
			return StatusCode.NOT_RECEIVED;

		final HandlingEvent.Type type = lastEvent().getType();

		switch (type) {
		case LOAD:
			return StatusCode.ONBOARD_CARRIER;

		case UNLOAD:
		case RECEIVE:
		case CUSTOMS:
			return StatusCode.IN_PORT;

		case CLAIM:
			return StatusCode.CLAIMED;

		default:
			return null;
		}
	}

	public Location currentLocation() {
		if (status().equals(StatusCode.IN_PORT)) {
			return lastEvent().getLocation();
		} else {
			return null;
		}
	}

	public CarrierMovement currentCarrierMovement() {
		if (status().equals(StatusCode.ONBOARD_CARRIER)) {
			return lastEvent().getLnkCarriermovent();
		} else {
			return null;
		}
	}
}
