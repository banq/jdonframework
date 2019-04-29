package ship.repository;

import java.util.List;

import ship.domain.Location;

public interface LocationRepository {
	
	/**
	   * Finds a location using given unlocode.
	   *
	   * @param unLocode UNLocode.
	   * @return Location.
	   */
	  Location find(String unLocode);

	  /**
	   * Finds all locations.
	   *
	   * @return All locations.
	   */
	  List<Location> findAll();
}
