package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import java.rmi.RemoteException;

/**
 * Class to connect the provided interfaces with the elevator interface provided 
 * by the elevator manufacturer.
 *
 */
public class ElevatorWrapper implements IElevatorWrapper, IBuildingWrapper {

	
	private IElevator elevatorInterface;
	
	/**
	 * Constructor for ElevatorWaraper.
	 * Connects the provided interfaces with the elevator interface provided 
	 * by the elevator manufacturer.
	 * @param elevatorInterface 	Interface to the elevator 
	 */
	public ElevatorWrapper(IElevator elevatorInterface) {
		this.elevatorInterface = elevatorInterface;
	}
	
	
	
	@Override
	public int getElevatorNum() throws RemoteException {
		return elevatorInterface.getElevatorNum();
	}

	@Override
	public int getFloorNum() throws RemoteException {
		return elevatorInterface.getFloorNum();
	}

	@Override
	public boolean getFloorButtonDown(int floor) throws RemoteException {
		return elevatorInterface.getFloorButtonDown(floor);
	}

	@Override
	public boolean getFloorButtonUp(int floor) throws RemoteException {
		return elevatorInterface.getFloorButtonUp(floor);
	}

	
	
	
	@Override
	public void setTarget(int elevatorNumber, int target) throws RemoteException {
		elevatorInterface.setTarget(elevatorNumber, target);	
	}

	@Override
	public int getTarget(int elevatorNumber) throws RemoteException {
		return elevatorInterface.getTarget(elevatorNumber);
	}

	@Override
	public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
		elevatorInterface.setCommittedDirection(elevatorNumber, direction);	
	}

	@Override
	public int getCommittedDirection(int elevatorNumber) throws RemoteException {
		return elevatorInterface.getCommittedDirection(elevatorNumber);
	}

	@Override
	public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
		return elevatorInterface.getElevatorDoorStatus(elevatorNumber);
	}

	@Override
	public int getElevatorFloor(int elevatorNumber) throws RemoteException {
		return elevatorInterface.getElevatorFloor(elevatorNumber);
	}

	@Override
	public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
		return elevatorInterface.getElevatorSpeed(elevatorNumber);
	}

	@Override
	public int getElevatorWeight(int elevatorNumber) throws RemoteException {
		return elevatorInterface.getElevatorWeight(elevatorNumber);
	}

	@Override
	public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
		return elevatorInterface.getElevatorButton(elevatorNumber, floor);
	}

	@Override
	public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
		return elevatorInterface.getServicesFloors(elevatorNumber, floor);
	}

	@Override
	public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
		elevatorInterface.setServicesFloors(elevatorNumber, floor, service);
	}

	
	
	
	@Override
	public long getClockTick() throws RemoteException {
		return elevatorInterface.getClockTick();
	}

}
