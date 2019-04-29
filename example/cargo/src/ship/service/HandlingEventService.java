package ship.service;

import java.util.Date;

import ship.domain.HandlingEvent;

public interface HandlingEventService {

	public void register(Date completionTime, String trackingId, String carrierMovementId, String unlocode, HandlingEvent.Type type) throws Exception;

	public void unLoad(final String carrierMovementId, final String unlocode, final String trackingId) throws Exception;
}
