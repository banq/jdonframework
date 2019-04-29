package ship.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jdon.annotation.Component;

import ship.domain.Cargo;
import static ship.repository.SampleLocations.*;

@Component(name="cargoRepository")
public class CargoRepositoryInMem implements CargoRepository {

	  private Map<String, Cargo> cargoDb;
	  private HandlingEventRepository handlingEventRepository;

	  /**
	   * Constructor.
	   */
	  public CargoRepositoryInMem(HandlingEventRepository handlingEventRepository) {
		this.handlingEventRepository = handlingEventRepository;
	    cargoDb = new HashMap<String, Cargo>();
	   
	  }

	  public Cargo find(final String trackingId) {
	    if(cargoDb.size() == 0)init();
	    return cargoDb.get(trackingId);
	  }

	  public void save(final Cargo cargo) {
	    //No need to save anything with InMem
		  cargoDb.put(cargo.getId(), cargo);
	  }
	

	  public List<Cargo> findAll() {
	    return new ArrayList(cargoDb.values());
	  }

	  public void init()  {
	    final Cargo cargoXYZ = CargoTestHelper.createCargoWithDeliveryHistory(
	    		"XYZ", STOCKHOLM, MELBOURNE, handlingEventRepository.findEventsForCargo("XYZ"));
	    cargoDb.put("XYZ", cargoXYZ);

	    final Cargo cargoZYX = CargoTestHelper.createCargoWithDeliveryHistory(
	    		"ZYX", MELBOURNE, STOCKHOLM, handlingEventRepository.findEventsForCargo("ZYX"));
	    cargoDb.put("ZYX", cargoZYX);

	    final Cargo cargoABC = CargoTestHelper.createCargoWithDeliveryHistory(
	    		"ABC", STOCKHOLM, HELSINKI, handlingEventRepository.findEventsForCargo("ABC"));
	    cargoDb.put("ABC", cargoABC);

	    final Cargo cargoCBA = CargoTestHelper.createCargoWithDeliveryHistory(
	    		"CBA", HELSINKI, STOCKHOLM, handlingEventRepository.findEventsForCargo("CBA"));
	    cargoDb.put("CBA", cargoCBA);
	  }

	  public void setHandlingEventRepository(final HandlingEventRepository handlingEventRepository) {
	    this.handlingEventRepository = handlingEventRepository;
	  }
}
