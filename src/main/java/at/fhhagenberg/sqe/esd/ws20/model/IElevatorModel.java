package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IElevatorModel {
	
	/**
	 * Add stop to elevator stop list
	 */
	void addStop(Integer stop);


	void setTarget(Integer target);

	void setPosition(Integer position);

	void setDirection(Integer direction);

	void setPayload(Integer payload);

	void setSpeed(Integer speed);

	void setDoors(Integer doors);

	void setStops(List<Integer> stops);

	Integer getTarget();

	Integer getPosition();

	Integer getDirection();

	Integer getPayload();

	Integer getSpeed();

	Integer getDoors();

	List<Integer> getStops();

	/**
	 * Adds a floor to the service-list so the elevator will service the specified floor.
	 * @param floorNumber the floor which should be serviced
	 */
	public void addIgnoredFloor(int floorNumber);
	
	/**
	 * Set the list of floors which are serviced by the elevator.
	 * @param floorNumber the floor which should be serviced
	 */
	void setIgnoredFloors(List<Integer> serviceFloors);
	
	/**
	 * Get the list of floors which are serviced by the elevator.
	 * @return the floor which are serviced
	 */
	List<Integer> getIgnoredFloors();

}