package at.fhhagenberg.sqe.esd.ws20.model;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

/**
 * Automode algorithm implementation. 
 * Stops at every floor, up and down. Like a Paternoster elevator system.
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-17 22:00
 */
public class AutoModeEveryFloor extends AutoMode{

	@Override
	protected int doGetNext(int elevatorNr) {

		IElevatorModel elevator = elevators.get(elevatorNr);
		
		if(elevator.getDoors() == ElevatorDoorStatus.ELEVATOR_DOORS_OPEN && elevator.getPosition().equals(elevator.getTarget())) {
			
			if(elevator.getDirection() == ElevatorDirection.ELEVATOR_DIRECTION_UP) {
				if(elevator.getTarget() < building.getNumFloors()-1)
				{
					return elevator.getTarget()+1;
				}
				else 
				{
					return elevator.getTarget()-1;
				}
			}
			else if(elevator.getDirection() == ElevatorDirection.ELEVATOR_DIRECTION_DOWN) {
				if(elevator.getTarget() > 0) 
				{
					return elevator.getTarget()-1;
				}
				else 
				{
					return elevator.getTarget()+1;
				}
			}
			else {
				if(elevator.getTarget() < building.getNumFloors()-1)
				{
					return elevator.getTarget()+1;
				}
				else if(elevator.getTarget() > 0) 
				{
					return elevator.getTarget()-1;
				}
				else {
					return elevator.getTarget();
				}
			}
		
		}
		
		return elevator.getTarget();
	}
}
