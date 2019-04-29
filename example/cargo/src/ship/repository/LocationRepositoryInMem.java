package ship.repository;

import java.util.List;

import com.jdon.annotation.Component;

import ship.domain.Location;

@Component(name="locationRepository")
public class LocationRepositoryInMem implements LocationRepository {

	  public Location find(String unLocode) {
	    for (Location location : SampleLocations.getAll()) {
	      if (location.getUcode().equals(unLocode)) {
	        return location;
	      }
	    }
	    return null;
	  }

	  public List<Location> findAll() {
	    return SampleLocations.getAll();
	  }
}
