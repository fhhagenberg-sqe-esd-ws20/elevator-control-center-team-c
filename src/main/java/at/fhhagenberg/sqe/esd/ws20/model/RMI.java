package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.ElevatorWrapper;
import sqelevator.IElevator;

public class RMI {

	
    public void SetRMIs(IElevator elevator, UpdateData coreUpdater) throws RemoteException {
			ElevatorWrapper wrap = new ElevatorWrapper(elevator);
			coreUpdater.SetSqs(wrap, wrap);
    }
	
}
