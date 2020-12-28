package at.fhhagenberg.sqe.esd.ws20.model;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;

public class ElevatorSimulatorMock implements IElevatorWrapper 
{

	
	
	
	

	public long getClockTick() throws java.rmi.RemoteException{
		return 1;
	}
	
}
