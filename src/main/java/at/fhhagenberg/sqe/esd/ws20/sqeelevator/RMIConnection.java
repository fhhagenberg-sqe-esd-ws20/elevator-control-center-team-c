package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import sqelevator.IElevator;

public class RMIConnection implements IRMIConnection {

	@Override
	public IElevator getElevator() {
	    IElevator elevator = null;
		try {
			elevator = (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			//Nothing to do, just return null ptr
		}
		return elevator;
	}

}
