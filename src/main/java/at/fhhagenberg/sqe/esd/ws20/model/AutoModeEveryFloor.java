package at.fhhagenberg.sqe.esd.ws20.model;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

public class AutoModeEveryFloor extends AutoMode{

	@Override
	protected int doGetNext(int elevatorNr) {

		IElevatorModel elevator = elevators.get(elevatorNr);
		
		if(elevator.getDoors() == ElevatorDoorStatus.ELEVATOR_DOORS_OPEN && elevator.getPosition() == elevator.getTarget()) {
			
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
