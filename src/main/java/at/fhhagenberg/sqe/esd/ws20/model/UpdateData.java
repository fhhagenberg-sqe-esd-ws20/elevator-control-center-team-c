package at.fhhagenberg.sqe.esd.ws20.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;
import sqelevator.IElevator;


/**
 * @author Florian Atzenhofer (s1910567001)
 * @since 2020-12-31 09:10
 * 
 * Represents the updater, which refreshes all data of the elevator every 10 ms
 *
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
	public UpdateData(IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators, MainGuiController guiController, StatusAlert statusAlert, AutoMode autoModeAlgorithm)
	{
		
		//sqbuilding, sqelevator can be null -> next update cycle a reconnect will be done
		if(building == null || floor == null || elevators == null || guiController == null || statusAlert == null || autoModeAlgorithm == null)
		{
			throw new NullPointerException("Nullpointer in UpdateData!");
		}
		
		// assign models to the internal fields
		Building = building;
		Floor = floor;
		Elevators = elevators;
		GuiController = guiController;
		StatusAlert = statusAlert;
		AutoModeAlgorithm = autoModeAlgorithm;
		OutOfSyncCounter = 0;
	}
	


	/** 
	 * Initializes all constant properties of the building
	 * 
	 * @throws RemoteException
	 */
	public void initializeBuilding() throws RemoteException
	{
		// get number of floors and number of Elevators from the building
		Building.setNumFloors(SqBuilding.getFloorNum());
		Building.setNumElevators(SqBuilding.getElevatorNum());
		OutOfSyncCounter = 0;
	}
	
	/** 
	 * Set service floor for each elevator in the building
	 * @throws RemoteException
	 */
	public void initializeServicedFloors()
	{
		//check if the number of stored elevators is the same as the number of 
		//elevators in the building
		if(Building.getNumElevators() != Elevators.size()) 
		{
			throw new InvalidParameterException("Numer of stored elevators not the same as number of elevators in the building!");
		}
		
		
		// get all servicefloors of each elevator and store them in a list
		for(int elevator = 0; elevator < Elevators.size(); elevator++)
		{
			Elevators.get(elevator).clearIgnoredFloorsList();
			
			for(int floor = 0; floor < Building.getNumFloors(); floor++)
			{
				boolean floor_serviced = false;
				try {
					floor_serviced = Sqelevator.getServicesFloors(elevator, floor);
				} catch (RemoteException e) {
					StatusAlert.setStatus("Exception in getServicesFloors() of SQElevator with floor " + floor + " and elevator " + elevator);
				}
				
				if(floor_serviced)
				{
					Elevators.get(elevator).addIgnoredFloor(floor);
				}
			}
		}
		OutOfSyncCounter = 0;
	}
	
	
	/** 
	 * Initializes all elevators
	 * @throws RemoteException
	 */
	public void initializeElevators()
	{
		Elevators.clear();
        for(int i = 0; i < Building.getNumElevators(); i++)
        {
        	Elevators.add(new ElevatorModel());
        }
	}
	
	
    /**
     * Periodic task refreshes properties of the elevator periodically
     */
    @Override
    public void run() {
        try {
        	
        	if(Sqelevator != null && SqBuilding != null) {
        	
            	boolean error = false;
            	// refresh list with the up and down buttons of the floors
            	error |= refreshUpDownList();
            	
            	// refresh the fields of all elevators
            	for(int i = 0; i < Elevators.size(); i++)
            	{
            		error |= refreshElevator(i);
            	}
            	
            	if(OutOfSyncCounter > CriticalOutOfSyncValue)
            	{
    	    		StatusAlert.setStatus("Out of sync with the simulator. We are to slow with polling values from the Elevator Interface. "
    	    				+ "Current timestamp = "
    	    				+ SqBuilding.getClockTick());
            	}

            	if(error) {
            		//try to reinitialize rmi
            		ReconnectRMI();
            	}
        		
        	}
        	else 
        	{
        		//try to reinitialize rmi
        		ReconnectRMI();
        	}
        	

        //catch everything else that was not dealt with before, therefore this message is more verbose
        } 
        catch (Exception ex) {
        	StatusAlert.setStatus("Exception when getting values from SQelevator: " + ex.getClass() + ": " + ex.getLocalizedMessage());
        }
    }
    

    /**
     * Set the index of the elevator, which is currently selected in the view
     * 
     * @param elevatorIdx - index of the elevator, which is selected in the GUI
     */
    public void setSelectedElevator(int elevatorIdx)
    {
    	if(elevatorIdx >= 0 && elevatorIdx < Elevators.size())
		{
			SelectedElevator = elevatorIdx;
			GuiController.update(Floor, Elevators.get(SelectedElevator));
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
    	if(floor >= 0 && floor < Building.getNumFloors())
		{
			if(elevator >= 0 && elevator < Elevators.size())
			{
				Elevators.get(elevator).setTarget(floor);
				
				// set new target for SQElevator
				try {
					Sqelevator.setTarget(elevator, floor);
				} catch (RemoteException e) {
					StatusAlert.setStatus("Exception in setTarget of SQElevator with floor: " + floor + " for Elevator" + elevator);
				}
				
				if(elevator == SelectedElevator)
				{
					GuiController.update(Floor, Elevators.get(SelectedElevator));
				}
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
    	if(floor >= 0 && floor < Building.getNumFloors())
    	{
    		Elevators.get(SelectedElevator).setTarget(floor);
    		
			// set new target for SQElevator
			try {
				Sqelevator.setTarget(SelectedElevator, floor);
			} catch (RemoteException e) {
				StatusAlert.setStatus("Exception in setTarget of SQElevator with floor: " + floor);
			}
    		GuiController.update(Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    
    /**
     * Refresh list with pressed up and down buttons and notify the gui to show them
     * @return success ... true, false ... error
     */
    public boolean refreshUpDownList()
    {
    	boolean error = false;
    	
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = 0;
		try {
			clocktickBeforeUpdate = SqBuilding.getClockTick();
		} catch (RemoteException e) {
			StatusAlert.setStatus(GetClockExecText);
			error = true;
		}
    	
		error |= refreshUpList();
		error |= refreshDownList();
    	
    	long clocktickAftereUpdate = 0;
		try {
			clocktickAftereUpdate = SqBuilding.getClockTick();
		} catch (RemoteException e) {
			StatusAlert.setStatus(GetClockExecText);
			error = true;
		}
		
    	// check, if clocktick of the sqelevator has changed in the meantime
		if(!error)
		{
			if(clocktickAftereUpdate != clocktickBeforeUpdate)
	    	{
				OutOfSyncCounter++;
	    	}
	    	else
	    	{
	    		// everything is okay. Notify the Controller, that updownlist has been updated
	    		GuiController.update(Floor, Elevators.get(SelectedElevator));
	    		OutOfSyncCounter = 0;
	    	}
		}
		
		return error;
    }
    
    /**
     * Refresh list with pressed up buttons
     * 
     * @return success ... true, false ... error
     * @throws RemoteException
     */
    public boolean refreshUpList()
    {
    	boolean error = false;
    	Floor.clearUpButtonsList();
    	
    	// check the up buttons of the floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if button up is pressed, this button will be added to the list
    		boolean button_pressed = false;
    		
    		try {
				button_pressed = SqBuilding.getFloorButtonUp(i);
			} catch (RemoteException e) {
				StatusAlert.setStatus("Exception in getFloorButtonUp of SQElevator");
				error = true;
			}
    		
    		if(button_pressed)
    		{
    			Floor.addButtonUp(i);
    		}
    	}
    	
    	return error;
    }
    
    /**
     * Refresh list with pressed down buttons
     
     * @return success ... true, false ... error
     * @throws RemoteException
     */
    public boolean refreshDownList()
    {
    	boolean error = false;
    	Floor.clearDownButtonsList();
    	
    	// Check the down buttons of the floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if down button is pressed in a floor, add id to the list
    		boolean button_pressed = false;
    		
    		try {
				button_pressed = SqBuilding.getFloorButtonDown(i);
			} catch (RemoteException e) {
				StatusAlert.setStatus("Exception in getFloorButtonDown of SQElevator");
			}
    		
    		if(button_pressed)
    		{
    			Floor.addButtonDown(i);
    		}
    	}
    	
    	return error;
    }
    
    /**
     * Refresh whole content of an elevator
     * 
     * @param elevator_idx - index of the elevator, that should be refreshed
     * @return success ... true, false ... error
     * @throws RemoteException
     */
    public boolean refreshElevator(int elevator_idx)
    {
    	if(elevator_idx < 0 || elevator_idx >= Elevators.size())
    	{
    		throw new InvalidParameterException("index of elevator out of range");
    	}
    	Boolean error = false;
    	
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = 0;
		try {
			clocktickBeforeUpdate = Sqelevator.getClockTick();
		} catch (RemoteException e) {
			StatusAlert.setStatus(GetClockExecText);
		}
    	
    	// store values to temp elevator. Necessary to do not overwrite the real elevator with corrupted data, if we are out of sync
    	IElevatorModel tempElevator = new ElevatorModel();
    	Boolean validValues = true;
    	
    	// the old serviced floors must be copied to the updated elevator
    	tempElevator.setIgnoredFloors(Elevators.get(elevator_idx).getIgnoredFloorsList());
    	
    	// refresh all fields in the elevator
    	error = refreshlevatorFields(tempElevator, elevator_idx);
    	
    	// sanity checks
    	if(tempElevator.getTarget() > Building.getNumFloors())
    	{
    		validValues = false;
    	}
    	if(tempElevator.getPosition() > Building.getNumFloors())
    	{
    		validValues = false;
    	}    	
    	
    	// refresh stoplist
    	List<Integer> Stops = new ArrayList<Integer>();
    	tempElevator.setStops(Stops);
    	
    	// get pressed stops for all floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		boolean button_pressed = false;
    		// if stop button was pressed in this elevator, add it to the list
    		try {
				button_pressed = Sqelevator.getElevatorButton(elevator_idx, i);
			} catch (RemoteException e) {
				error = true;
				StatusAlert.setStatus("Exception in getTarget() of SQElevator 1");
			}
    		
    		if(button_pressed)
    		{
    			Stops.add(i);
    		}
    	}
    	
    	long clockTickAfterUpdate = 0;
    	try {
    		clockTickAfterUpdate = Sqelevator.getClockTick();
		} catch (RemoteException e) {
			error = true;
			StatusAlert.setStatus(GetClockExecText);
		}
    	
    	if(!error)
    	{
	    	// check, if clocktick of the sqelevator has changed in the meantime
	    	if(clockTickAfterUpdate != clocktickBeforeUpdate)
	    	{ 
	    		OutOfSyncCounter++;
	    	}
	    	else if(validValues == false) // sanity checks failes
	    	{
	    		StatusAlert.setStatus("Sanity Check failed in UpdateData.refreshElevator()");
	    	}
	    	else
	    	{
	    		// everything is okay. update the elevator and notify the gui, if this is the selected elevator
	    		Elevators.set(elevator_idx, tempElevator);
	    		if(elevator_idx == SelectedElevator)
	    		{
	    			GuiController.update(Floor, Elevators.get(SelectedElevator));
	    		}
	    		OutOfSyncCounter = 0;
	    	}
    	}
    	
    	return error;
    }
    
    /**
     * Refresh all fields of this elevator
     * 
     * @param tempElevator temp elevator
     * @param elevator_idx index of current updated elevator
     * @return
     */
    private Boolean refreshlevatorFields(IElevatorModel tempElevator, int elevator_idx)
    {
    	Boolean error = false;
    	int target = 0;
    	ElevatorDoorStatus door_status = ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED;
    	int position = 0;
    	int speed = 0;
    	int payload = 0;
    	ElevatorDirection direction = ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED;
    	
    	try{
    		target = Sqelevator.getTarget(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.setStatus("Exception in getTarget() of SQElevator");
    	}
    	try{
    		door_status = Sqelevator.getElevatorDoorStatus(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.setStatus("Exception in getElevatorDoorStatus() of SQElevator");
    	}
    	try{
    		position = Sqelevator.getElevatorFloor(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.setStatus("Exception in getElevatorFloor() of SQElevator");
    	}
    	try{
    		speed = Sqelevator.getElevatorSpeed(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.setStatus("Exception in getElevatorSpeed() of SQElevator");
    	}
    	try{
    		payload = Sqelevator.getElevatorWeight(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.setStatus("Exception in getElevatorWeight() of SQElevator");
    	}
    	try{
    		direction = Sqelevator.getCommittedDirection(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.setStatus("Exception in getCommittedDirection() of SQElevator");
    	}
    	

    	if(error == false)
    	{
	    	tempElevator.setTarget(target);
	    	tempElevator.setDoors(door_status);
	    	tempElevator.setPosition(position);
	    	tempElevator.setSpeed(speed);
	    	tempElevator.setPayload(payload);
	    	tempElevator.setDirection(direction);
    	}
		return error;
    	
    }
    
    
    
    /**
     * returns the index of the current selected elevator
     */
    public int getSelectedElevator()
    {
    	return SelectedElevator;
    }
    
    
    
    public void ReconnectRMI() {

	    IElevator elevator = null;
		try {
			elevator = (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
			SetRMIs(elevator);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			StatusAlert.setStatus("No Elevator Connection");
		}
    }
    
    public void SetRMIs(IElevator elevator) throws RemoteException {
			ElevatorWrapper wrap = new ElevatorWrapper(elevator);
			SetSqs(wrap, wrap);
    }
    
    public void SetSqs(IBuildingWrapper b, IElevatorWrapper e) throws RemoteException {
	    	if(b == null || e == null)
	    	{
	    		throw new NullPointerException("Nullpointer in UpdateData.setSqs");
	    	}
			SqBuilding = b;
			Sqelevator = e;
			StatusAlert.setStatus("Connected to Elevator");
			initializeBuilding();
			initializeElevators();
			initializeServicedFloors();
			AutoModeAlgorithm.Init(Building, Elevators, this);
			GuiController.reUpdate();
    }
    
    public Integer GetOutOfSyncCounter()
    {
    	return OutOfSyncCounter;
    }
    
    
	private IElevatorWrapper Sqelevator;
	private IBuildingWrapper SqBuilding;
	private IBuildingModel Building;
	private IFloorModel Floor;
	private List<IElevatorModel> Elevators;
	private MainGuiController GuiController;
	private int SelectedElevator;
	StatusAlert StatusAlert;
    private Integer OutOfSyncCounter;
    private Integer CriticalOutOfSyncValue = 5;
    private final String GetClockExecText = "Exception in getClockTick() of SQElevator";
    private AutoMode AutoModeAlgorithm;
    
}
