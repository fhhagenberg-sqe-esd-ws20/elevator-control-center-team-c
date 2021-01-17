package at.fhhagenberg.sqe.esd.ws20.model;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

/**
 * Automode algorithm implementation. 
 * Stops at every serviced floor, up and down. Like a Paternoster elevator system.
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-17 03:40
 */
public class AutoModeEveryFloor extends AutoMode{
	
	
	private enum offsetSearch {
		ABOVE, BELOW
	}

	/**
	 * checks for the next floor that can be serviced. If not keeps checking the next one till one is found.
	 * Returns an offset. The elevator has to travel this offset to get to the next serviced floor.
	 * 
	 * @param elevator
	 * @param direction		search for floors above or below the current elevator position
	 * @return				offset to the next serviced floor from the current elevator position
	 */
	private int getNextTargetOffset(IElevatorModel elevator, offsetSearch direction) {
		int currentPosition = elevator.getPosition();
		int defaultOffset = 1; // advance at least 1 floor by default

		// check if the next floor can be serviced. If not keep checking the next one till one is found.
		if (direction == offsetSearch.ABOVE) {
			for (int i = defaultOffset; i < building.getNumFloors(); i++) {
				if (!elevator.getIgnoredFloorsList().contains(currentPosition + i)) {
					// we found the next floor that can be serviced
					return i;
				}
			}
		}
		
		if (direction == offsetSearch.BELOW) {
			for (int i = defaultOffset; i < building.getNumFloors(); i++) {
				if (!elevator.getIgnoredFloorsList().contains(currentPosition - i)) {
					return i;
				}
			}
		}

		return defaultOffset;
	}
	
	/**
	 * searches for the top most serviced floor to know when the elevator reached the top floor and needs to turn around back down
	 * 
	 * @param elevator
	 * @return				the floor number of the topmost floor that is serviced
	 */
	private int getTopServicedFloor(IElevatorModel elevator) {
		// from top to bottom
		for (int floor = building.getNumFloors() - 1; floor > 0; floor--) {
			if (!elevator.getIgnoredFloorsList().contains(floor)) {
				return floor;
			}
		}
		
		return 0;	// the ground floor is always serviced according to specification
	}
	

	@Override
	protected int doGetNext(int elevatorNr) {

		IElevatorModel elevator = elevators.get(elevatorNr);
		
		int offsetUp = getNextTargetOffset(elevator, offsetSearch.ABOVE);
		int offsetDown = getNextTargetOffset(elevator, offsetSearch.BELOW);
		int topServicedFloor = getTopServicedFloor(elevator);
		// we dont't need a lowestServicedFloor as the ground floor is always serviced according to specification
		
		// elevator doors are open and it arrived at target
		if(elevator.getDoors() == ElevatorDoorStatus.ELEVATOR_DOORS_OPEN && elevator.getPosition().equals(elevator.getTarget())) {
			
			if(elevator.getDirection() == ElevatorDirection.ELEVATOR_DIRECTION_UP) {
				if(elevator.getTarget() < topServicedFloor)
				{
					return elevator.getTarget() + offsetUp;
				}
				else 
				{
					return elevator.getTarget() - offsetDown;
				}
			}
			else if(elevator.getDirection() == ElevatorDirection.ELEVATOR_DIRECTION_DOWN) {
				if(elevator.getTarget() > 0) 
				{
					return elevator.getTarget() - offsetDown;
				}
				else 
				{
					return elevator.getTarget() + offsetUp;
				}
			}
			else {
				if(elevator.getTarget() < topServicedFloor)
				{
					return elevator.getTarget() + offsetUp;
				}
				else if(elevator.getTarget() > 0) 
				{
					return elevator.getTarget() - offsetDown;
				}
				else {
					return elevator.getTarget();
				}
			}
		
		}
		
		return elevator.getTarget();
	}
}
