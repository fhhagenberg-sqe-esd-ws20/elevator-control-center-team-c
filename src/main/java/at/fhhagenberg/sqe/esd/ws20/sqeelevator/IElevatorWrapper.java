package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

/**
 * Wraps all elevator functions related to the elevator
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2020-12-28 16:00
 */
public interface IElevatorWrapper {

	public enum ElevatorDoorStatus
	{
		ELEVATOR_DOORS_UNKNOWN, ELEVATOR_DOORS_OPEN, ELEVATOR_DOORS_CLOSED, ELEVATOR_DOORS_OPENING, ELEVATOR_DOORS_CLOSING
	}
	
	public enum ElevatorDirection
	{
		ELEVATOR_DIRECTION_UP, ELEVATOR_DIRECTION_DOWN, ELEVATOR_DIRECTION_UNCOMMITTED
	}
	
	
	/* **********************************************************************************
	 * Target
	 ********************************************************************************** */
	
	/**
	 * Sets the floor target of the specified elevator. 
	 * @param elevatorNumber elevator number whose target floor is being set
	 * @param target floor number which the specified elevator is to target
	 */
	public void setTarget(int elevatorNumber, int target) throws java.rmi.RemoteException; 
	
	/**
	 * Retrieves the floor target of the specified elevator. 
	 * @param elevatorNumber elevator number whose target floor is being retrieved
	 * @return current floor target of the specified elevator
	 */
	public int getTarget(int elevatorNumber) throws java.rmi.RemoteException; 
	
	
	/**
	 * Sets the committed direction of the specified elevator (up / down / uncommitted). 
	 * @param elevatorNumber elevator number whose committed direction is being set
	 * @param direction direction being set where up=0, down=1 and uncommitted=2
	 */
	public void setCommittedDirection(int elevatorNumber, ElevatorDirection direction) throws java.rmi.RemoteException;
	
	/**
	 * Retrieves the committed direction of the specified elevator (up / down / uncommitted). 
	 * @param elevatorNumber - elevator number whose committed direction is being retrieved 
	 * @return the current direction of the specified elevator where up=0, down=1 and uncommitted=2
	 */
	public ElevatorDirection getCommittedDirection(int elevatorNumber) throws java.rmi.RemoteException; 

	
	
	/* **********************************************************************************
	 * Status
	 ********************************************************************************** */
	
	/**
	 * Provides the current status of the doors of the specified elevator (open/closed).      
	 * @param elevatorNumber - elevator number whose door status is being retrieved 
	 * @return returns the door status of the indicated elevator where 1=open and 2=closed
	 */
	public ElevatorDoorStatus getElevatorDoorStatus(int elevatorNumber) throws java.rmi.RemoteException; 

	/**
	 * Provides the current location of the specified elevator to the nearest floor 
	 * @param elevatorNumber - elevator number whose location is being retrieved 
	 * @return returns the floor number of the floor closest to the indicated elevator
	 */
	public int getElevatorFloor(int elevatorNumber) throws java.rmi.RemoteException; 

	/**
	 * Provides the current speed of the specified elevator in feet per sec. 
	 * @param elevatorNumber - elevator number whose speed is being retrieved 
	 * @return returns the speed of the indicated elevator where positive speed is up and negative is down
	 */
	public int getElevatorSpeed(int elevatorNumber) throws java.rmi.RemoteException; 

	/**
	 * Retrieves the weight of passengers on the specified elevator. 
	 * @param elevatorNumber - elevator number whose service is being retrieved
	 * @return total weight of all passengers in lbs
	 */
	public int getElevatorWeight(int elevatorNumber) throws java.rmi.RemoteException; 
	
	

	/* **********************************************************************************
	 * Buttons
	 ********************************************************************************** */

	/**
	 * Provides the status of a floor request button on a specified elevator (on/off).      
	 * @param elevatorNumber - elevator number whose button status is being retrieved
	 * @param floor - floor number button being checked on the selected elevator 
	 * @return returns boolean to indicate if floor button on the elevator is active (true) or not (false)
	 */
	public boolean getElevatorButton(int elevatorNumber, int floor) throws java.rmi.RemoteException;
	
	
	
	/* **********************************************************************************
	 * Serviced Floors
	 ********************************************************************************** */
	
	/** 
	 * Retrieves whether or not the specified elevator will service the specified floor (yes/no). 
	 * @param elevatorNumber elevator number whose service is being retrieved
	 * @param floor floor whose service status by the specified elevator is being retrieved
	 * @return service status whether the floor is serviced by the specified elevator (yes=true,no=false)
	 */
	public boolean getServicesFloors(int elevatorNumber, int floor) throws java.rmi.RemoteException; 
	
	
	/**
	 * Sets whether or not the specified elevator will service the specified floor (yes/no). 
	 * @param elevatorNumber elevator number whose service is being defined
	 * @param floor floor whose service by the specified elevator is being set
	 * @param service indicates whether the floor is serviced by the specified elevator (yes=true,no=false)
	 */
	public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws java.rmi.RemoteException; 
	
	
	
	/* **********************************************************************************
	 * Update
	 ********************************************************************************** */
	
	/**
	 * Retrieves the current clock tick of the elevator control system. 
	 * @return clock tick
	 */
	public long getClockTick() throws java.rmi.RemoteException;
	
}
