package at.fhhagenberg.sqe.esd.ws20.model;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

public class AutoModeExampleAlgo extends AutoMode {

	@Override
	protected int doGetNext(int elevator) {
		
		int numberOfFloors = Building.getNumFloors();
		int sleepTime = 60;
		
		// First: Starting from ground floor, go up to the top floor, stopping in each floor
		
		// Set the committed direction displayed on the elevator to up
		Elevators.get(elevator).setDirection(ElevatorDirection.ELEVATOR_DIRECTION_UP);
		
		for (int nextFloor=1; nextFloor<numberOfFloors; nextFloor++) {
			
			// Set the target floor to the next floor above
			//Elevators.get(elevator).setTarget(nextFloor);
			Updater.setTarget(nextFloor, elevator);
			
			// Wait until closest floor is the target floor and speed is back to zero 
			while (Elevators.get(elevator).getPosition() < nextFloor || Elevators.get(elevator).getSpeed() > 0) {
				try { 
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			
			// Wait until doors are open before setting the next direction
			while (Elevators.get(elevator).getDoors() != ElevatorDoorStatus.ELEVATOR_DOORS_OPEN) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		
		
		// Second, go back from the top floor to the ground floor in one move
		
		// Set the committed direction displayed on the elevator to down
		// controller.setCommittedDirection(elevator, IElevator.ELEVATOR_DIRECTION_DOWN);
		Elevators.get(elevator).setDirection(ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
		
		// Set the target floor to the ground floor (floor number 0)
		Updater.setTarget(0, elevator);
		
		// Wait until ground floor has been reached
		while (Elevators.get(elevator).getPosition() > 0 || Elevators.get(elevator).getSpeed() > 0) {
			try { 
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
		// Set the committed direction to uncommitted when back at the ground floor
		Elevators.get(elevator).setDirection(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		
		
		
		return 0;
	}

}
