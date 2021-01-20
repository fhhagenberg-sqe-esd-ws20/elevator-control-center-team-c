package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

/**
 * Wraps all elevator functions related to the building
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2020-12-28 16:00
 */
public interface IBuildingWrapper {

	/* **********************************************************************************
	 * Building information
	 ********************************************************************************** */
	
	/**
	 * Retrieves the number of elevators in the building. 
	 * @return total number of elevators
	 */
	public int getElevatorNum() throws java.rmi.RemoteException; 
	
	
	/**
	 * Retrieves the number of floors in the building. 
	 * @return total number of floors
	 */
	public int getFloorNum() throws java.rmi.RemoteException; 
	
	
	/* **********************************************************************************
	 * Buttons
	 ********************************************************************************** */
	
	/**
	 * Provides the status of the Down button on specified floor (on/off). 
	 * @param floor - floor number whose Down button status is being retrieved 
	 * @return returns boolean to indicate if button is active (true) or not (false)
	 */
	public boolean getFloorButtonDown(int floor) throws java.rmi.RemoteException; 

	/**
	 * Provides the status of the Up button on specified floor (on/off). 
	 * @param floor - floor number whose Up button status is being retrieved 
	 * @return returns boolean to indicate if button is active (true) or not (false)
	 */
	public boolean getFloorButtonUp(int floor) throws java.rmi.RemoteException; 



	/* **********************************************************************************
	 * Update
	 ********************************************************************************** */
	
	/**
	 * Retrieves the current clock tick of the elevator control system. 
	 * @return clock tick
	 */
	public long getClockTick() throws java.rmi.RemoteException;
	
}
