package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

/**
 * Interface of a Model representing a building.
 * A building can have many floors and several elevators.
 */
public interface IBuildingModel {

	
	/**
	 * Adds a floor to the service-list so the elevator will service the specified floor.
	 * @param floorNumber the floor which should be serviced
	 */
	public void AddServiceFloor(int floorNumber);
	
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
	 * Set the list of floors which are serviced by the elevator.
	 * @param floorNumber the floor which should be serviced
	 */
	void SetServiceFloors(List<Integer> serviceFloors);

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

	/**
	 * Get the list of floors which are serviced by the elevator.
	 * @return the floor which are serviced
	 */
	List<Integer> GetServiceFloors();

}