package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import sqelevator.IElevator;

/**
 * Stub for the elevator. Always returns hardcoded values.
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-16 00:40
 */
public class ElevatorStub implements IElevator {	
	
	public int getCommittedDirection(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 0;
	}

	public int getElevatorAccel(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 125;
	}

	public boolean getElevatorButton(int elevatorNumber, int floor) throws java.rmi.RemoteException
	{
		return true;
	}
	
	public int getElevatorDoorStatus(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 1;
	}
	
	public int getElevatorFloor(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 3;
	}
	
	public int getElevatorNum() throws java.rmi.RemoteException 
	{
		return 4;
	}
	
	public int getElevatorPosition(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 6;
	}

	public int getElevatorSpeed(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 33;
	}
	
	public int getElevatorWeight(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 123;
	}
	
	public int getElevatorCapacity(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 7;
	}
	
	public boolean getFloorButtonDown(int floor) throws java.rmi.RemoteException
	{
		return true;
	}
	
	public boolean getFloorButtonUp(int floor) throws java.rmi.RemoteException
	{
		return true;
	}
	
	public int getFloorHeight() throws java.rmi.RemoteException
	{
		return 22;
	}
	
	public int getFloorNum() throws java.rmi.RemoteException
	{
		return 25;
	}
	
	public boolean getServicesFloors(int elevatorNumber, int floor) throws java.rmi.RemoteException 
	{
		return true;
	}

	public int getTarget(int elevatorNumber) throws java.rmi.RemoteException
	{
		return 4;
	}

	public void setCommittedDirection(int elevatorNumber, int direction)
	{
		throw new UnsupportedOperationException();
	}
	
	public void setServicesFloors(int elevatorNumber, int floor, boolean service)
	{
		throw new UnsupportedOperationException();
	}
	
	public void setTarget(int elevatorNumber, int target)
	{
		throw new UnsupportedOperationException();
	}

	public long getClockTick() throws java.rmi.RemoteException
	{
		return 452;
	}
	
}

