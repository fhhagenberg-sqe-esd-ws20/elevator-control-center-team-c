package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

/**
 * Interface of a Model representing a building.
 * A building can have many floors and several elevators.
 */
public interface IBuildingModel {

	/**
	 * Set the number of elevators in the building.
	 * @param numElevators number of elevators in this building
	 */
	void SetNumElevators(int numElevators);

	/**
	 * Set the number of floors in the building.
	 * @param numFloors number of floors in this building
	 */
	void SetNumFloors(int numFloors);

	/**
	 * Get the number of elevators in the building.
	 * @return number of elevators in the building
	 */
	int GetNumElevators();

	/**
	 * Get the number of floors in the building.
	 * @return the number of floors in the building.
	 */
	int GetNumFloors();

}