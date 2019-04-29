package ship.repository;

import static ship.repository.SampleLocations.HELSINKI;
import static ship.repository.SampleLocations.MELBOURNE;
import static ship.repository.SampleLocations.STOCKHOLM;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jdon.annotation.Component;

import ship.domain.Cargo;
import ship.domain.CarrierMovement;
import ship.domain.DeliverySpec;
import ship.domain.HandlingEvent;
import ship.domain.HandlingEvent.Type;

@Component(name="handlingEventRepository")
public class HandlingEventRepositoryInMem implements HandlingEventRepository {

	private final HashMap<String, List> eventDB = new HashMap<String, List>();
	private CarrierMovementRepository carrierMovementRepository;

	public HandlingEventRepositoryInMem(
			CarrierMovementRepository carrierMovementRepository) {
		super();
		this.carrierMovementRepository = carrierMovementRepository;
		init();
	}

	/**
	 * Initilaze the in mem repository.
	 * <p/>
	 * SpringIoC will call this init-method after the bean has bean created and
	 * properties has been set.
	 * 
	 * @throws ParseException
	 */
	public void init() {
		// CargoXYZ
		DeliverySpec deliverySpec = new DeliverySpec();
		deliverySpec.setOrigin(STOCKHOLM);
		deliverySpec.setDestination(MELBOURNE);
		final Cargo cargoXYZ = new Cargo("XYZ", deliverySpec);
		registerEvent(cargoXYZ, "2007-11-30", HandlingEvent.Type.RECEIVE, null);

		final CarrierMovement stockholmToHamburg = carrierMovementRepository
				.find("SESTO_DEHAM");
		registerEvent(cargoXYZ, "2007-12-01", HandlingEvent.Type.LOAD,
				stockholmToHamburg);
		registerEvent(cargoXYZ, "2007-12-02", HandlingEvent.Type.UNLOAD,
				stockholmToHamburg);

		final CarrierMovement hamburgToHongKong = carrierMovementRepository
				.find("DEHAM_CNHKG");
		registerEvent(cargoXYZ, "2007-12-03", HandlingEvent.Type.LOAD,
				hamburgToHongKong);
		registerEvent(cargoXYZ, "2007-12-05", HandlingEvent.Type.UNLOAD,
				hamburgToHongKong);

		// CargoZYX
		DeliverySpec deliverySpec2 = new DeliverySpec();
		deliverySpec.setOrigin(MELBOURNE);
		deliverySpec.setDestination(STOCKHOLM);
		final Cargo cargoZYX = new Cargo("ZYX", deliverySpec2);
		registerEvent(cargoZYX, "2007-12-09", HandlingEvent.Type.RECEIVE, null);

		final CarrierMovement melbourneToTokyo = carrierMovementRepository
				.find("AUMEL_JPTOK");
		registerEvent(cargoZYX, "2007-12-10", HandlingEvent.Type.LOAD,
				melbourneToTokyo);
		registerEvent(cargoZYX, "2007-12-12", HandlingEvent.Type.UNLOAD,
				melbourneToTokyo);

		final CarrierMovement tokyoToLosAngeles = carrierMovementRepository
				.find("JPTOK_USLA");
		registerEvent(cargoZYX, "2007-12-13", HandlingEvent.Type.LOAD,
				tokyoToLosAngeles);

		// CargoABC
		DeliverySpec deliverySpec3 = new DeliverySpec();
		deliverySpec.setOrigin(STOCKHOLM);
		deliverySpec.setDestination(HELSINKI);
		final Cargo cargoABC = new Cargo("ABC", deliverySpec3);
		registerEvent(cargoABC, "2008-01-01", HandlingEvent.Type.RECEIVE, null);

		final CarrierMovement stockholmToHelsinki = new CarrierMovement(
				"CAR_001", STOCKHOLM, HELSINKI);

		registerEvent(cargoABC, "2008-01-02", HandlingEvent.Type.LOAD,
				stockholmToHelsinki);
		registerEvent(cargoABC, "2008-01-03", HandlingEvent.Type.UNLOAD,
				stockholmToHelsinki);
		registerEvent(cargoABC, "2008-01-05", HandlingEvent.Type.CLAIM, null);

		// CargoCBA
		DeliverySpec deliverySpec4 = new DeliverySpec();
		deliverySpec.setOrigin(HELSINKI);
		deliverySpec.setDestination(STOCKHOLM);
		final Cargo cargoCBA = new Cargo("CBA", deliverySpec4);
		registerEvent(cargoCBA, "2008-01-10", HandlingEvent.Type.RECEIVE, null);
	}

	private void registerEvent(Cargo cargo, String date, Type type,
			CarrierMovement carrierMovement) {
		HandlingEvent event = new HandlingEvent(carrierMovement, cargo, null,
				getDate(date), new Date(), type);
		save(cargo.getId(), event);
	}

	public void save(String Id, HandlingEvent event) {
		List list = (List) eventDB.get(Id);
		if (list == null) {
			list = new ArrayList();
			list.add(event);
			eventDB.put(Id, list);
		} else {
			list.add(event);
		}
	}

	public List<HandlingEvent> findEventsForCargo(String trackingId) {
		return eventDB.get(trackingId);
	}

	/**
	 * Parse an ISO 8601 (YYYY-MM-DD) String to Date
	 * 
	 * @param isoFormat
	 *            String to parse.
	 * @return Created date instance.
	 * @throws ParseException
	 *             Thrown if parsing fails.
	 */
	private Date getDate(final String isoFormat) {
		try {
			final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.parse(isoFormat);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void setCarrierRepository(
			final CarrierMovementRepository carrierMovementRepository) {
		this.carrierMovementRepository = carrierMovementRepository;
	}
}
