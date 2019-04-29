package ship.repository;

import java.util.HashMap;
import java.util.Map;

import com.jdon.annotation.Component;

import ship.domain.CarrierMovement;
import static ship.repository.SampleLocations.*;

@Component(name="carrierMovementRepository")
public class CarrierMovementRepositoryInMem implements CarrierMovementRepository {
	

	  private Map<String, CarrierMovement> carriers;

	  public CarrierMovementRepositoryInMem() {
	    carriers = new HashMap<String, CarrierMovement>();
	    setup();
	  }

	  private void setup() {
	    final CarrierMovement stockholmToHamburg = new CarrierMovement(
	            "CAR_001", STOCKHOLM, HAMBURG);
	    final CarrierMovement hamburgToHongKong = new CarrierMovement(
	            "CAR_002", HAMBURG, HONGKONG);
	    final CarrierMovement melbourneToTokyo = new CarrierMovement(
	            "CAR_003", MELBOURNE, TOKYO);
	    final CarrierMovement tokyoToChicago = new CarrierMovement(
	            "CAR_004", TOKYO, CHICAGO);
	    final CarrierMovement stockholmToHelsinki = new CarrierMovement(
	            "CAR_005", STOCKHOLM, HELSINKI);
	    
	    carriers.put("SESTO_DEHAM", stockholmToHamburg);
	    carriers.put("DEHAM_CNHKG", hamburgToHongKong);
	    carriers.put("AUMEL_JPTOK", melbourneToTokyo);
	    carriers.put("JPTOK_USCHI", tokyoToChicago);
	    carriers.put("SESTO_FIHEL", stockholmToHelsinki);
	  }

	  public CarrierMovement find(final String carrierMovementId) {
	    return carriers.get(carrierMovementId);
	  }
}
