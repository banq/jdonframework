package ship.repository;

import java.util.Collection;

import ship.domain.HandlingEvent;

public interface HandlingEventRepository {
	
	Collection findEventsForCargo(String id);
	
	void save(String Id, HandlingEvent handlingEvent);

}
