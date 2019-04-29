package ship.repository;

import java.util.List;

import ship.domain.Cargo;


public interface CargoRepository {
	/**
	   * Finds a cargo using given id.
	   *
	   * @param trackingId Id
	   * @return Cargo if found, else {@code null}
	   */
	  Cargo find(String trackingId);

	  /**
	   * Finds all cargo.
	   *
	   * @return All cargo.
	   */
	  List<Cargo> findAll();

	  /**
	   * Saves given cargo.
	   *
	   * @param cargo cargo to save
	   */
	  void save(Cargo cargo);
}
