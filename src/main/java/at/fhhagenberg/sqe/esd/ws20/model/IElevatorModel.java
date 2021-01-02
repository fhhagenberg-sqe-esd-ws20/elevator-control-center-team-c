package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

/**
 * @author Florian Atzenhofer (s1910567001)
 * @since 2020-12-31 09:06
 * 
 * Interface for the ElevatorModel
 * Each elevator has the following attributes:
 * - target
 * - position
 * - direction
 * - payload
 * - speed
 * - door-status
 * Furthermore it has a list of pressed buttons inside the elevator
 * Each elevator has a list of floors which are not serviced by the elevator
 *
 */
public interface IElevatorModel {
	
	/**
	 * Add a floor whose stop button was pressed to the stop list
	 * @param floor the floor whose stop button was pressed
	 */
	void addStop(Integer floor);

	/**
	 * Set the target floor
	 * @param target the target floor
	 */
	void setTarget(Integer target);

	/**
	 * Set the position
	 * @param position the position to set
	 */
	void setPosition(Integer position);

	/**
	 * Set the direction of the elevator
	 * @param direction the direction to set
	 * ELEVATOR_DIRECTION_UP = 0
	 * ELEVATOR_DIRECTION_DOWN = 1
	 * ELEVATOR_DIRECTION_UNCOMMITTED = 2
	 */
	void setDirection(ElevatorDirection direction);

	/**
	 * Set the playload of the elevator
	 * @param payload the payload
	 */
	void setPayload(Integer payload);

	/**
	 * Set the speed of the elevator
	 * @param speed the speed to set
	 */
	void setSpeed(Integer speed);

	/**
	 * Set the status of the doors
	 * @param doors status of the doors
	 * 	 ELEVATOR_DOORS_OPEN = 1
	 *   ELEVATOR_DOORS_CLOSED = 2
	 *   ELEVATOR_DOORS_OPENING = 3
	 *   ELEVATOR_DOORS_CLOSING = 4
	 */
	void setDoors(ElevatorDoorStatus doors);

	/**
	 * Set the list of pressed stop buttons inside the elevator
	 * @param stops list of pressed stops
	 */
	void setStops(List<Integer> stops);

	
	/**
	 * Get the current elevator target
	 * @return target of the elevator
	 */
	Integer getTarget();

	/**
	 * Get the position of the elevator
	 * @return elevator position
	 */
	Integer getPosition();

	/**
	 * Get the elevator direction
	 * @return elevator direction
	 * ELEVATOR_DIRECTION_UP = 0
	 * ELEVATOR_DIRECTION_DOWN = 1
	 * ELEVATOR_DIRECTION_UNCOMMITTED = 2
	 */
	ElevatorDirection getDirection();

	/**
	 * Get the elevator payload
	 * @return elevator payload
	 */
	Integer getPayload();

	/**
	 * Get the speed of the elevator
	 * @return elevator speed
	 */
	Integer getSpeed();

	/**
	 * Get the status of the elevator doors
	 * @return Status of the elevator doors: 
	 *   ELEVATOR_DOORS_OPEN = 1
	 *   ELEVATOR_DOORS_CLOSED = 2
	 *   ELEVATOR_DOORS_OPENING = 3
	 *   ELEVATOR_DOORS_CLOSING = 4
	 */
	ElevatorDoorStatus getDoors();

	List<Integer> getStopsList();

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
	List<Integer> getIgnoredFloorsList();

}