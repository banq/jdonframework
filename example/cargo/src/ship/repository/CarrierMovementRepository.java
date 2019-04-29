package ship.repository;

import com.jdon.annotation.Component;

import ship.domain.CarrierMovement;


public interface CarrierMovementRepository {
	
	/**
	   * Finds a carrier movement using given id.
	   *
	   * @param carrierMovementId Id
	   * @return The carrier movement.
	   */
	  CarrierMovement find(String carrierMovementId);
}
