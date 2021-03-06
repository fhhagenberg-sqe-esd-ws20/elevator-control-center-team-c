package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.ElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IRMIConnection;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;
import sqelevator.IElevator;


/**
 * Represents the updater, which refreshes all data of the elevator every timer tick
 *
 * @author Florian Atzenhofer (s1910567001)
 * @since 2021-01-18 20:00
 */
public class UpdateData extends TimerTask {

	/**
	 * Constructor initializes the number of floors and elevators
	 * 
	 * @param sqbuilding - the simulator for the building, can be null
	 * @param sqelevator - the simulator for the elevators, can be null
	 * @param building - the internal model of the building
	 * @param floor - the internal model for the floors
	 * @param elevators - the internal list with the models for the elevators
	 * @param guiController - the controller, which controls the gui
	 * @param autoModeAlgorithm - the algorithm to calculate the next elevator target in automatic mode
	 * @throws RemoteException 
	 */
	public UpdateData(IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators, MainGuiController guiController, StatusAlert statusAlert, AutoMode autoModeAlgorithm, IRMIConnection rmiConnection)
	{
		
		//sqbuilding, sqelevator can be null -> next update cycle a reconnect will be done
		if(building == null || floor == null || elevators == null || guiController == null || statusAlert == null || autoModeAlgorithm == null || rmiConnection == null)
		{
			throw new NullPointerException("Nullpointer in UpdateData!");
		}
		
		// assign models to the internal fields
		buildingModel = building;
		floorModel = floor;
		elevatorsList = elevators;
		mainGuiController = guiController;
		statusAlertContext = statusAlert;
		autoModeAlgorithmContext = autoModeAlgorithm;
		rmiConnectionContext = rmiConnection;
		outOfSyncCounter = 0;
		connected = false;
	}
	


	/** 
	 * Initializes all constant properties of the building
	 * 
	 * @throws RemoteException
	 */
	public void initializeBuilding() throws RemoteException
	{
		// get number of floors and number of elevators from the building
		buildingModel.setNumFloors(sqBuilding.getFloorNum());
		buildingModel.setNumElevators(sqBuilding.getElevatorNum());
		outOfSyncCounter = 0;
	}
	
	

	
	
	/** 
	 * Set service floor for each elevator in the building
	 * @throws RemoteException
	 */
	public void initializeServicedFloors()
	{
		//check if the number of stored elevators is the same as the number of 
		//elevators in the building
		if(buildingModel.getNumElevators() != elevatorsList.size()) 
		{
			throw new InvalidParameterException("Numer of stored elevators not the same as number of elevators in the building!");
		}
		
		// get all servicefloors of each elevator and store them in a list
		for(int elevator = 0; elevator < elevatorsList.size(); elevator++)
		{
			elevatorsList.get(elevator).clearIgnoredFloorsList();
			
			for(int floor = 0; floor < buildingModel.getNumFloors(); floor++)
			{
				boolean floorServiced = false;
				try {
					floorServiced = sqElevator.getServicesFloors(elevator, floor);
				} catch (RemoteException e) {
					if(connected)
					{
						statusAlertContext.setStatus("Exception in getServicesFloors() of SQElevator with floor " + floor + " and elevator " + elevator);
					}
				}
				
				if(!floorServiced)
				{
					elevatorsList.get(elevator).addIgnoredFloor(floor);
				}
			}
		}
		outOfSyncCounter = 0;
	}
	
	
	/** 
	 * Initializes all elevators
	 * @throws RemoteException
	 */
	public void initializeElevators()
	{
		elevatorsList.clear();
        for(int i = 0; i < buildingModel.getNumElevators(); i++)
        {
        	elevatorsList.add(new ElevatorModel());
        }
	}
	
	
	public boolean updateElevatorClockTick() {
		try {
			currentTick = sqElevator.getClockTick();
			return false;
		} catch (RemoteException e) {
			return true;
		}
	}
	
    /**
     * Periodic task refreshes properties of the elevator periodically
     */
    @Override
    public void run() {
        try {
        	
        	if(sqElevator != null && sqBuilding != null) {
        	
            	boolean error = false;
            	
            	error |= updateElevatorClockTick();
            	//Get simulator time because if the time is the same, no update needs to be done
            	//if time is running backwards -> simulator was restarted -> Do a reconnect and new initialization

        		if(currentTick < lastTick) 
        		{
        			error |= true;	
        		}
        		
            	// refresh list with the up and down buttons of the floors
            	error |= refreshUpDownList();
            	
            	// refresh the fields of all elevators
            	for(int i = 0; i < elevatorsList.size(); i++)
            	{
            		error |= refreshElevator(i);
            	}
            	
            	if(outOfSyncCounter > criticalOutOfSyncValue)
            	{
            		if(connected)
            		{
            			statusAlertContext.setStatus("Out of sync with the simulator. We are to slow with polling values from the Elevator Interface.");
            		}
        		}
            	else if(!error && currentTick > lastTick) {
            		autoModeAlgorithmContext.updateAllElevatorTargets();
            	}
            	
            	lastTick = currentTick;
            	
            	if(error) {
            		//try to reinitialize rmi
            		reconnectRMI();
            	}
        		
        	}
        	else 
        	{
        		//try to reinitialize rmi
        		reconnectRMI();
        	}
        	

        //catch everything else that was not dealt with before, therefore this message is more verbose
        } 
        catch (Exception ex) {
        	if(connected)
        	{
        		statusAlertContext.setStatus("Exception when getting values from SQelevator: " + ex.getClass() + ": " + ex.getLocalizedMessage());
        	}
    	}
    }
    

    /**
     * Set the index of the elevator, which is currently selected in the view
     * 
     * @param elevatorIdx - index of the elevator, which is selected in the GUI
     */
    public void setSelectedElevator(int elevatorIdx)
    {
    	if(elevatorIdx >= 0 && elevatorIdx < elevatorsList.size())
		{
			selectedElevator = elevatorIdx;
			mainGuiController.update(floorModel, elevatorsList.get(selectedElevator));
		}
    }
    
    /**
     * Set a new target for the elevator
     * 
     * @param floor - index of the floor, where the elevator should stop
     * @param elevator - index of the elevator, which should stop in the floor
     */
    public void setTarget(int floor, int elevator)
    {
    	if(floor >= 0 && floor < buildingModel.getNumFloors() && elevator >= 0 && elevator < elevatorsList.size())
		{
			elevatorsList.get(elevator).setTarget(floor);
			
			// set new target for SQElevator
			try {
				int currentPosition = sqElevator.getElevatorFloor(elevator);
				sqElevator.setTarget(elevator, floor);
				setComittedDirection(currentPosition, floor, elevator);
				
			} catch (RemoteException e) {
				if(connected)
				{
					statusAlertContext.setStatus("Exception in setTarget of SQElevator with floor: " + floor + " for Elevator" + elevator);
				}
			}
			
			if(elevator == selectedElevator)
			{
				mainGuiController.update(floorModel, elevatorsList.get(selectedElevator));
			}
		}
    }
    
    
    /**
     * Set a new target for the elevator
     * 
     * @param floor - index of the floor, where the elevator should stop
     */
    public void setTarget(int floor)
    {
    	if(floor >= 0 && floor < buildingModel.getNumFloors())
    	{
    		elevatorsList.get(selectedElevator).setTarget(floor);
    		
			// set new target for SQElevator
			try {
				int currentPosition = sqElevator.getElevatorFloor(selectedElevator);
				sqElevator.setTarget(selectedElevator, floor);
				setComittedDirection(currentPosition, floor, selectedElevator);
				
			} catch (RemoteException e) {
				if(connected)
				{
					statusAlertContext.setStatus("Exception in setTarget of SQElevator with floor: " + floor);
				}
			}
    		mainGuiController.update(floorModel, elevatorsList.get(selectedElevator));
    	}
    }

	/**
	 * Returns the list of ignored floors for the currently selected elevator
	 * 
	 * @return	a list of ignored floors
	 */
	public List<Integer> getIgnoredFloorsFromSelectedElevator() {
		try {
			return elevatorsList.get(selectedElevator).getIgnoredFloorsList();
		} catch (IndexOutOfBoundsException e) {
			statusAlertContext.setStatus("Exception in getIgnoredFloorsFromSelectedElevator()");
			return new ArrayList<>();
		}
	}
    
    
    /**
     * Set the comitted direction depending on current floor and target floor
     * 
     * @param currentFloor - current position of the elevator
     * @param targetFloor - index of the floor, where the elevator should stop
     * @param elevator - index of the elevator, which should change the comitted direction
     */
    private void setComittedDirection(int currentFloor, int targetFloor, int elevatorIdx) throws RemoteException
    {
		if(targetFloor > currentFloor)
		{
			sqElevator.setCommittedDirection(elevatorIdx, ElevatorDirection.ELEVATOR_DIRECTION_UP);
		}
		else if(targetFloor < currentFloor)
		{
			sqElevator.setCommittedDirection(elevatorIdx, ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
		}
    }
    
    
    /**
     * Refresh list with pressed up and down buttons and notify the gui to show them
     * @return error ... true, success ... false
     *      */
    public boolean refreshUpDownList()
    {
    	boolean error = false;
    	
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = 0;
		try {
			clocktickBeforeUpdate = sqBuilding.getClockTick();
		} catch (RemoteException e) {
			if(connected)
			{
				statusAlertContext.setStatus(GET_CLOCK_EXEC_TEXT);
			}
			error = true;
		}
    	
		error |= refreshUpList();
		error |= refreshDownList();
    	
    	long clocktickAftereUpdate = 0;
		try {
			clocktickAftereUpdate = sqBuilding.getClockTick();
		} catch (RemoteException e) {
			if(connected)
			{
				statusAlertContext.setStatus(GET_CLOCK_EXEC_TEXT);
			}
			error = true;
		}
		
    	// check, if clocktick of the sqelevator has changed in the meantime
		if(!error)
		{
			if(clocktickAftereUpdate != clocktickBeforeUpdate)
	    	{
				outOfSyncCounter++;
	    	}
	    	else
	    	{
	    		// everything is okay. Notify the Controller, that updownlist has been updated
	    		mainGuiController.update(floorModel, elevatorsList.get(selectedElevator));
	    		outOfSyncCounter = 0;
	    	}
		}
		
		return error;
    }
    
    /**
     * Refresh list with pressed up buttons
     * 
     * @return error ... true, success ... false
     * @throws RemoteException
     */
    public boolean refreshUpList()
    {
    	boolean error = false;
    	floorModel.clearUpButtonsList();
    	
    	// check the up buttons of the floors
    	for(int i = 0; i < buildingModel.getNumFloors(); i++)
    	{
    		// if button up is pressed, this button will be added to the list
    		boolean buttonPressed = false;
    		
    		try {
				buttonPressed = sqBuilding.getFloorButtonUp(i);
			} catch (RemoteException e) {
				if(connected)
				{
					statusAlertContext.setStatus("Exception in getFloorButtonUp of SQElevator");
				}
				error = true;
			}
    		
    		if(buttonPressed)
    		{
    			floorModel.addButtonUp(i);
    		}
    	}
    	
    	return error;
    }
    
    /**
     * Refresh list with pressed down buttons
     
     * @return error ... true, success ... false
     *      * @throws RemoteException
     */
    public boolean refreshDownList()
    {
    	boolean error = false;
    	floorModel.clearDownButtonsList();
    	
    	// Check the down buttons of the floors
    	for(int i = 0; i < buildingModel.getNumFloors(); i++)
    	{
    		// if down button is pressed in a floor, add id to the list
    		boolean buttonPressed = false;
    		
    		try {
    			buttonPressed = sqBuilding.getFloorButtonDown(i);
			} catch (RemoteException e) {
				if(connected)
				{
					statusAlertContext.setStatus("Exception in getFloorButtonDown of SQElevator");
				}
			}
    		
    		if(buttonPressed)
    		{
    			floorModel.addButtonDown(i);
    		}
    	}
    	
    	return error;
    }
    
    /**
     * Refresh whole content of an elevator
     * 
     * @param elevatorIdx - index of the elevator, that should be refreshed
     * @return error ... true, success ... false
     *      * @throws RemoteException
     */
    public boolean refreshElevator(int elevatorIdx)
    {
    	if(elevatorIdx < 0 || elevatorIdx >= elevatorsList.size())
    	{
    		throw new InvalidParameterException("index of elevator out of range");
    	}
    	boolean error = false;
    	
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = 0;
		try {
			clocktickBeforeUpdate = sqElevator.getClockTick();
		} catch (RemoteException e) {
			if(connected)
			{
				statusAlertContext.setStatus(GET_CLOCK_EXEC_TEXT);
			}
		}
    	
    	// store values to temp elevator. Necessary to do not overwrite the real elevator with corrupted data, if we are out of sync
    	IElevatorModel tempElevator = new ElevatorModel();
    	boolean validValues = true;
    	
    	// the old serviced floors must be copied to the updated elevator
    	tempElevator.setIgnoredFloors(elevatorsList.get(elevatorIdx).getIgnoredFloorsList());
    	
    	// refresh all fields in the elevator
    	error = refreshlevatorFields(tempElevator, elevatorIdx);
    	
    	// sanity checks
    	if(tempElevator.getTarget() > buildingModel.getNumFloors())
    	{
    		validValues = false;
    	}
    	if(tempElevator.getPosition() > buildingModel.getNumFloors())
    	{
    		validValues = false;
    	}
    	
    	// refresh stoplist
    	error |= refreshStopList(tempElevator, elevatorIdx);
    	
    	long clockTickAfterUpdate = 0;
    	try {
    		clockTickAfterUpdate = sqElevator.getClockTick();
		} catch (RemoteException e) {
			error = true;
			if(connected)
			{
				statusAlertContext.setStatus(GET_CLOCK_EXEC_TEXT);
			}
		}
    	
    	if(!error)
    	{
	    	// check, if clocktick of the sqelevator has changed in the meantime
	    	if(clockTickAfterUpdate != clocktickBeforeUpdate)
	    	{ 
	    		outOfSyncCounter++;
	    	}
	    	else if(!validValues) // sanity checks failes
	    	{
	    		if(connected)
	    		{
	    			statusAlertContext.setStatus("Sanity Check failed in UpdateData.refreshElevator()");
	    		}
	    	}
	    	else
	    	{
	    		// everything is okay. update the elevator and notify the gui, if this is the selected elevator
	    		elevatorsList.set(elevatorIdx, tempElevator);
	    		if(elevatorIdx == selectedElevator)
	    		{
	    			mainGuiController.update(floorModel, elevatorsList.get(selectedElevator));
	    		}
	    		outOfSyncCounter = 0;
	    	}
    	}
    	
    	return error;
    }
    
    
    private boolean refreshStopList(IElevatorModel elevator, int elevatorIdx)
    {
    	List<Integer> stops = new ArrayList<>();
    	elevator.setStops(stops);
    	boolean error = false;
    	
    	// get pressed stops for all floors
    	for(int i = 0; i < buildingModel.getNumFloors(); i++)
    	{
    		boolean buttonPressed = false;
    		// if stop button was pressed in this elevator, add it to the list
    		try {
				buttonPressed = sqElevator.getElevatorButton(elevatorIdx, i);
			} catch (RemoteException e) {
				error = true;
				if(connected)
				{
					statusAlertContext.setStatus("Exception in getTarget() of SQElevator 1");
				}
			}
    		
    		if(buttonPressed)
    		{
    			stops.add(i);
    		}
    	}
    	return error;
    }
    
    /**
     * Refresh all fields of this elevator
     * 
     * @param tempElevator temp elevator
     * @param elevatorIdx index of current updated elevator
     * @return
     */
    private Boolean refreshlevatorFields(IElevatorModel tempElevator, int elevatorIdx)
    {
    	boolean error = false;
    	int target = 0;
    	ElevatorDoorStatus doorStatus = ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED;
    	int position = 0;
    	int speed = 0;
    	int payload = 0;
    	ElevatorDirection direction = ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED;
    	
    	try{
    		target = sqElevator.getTarget(elevatorIdx);
    		doorStatus = sqElevator.getElevatorDoorStatus(elevatorIdx);
    		position = sqElevator.getElevatorFloor(elevatorIdx);
    		speed = sqElevator.getElevatorSpeed(elevatorIdx);
    		payload = sqElevator.getElevatorWeight(elevatorIdx);
    		direction = sqElevator.getCommittedDirection(elevatorIdx);
    	}
    	catch(RemoteException e){
    		error = true;
    		statusAlertContext.setStatus("Connection lost to Elevator Simulator");
    		connected = false;
    	}

    	

    	if(!error)
    	{
	    	tempElevator.setTarget(target);
	    	tempElevator.setDoors(doorStatus);
	    	tempElevator.setPosition(position);
	    	tempElevator.setSpeed(speed);
	    	tempElevator.setPayload(payload);
	    	tempElevator.setDirection(direction);
    	}
		return error;
    	
    }
    
    /**
     * Set new SqElevator and SqBuilding to use and reinitialize all components
     * @param b IBuildingWrapper
     * @param e IElevatorWrapper
     * @throws RemoteException 
     */
    public void setSqs(IBuildingWrapper b, IElevatorWrapper e) throws RemoteException {
    	if(b == null || e == null)
    	{
    		throw new NullPointerException("Nullpointer in UpdateData.setSqs");
    	}
		sqBuilding = b;
		sqElevator = e;
		initializeBuilding();
		initializeElevators();
		initializeServicedFloors();
		autoModeAlgorithmContext.init(buildingModel, elevatorsList, this);
		mainGuiController.reUpdate();
		lastTick = 0;
    }
    
    /**
     * Set ElevatorSimulator to use
     * @param elevator elevator to use
     * @throws RemoteException exception which can be thrown
     */
    public void setRMIs(IElevator elevator) throws RemoteException {
			ElevatorWrapper wrap = new ElevatorWrapper(elevator);
			setSqs(wrap, wrap);
    }
    

    /**
     * Reconnects to the RMI
     */
    public void reconnectRMI() {

	    IElevator elevator = rmiConnectionContext.getElevator();
	    
	    if(elevator != null) {
			try {
				setRMIs(elevator);
				statusAlertContext.setStatus("Connected to Elevator");
				connected = true;
			} catch (RemoteException e) {
				statusAlertContext.setStatus("No Elevator Connection");
				connected = false;
			}
	    }
	    else {
	    	connected = false;
	    }
	    
    }
    
    /**
     * returns the index of the current selected elevator
     */
    public int getSelectedElevator()
    {
    	return selectedElevator;
    }

    
    /**
     * Get the Out of sync counter
     * @return out of sync counter
     */
    public Integer getOutOfSyncCounter()
    {
    	return outOfSyncCounter;
    }
    
    /**
     * Get the Sqelevator
     * @return Sqelevator
     */
    public IElevatorWrapper getSqelevator()
    {
    	return sqElevator;
    }
    
    /**
     * Get the SqBuilding
     * @return SqBuilding
     */
    public IBuildingWrapper getSqBuilding()
    {
    	return sqBuilding;
    }
    
    /**
     * Get the CurrentTick
     * @return CurrentTick
     */
    public long getCurrentTick()
    {
    	return currentTick;
    }
    
    /**
     * @param val true, if connection should be active
     */
    public void setConnection(boolean val)
    {
    	connected = val;
    }
    
    /**
     * @return true, if connection should be active
     */
    public boolean getConnection()
    {
    	return connected;
    }
    
	private IElevatorWrapper sqElevator;
	private IBuildingWrapper sqBuilding;
	private IBuildingModel buildingModel;
	private IFloorModel floorModel;
	private List<IElevatorModel> elevatorsList;
	private MainGuiController mainGuiController;
	private int selectedElevator;
	StatusAlert statusAlertContext;
    private Integer outOfSyncCounter;
    private Integer criticalOutOfSyncValue = 5;
    private static final String GET_CLOCK_EXEC_TEXT = "Exception in getClockTick() of SQElevator";
    private AutoMode autoModeAlgorithmContext;
    private IRMIConnection rmiConnectionContext;
    private long lastTick = 0;
    private long currentTick = 0;
    private boolean connected;
}
