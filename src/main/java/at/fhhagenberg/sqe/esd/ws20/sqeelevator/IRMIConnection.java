package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import sqelevator.IElevator;

/**
 * Establishes a connection with the rmi elevator simulator
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-14 14:00
 */
public interface IRMIConnection {

	/**
	 * Establishes a connection with the rmi elevator simulator
	 * 
	 * @return	interface to access all function from the elevator simulator
	 */
	IElevator getElevator();

}
