package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import java.rmi.RemoteException;

public class ElevatorWrapperStub implements IElevatorWrapper, IBuildingWrapper  {

	@Override
	public int getElevatorNum() throws RemoteException {
		return 4;
	}

	@Override
	public int getFloorNum() throws RemoteException {
		return 12;
	}

	@Override
	public boolean getFloorButtonDown(int floor) throws RemoteException {
		return false;
	}

	@Override
	public boolean getFloorButtonUp(int floor) throws RemoteException {
		return false;
	}

	@Override
	public void setTarget(int elevatorNumber, int target) throws RemoteException {
	}

	@Override
	public int getTarget(int elevatorNumber) throws RemoteException {
		return 5;
	}

	@Override
	public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
	}

	@Override
	public int getCommittedDirection(int elevatorNumber) throws RemoteException {
		return 1;
	}

	@Override
	public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
		return 2;
	}

	@Override
	public int getElevatorFloor(int elevatorNumber) throws RemoteException {
		return 3;
	}

	@Override
	public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
		return 22;
	}

	@Override
	public int getElevatorWeight(int elevatorNumber) throws RemoteException {
		return 345;
	}

	@Override
	public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
		return false;
	}

	@Override
	public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
		return false;
	}

	@Override
	public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
	}

	@Override
	public long getClockTick() throws RemoteException {
		return 111;
	}

}
