package at.fhhagenberg.sqe.esd.ws20.integration;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevator;

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

	public void setCommittedDirection(int elevatorNumber, int direction) throws java.rmi.RemoteException
	{
		
	}
	
	public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws java.rmi.RemoteException 
	{
		
	}
	
	public void setTarget(int elevatorNumber, int target) throws java.rmi.RemoteException
	{
		
	}

	public long getClockTick() throws java.rmi.RemoteException
	{
		return 452;
	}
	
}

