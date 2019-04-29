package ship.domain;

import ship.domain.events.DomainEventsRole;

import com.jdon.annotation.Model;
import com.jdon.annotation.model.Inject;

/**
 * @stereotype thing
 */
@Model
public class Cargo {

	private String id;

	/**
	 * @link aggregation
	 */
	private ship.domain.DeliveryHistory lnkDeliveryHistory;
	/**
	 * @link aggregation
	 */
	private ship.domain.DeliverySpec lnkDeliverySpec;

	private Customer customer;

	@Inject
	public DomainEventsRole domainEventsRole;

	private CargoState cargoState;

	public Cargo(String trackingId, DeliverySpec deliverySpec) {
		this.id = trackingId;
		this.lnkDeliverySpec = deliverySpec;
		this.cargoState = new CargoState(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void changeDestination(final Location newDestination) {
		lnkDeliverySpec.setDestination(newDestination);
	}

	// ���ٻ���λ��
	public Location lastKnownLocation() {
		final HandlingEvent lastEvent = this.lnkDeliveryHistory.lastEvent();
		if (lastEvent != null) {
			return lastEvent.getLocation();
		} else {
			return null;
		}
	}

	// ������������ִ�Ŀ�ĵص�ʱ
	public boolean hasArrived() {
		if (lnkDeliverySpec.getDestination().equals(lastKnownLocation())) {
			return true;
		} else
			return false;
	}

	// ���ٹ˿ͻ���Ĺؼ�װж�¼�
	public boolean isUnloadedAtDestination() {
		for (HandlingEvent event : this.lnkDeliveryHistory.eventsOrderedByCompletionTime()) {
			if (HandlingEvent.Type.UNLOAD.equals(event.getType()) && hasArrived()) {
				return true;
			}
		}
		return false;
	}

	public void addHandlingEvent(HandlingEvent event) {
		this.lnkDeliveryHistory.addHandlingEvent(event);
	}

	public void changeState() {
		this.cargoState.changeState();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setLnkDeliveryHistory(DeliveryHistory lnkDeliveryHistory) {
		this.lnkDeliveryHistory = lnkDeliveryHistory;
	}

}
