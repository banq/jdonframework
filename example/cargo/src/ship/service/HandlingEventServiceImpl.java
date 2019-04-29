package ship.service;

import java.util.Date;

import ship.context.UnLoadContext;
import ship.domain.Cargo;
import ship.domain.CarrierMovement;
import ship.domain.HandlingEvent;
import ship.domain.Location;
import ship.repository.CargoRepository;
import ship.repository.CarrierMovementRepository;
import ship.repository.HandlingEventRepository;
import ship.repository.LocationRepository;

import com.jdon.annotation.Component;
import com.jdon.annotation.intercept.Poolable;
import com.jdon.domain.dci.RoleAssigner;

@Poolable
@Component("handlingEventService")
public class HandlingEventServiceImpl implements HandlingEventService {
	private CargoRepository cargoRepository;
	private CarrierMovementRepository carrierMovementRepository;
	private HandlingEventRepository handlingEventRepository;
	private LocationRepository locationRepository;

	private RoleAssigner roleAssigner;

	public HandlingEventServiceImpl(CargoRepository cargoRepository, CarrierMovementRepository carrierMovementRepository,
			HandlingEventRepository handlingEventRepository, LocationRepository locationRepository, RoleAssigner roleAssigner) {
		super();
		this.cargoRepository = cargoRepository;
		this.carrierMovementRepository = carrierMovementRepository;
		this.handlingEventRepository = handlingEventRepository;
		this.locationRepository = locationRepository;
		this.roleAssigner = roleAssigner;
	}

	public void register(final Date completionTime, final String trackingId, final String carrierMovementId, final String unlocode,
			final HandlingEvent.Type type) throws Exception {

		Cargo cargo = cargoRepository.find(trackingId);
		if (cargo == null)
			throw new Exception(trackingId);

		final CarrierMovement carrierMovement = findCarrierMovement(carrierMovementId);
		final Location location = findLocation(unlocode);
		final Date registrationTime = new Date();

		final HandlingEvent event = new HandlingEvent(carrierMovement, cargo, location, completionTime, registrationTime, type);

		/*
		 * NOTE: The cargo instance that's loaded and associated with the
		 * handling event is in an inconsitent state, because the cargo delivery
		 * history's collection of events does not contain the event created
		 * here. However, this is not a problem, because cargo is in a different
		 * aggregate from handling event.
		 * 
		 * The rules of an aggregate dictate that all consistency rules within
		 * the aggregate are enforced synchronously in the transaction, but
		 * consistency rules of other aggregates are enforced by asynchronous
		 * updates, after the commit of this transaction.
		 */
		handlingEventRepository.save(trackingId, event);

	}

	private CarrierMovement findCarrierMovement(final String carrierMovementId) throws Exception {

		if (carrierMovementId == null) {
			return null;
		}
		final CarrierMovement carrierMovement = carrierMovementRepository.find(carrierMovementId);
		if (carrierMovement == null) {
			throw new Exception(carrierMovementId);
		}

		return carrierMovement;
	}

	private Location findLocation(final String unlocode) throws Exception {
		if (unlocode == null) {
			return null;
		}

		final Location location = locationRepository.find(unlocode);
		if (location == null) {
			throw new Exception(unlocode);
		}

		return location;
	}

	// 卸载
	public void unLoad(final String carrierMovementId, final String unlocode, final String trackingId) throws Exception {
		UnLoadContext unLoadContext = new UnLoadContext(roleAssigner);
		final CarrierMovement carrierMovement = findCarrierMovement(carrierMovementId);
		final Location location = findLocation(unlocode);
		Cargo cargo = cargoRepository.find(trackingId);
		unLoadContext.arrived(carrierMovement, location, cargo);
		// 发送消息给某个货物的客户
		// sendSMS(cargo.getCustomer())
		// send1
		// send3
		// send101
	}

}
