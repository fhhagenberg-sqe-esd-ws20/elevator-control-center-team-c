package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IElevatorModel {
	
	/**
	 * Add stop to elevator stop list
	 */
	void AddStop(Integer stop);


	void SetTarget(Integer target);

	void SetPosition(Integer position);

	void SetDirection(Integer direction);

	void SetPayload(Integer payload);

	void SetSpeed(Integer speed);

	void SetDoors(Integer doors);

	void SetStops(List<Integer> stops);

	Integer GetTarget();

	Integer GetPosition();

	Integer GetDirection();

	Integer GetPayload();

	Integer GetSpeed();

	Integer GetDoors();

	List<Integer> GetStops();

	/**
	 * Adds a floor to the service-list so the elevator will service the specified floor.
	 * @param floorNumber the floor which should be serviced
	 */
	public void AddServiceFloor(int floorNumber);
	
	/**
	 * Set the list of floors which are serviced by the elevator.
	 * @param floorNumber the floor which should be serviced
	 */
	void SetServiceFloors(List<Integer> serviceFloors);
	
	/**
	 * Get the list of floors which are serviced by the elevator.
	 * @return the floor which are serviced
	 */
	List<Integer> GetServiceFloors();

}