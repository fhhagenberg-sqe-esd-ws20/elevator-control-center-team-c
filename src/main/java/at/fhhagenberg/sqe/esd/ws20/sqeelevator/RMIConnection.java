package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.SecureRandom;

import sqelevator.IElevator;

public class RMIConnection implements IRMIConnection {

	@Override
	public IElevator getElevator() {
	    IElevator elevator = null;
		try {
			elevator = (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
			//not done by simulator -> generate random floors which are not serviced to show functionality
			generateNotServicedFloors(elevator);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			//Nothing to do, just return null ptr
		}
		return elevator;
	}
	
	
	/**
	 * generate floors that are not serviced. This is not done by the simulator -> we have to do it.
	 * If there are already not serviced floors set, this function does not overwrite them and returns.
	 */
	private void generateNotServicedFloors(IElevator elevator) throws RemoteException {
		//ground floor gets always serviced according to specification
		if (elevator.getFloorNum() <= 1) {
			return;
		}
		
		int numElevators = elevator.getElevatorNum();
		int numFloors = elevator.getFloorNum();
		
		//check if there are already no serviced floors set -> don't overwrite them
		boolean anyFloorNotServiced = false;
		for(int elev = 0; elev < numElevators; elev++) {
			for(int floor = 0; floor < numFloors; floor++) {
				if(!elevator.getServicesFloors(elev, floor)) anyFloorNotServiced = true;
			}
		}
		if(anyFloorNotServiced) {
			return;
		}
		
		//all floors are serviced -> add some not serviced ones
		SecureRandom random = new SecureRandom();
		for(int i = 0; i < NOT_SERVICED_FLOORS_RANDOM_ITERATIONS; i++) {
			int elev = random.nextInt(numElevators);
			int floor = random.nextInt(numFloors);	//this can also generate 0. This will be ignored by elevator simulator as ground floor will always be serviced.
				elevator.setServicesFloors(elev, floor, false);
		}
	}
	
    private static final int NOT_SERVICED_FLOORS_RANDOM_ITERATIONS = 15;

}
