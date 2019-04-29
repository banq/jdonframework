package ship.repository;

import java.util.Collection;

import ship.domain.Cargo;
import ship.domain.DeliveryHistory;
import ship.domain.DeliverySpec;
import ship.domain.HandlingEvent;
import ship.domain.Location;



public class CargoTestHelper {
	
	public static Cargo createCargoWithDeliveryHistory(
		    String trackingId, Location origin, Location destination,
		    Collection<HandlingEvent> events) {
		
		    DeliverySpec deliverySpec = new DeliverySpec();
		    deliverySpec.setOrigin(origin);
		    deliverySpec.setDestination(destination);

		    final Cargo cargo = new Cargo(trackingId, deliverySpec);
		    setDeliveryHistory(cargo, events);

		    return cargo;
		  }

		  public static void setDeliveryHistory(Cargo cargo, Collection<HandlingEvent> events) {
		    cargo.setLnkDeliveryHistory(new DeliveryHistory(events));
		  }

}
