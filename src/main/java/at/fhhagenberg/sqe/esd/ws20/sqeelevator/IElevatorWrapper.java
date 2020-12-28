package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

public interface IElevatorWrapper {

	/** State variable for elevator doors open.	 */
	public final static int ELEVATOR_DOORS_OPEN = 1;	
	/** State variable for elevator doors closed. */
	public final static int ELEVATOR_DOORS_CLOSED = 2;
	/** State variable for elevator doors opening. */
	public final static int ELEVATOR_DOORS_OPENING = 3;
	/** State variable for elevator doors closing. */
	public final static int ELEVATOR_DOORS_CLOSING = 4;
	
	
	/** State variable for elevator status when going up */
	public final static int ELEVATOR_DIRECTION_UP = 0;				
	/** State variable for elevator status when going down. */
	public final static int ELEVATOR_DIRECTION_DOWN = 1;			
	/** State variables for elevator status stopped and uncommitted. */
	public final static int ELEVATOR_DIRECTION_UNCOMMITTED = 2;	
	
	
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
	public void setCommittedDirection(int elevatorNumber, int direction) throws java.rmi.RemoteException;
	
	/**
	 * Retrieves the committed direction of the specified elevator (up / down / uncommitted). 
	 * @param elevatorNumber - elevator number whose committed direction is being retrieved 
	 * @return the current direction of the specified elevator where up=0, down=1 and uncommitted=2
	 */
	public int getCommittedDirection(int elevatorNumber) throws java.rmi.RemoteException; 

	
	
	/* **********************************************************************************
	 * Status
	 ********************************************************************************** */
	
	/**
	 * Provides the current status of the doors of the specified elevator (open/closed).      
	 * @param elevatorNumber - elevator number whose door status is being retrieved 
	 * @return returns the door status of the indicated elevator where 1=open and 2=closed
	 */
	public int getElevatorDoorStatus(int elevatorNumber) throws java.rmi.RemoteException; 

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
	
	
	
	/* **********************************************************************************
	 * Functions not wrapped
	 ********************************************************************************** */
	
	/**
	 * Provides the current location of the specified elevator in feet from the bottom of the building. 
	 * @param elevatorNumber  - elevator number whose location is being retrieved 
	 * @return returns the location in feet of the indicated elevator from the bottom of the building
	 */
	//public int getElevatorPosition(int elevatorNumber) throws java.rmi.RemoteException; 
	
	/**
	 * Retrieves the maximum number of passengers that can fit on the specified elevator.
	 * @param elevatorNumber - elevator number whose service is being retrieved
	 * @return number of passengers
	 */
	//public int getElevatorCapacity(int elevatorNumber) throws java.rmi.RemoteException;
	
	/**
	 * Provides the current acceleration of the specified elevator in feet per sec^2. 
	 * @param elevatorNumber - elevator number whose acceleration is being retrieved 
	 * @return returns the acceleration of the indicated elevator where positive speed is acceleration and negative is deceleration
	 */
	//public int getElevatorAccel(int elevatorNumber) throws java.rmi.RemoteException;
	
}
