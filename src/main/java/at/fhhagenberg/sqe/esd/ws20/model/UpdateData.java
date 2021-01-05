package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;


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
	 * @param sqbuilding - the simulator for the building
	 * @param sqelevator - the simulator for the elevators
	 * @param building - the internal model of the building
	 * @param floor - the internal model for the floors
	 * @param elevators - the internal list with the models for the elevators
	 * @param guiController - the controller, which controls the gui
	 * @throws RemoteException
	 */
	public UpdateData(IBuildingWrapper sqbuilding, IElevatorWrapper sqelevator,IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators, 
			MainGuiController guiController, StatusAlert statusAlert)
	{
		
		if(sqbuilding == null || building == null || floor == null 
				|| elevators == null || sqelevator == null || guiController == null || statusAlert == null )
		{
			throw new NullPointerException("Nullpointer in UpdateData!");
		}
		
		// assign models to the internal fields
		SqBuilding = sqbuilding;
		Building = building;
		Floor = floor;
		Elevators = elevators;
		Sqelevator = sqelevator;
		GuiController = guiController;
		StatusAlert = statusAlert;
		
		try
		{
			initializeBuilding();
		}
		catch(RemoteException e)
		{
			StatusAlert.Status.set("Exception in initializeBuilding of UpdateData()");
			e.printStackTrace();
		}
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
			throw new RuntimeException("Numer of stored elevators not the same as number of elevators in the building!");
		}
		
		// get all servicefloors of each elevator and store them in a list
		for(int elevator = 0; elevator < Elevators.size(); elevator++)
		{
			for(int floor = 0; floor < Building.getNumFloors(); floor++)
			{
				boolean floor_serviced = false;
				try {
					floor_serviced = Sqelevator.getServicesFloors(elevator, floor);
				} catch (RemoteException e) {
					StatusAlert.Status.set("Exception in getServicesFloors() of SQElevator with floor " + floor + " and elevator " + elevator);
					e.printStackTrace();
				}
				
				if(floor_serviced)
				{
					Elevators.get(elevator).addIgnoredFloor(floor);
				}
			}
		}
	}
	
	
    /**
     * Periodic task refreshes properties of the elevator periodically
     */
    @Override
    public void run() {
        try {
        	// refresh list with the up and down buttons of the floors
        	refreshUpDownList();
        	
        	// refresh the fields of all elevators
        	for(int i = 0; i < Elevators.size(); i++)
        	{
        		refreshElevator(i);
        	}

        //catch everything else that was not dealt with before, therefore this message is more verbose
        } catch (Exception ex) {
        	StatusAlert.Status.set("Exception when getting values from SQelevator: " + ex.getClass() + ": " + ex.getLocalizedMessage());
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
    	if(floor >= 0 && floor <= Building.getNumFloors())
		{
			if(elevator >= 0 && elevator < Elevators.size())
			{
				Elevators.get(elevator).setTarget(floor);
				
				// set new target for SQElevator
				try {
					Sqelevator.setTarget(elevator, floor);
				} catch (RemoteException e) {
					StatusAlert.Status.set("Exception in setTarget of SQElevator with floor: " + floor + " for Elevator" + elevator);
					e.printStackTrace();
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
    	if(floor >= 0 && floor <= Building.getNumFloors())
    	{
    		Elevators.get(SelectedElevator).setTarget(floor);
    		
			// set new target for SQElevator
			try {
				Sqelevator.setTarget(SelectedElevator, floor);
			} catch (RemoteException e) {
				StatusAlert.Status.set("Exception in setTarget of SQElevator with floor: " + floor);
				e.printStackTrace();
			}
    		GuiController.update(Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    
    /**
     * Refresh list with pressed up and down buttons and notify the gui to show them
     * 
     * @throws RemoteException
     */
    public void refreshUpDownList()
    {
    	boolean error = false;
    	
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = 0;
		try {
			clocktickBeforeUpdate = SqBuilding.getClockTick();
		} catch (RemoteException e) {
			StatusAlert.Status.set("Exception in getClockTick() of SQElevator");
			e.printStackTrace();
			error = true;
		}
    	
    	refreshUpList();
    	refreshDownList();
    	
    	long clocktick = 0;
		try {
			clocktick = SqBuilding.getClockTick();
		} catch (RemoteException e) {
			e.printStackTrace();
			error = true;
		}
		
    	// check, if clocktick of the sqelevator has changed in the meantime
		if(error)
		{
			StatusAlert.Status.set("Exception in getClockTick() of SQElevator");
		}
		else if(clocktick != clocktickBeforeUpdate)
    	{
    		StatusAlert.Status.set("Out of sync with the simulator at clocktick " + clocktick);
    	}
    	else
    	{
    		// everything is okay. Notify the Controller, that updownlist has been updated
    		GuiController.update(Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    /**
     * Refresh list with pressed up buttons
     * 
     * @throws RemoteException
     */
    public void refreshUpList()
    {
    	Floor.clearUpButtonsList();
    	
    	// check the up buttons of the floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if button up is pressed, this button will be added to the list
    		boolean button_pressed = false;
    		
    		try {
				button_pressed = SqBuilding.getFloorButtonUp(i);
			} catch (RemoteException e) {
				StatusAlert.Status.set("Exception in getFloorButtonUp of SQElevator");
				e.printStackTrace();
			}
    		
    		if(button_pressed)
    		{
    			Floor.addButtonUp(i);
    		}
    	}
    }
    
    /**
     * Refresh list with pressed down buttons
     * 
     * @throws RemoteException
     */
    public void refreshDownList()
    {
    	Floor.clearDownButtonsList();
    	
    	// Check the down buttons of the floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if down button is pressed in a floor, add id to the list
    		boolean button_pressed = false;
    		
    		try {
				button_pressed = SqBuilding.getFloorButtonDown(i);
			} catch (RemoteException e) {
				StatusAlert.Status.set("Exception in getFloorButtonDown of SQElevator");
				e.printStackTrace();
			}
    		
    		if(button_pressed)
    		{
    			Floor.addButtonDown(i);
    		}
    	}
    }
    
    /**
     * Refresh whole content of an elevator
     * 
     * @param elevator_idx - index of the elevator, that should be refreshed
     * @throws RemoteException
     */
    public void refreshElevator(int elevator_idx)
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
			error = true;
			StatusAlert.Status.set("Exception in getClockTick() of SQElevator");
			e.printStackTrace();
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
				StatusAlert.Status.set("Exception in getTarget() of SQElevator");
				e.printStackTrace();
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
			StatusAlert.Status.set("Exception in getClockTick() of SQElevator");
			e.printStackTrace();
		}
    	
    	if(!error)
    	{
	    	// check, if clocktick of the sqelevator has changed in the meantime
	    	if(clockTickAfterUpdate != clocktickBeforeUpdate)
	    	{
	    		StatusAlert.Status.set("Out of sync with the simulator when getting updownlist");
	    	}
	    	else if(validValues == false) // sanity checks failes
	    	{
	    		StatusAlert.Status.set("Sanity Check failed in UpdateData.refreshElevator()");
	    	}
	    	else
	    	{
	    		// everything is okay. update the elevator and notify the gui, if this is the selected elevator
	    		Elevators.set(elevator_idx, tempElevator);
	    		if(elevator_idx == SelectedElevator)
	    		{
	    			GuiController.update(Floor, Elevators.get(SelectedElevator));
	    		}
	    	}
    	}
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
    		StatusAlert.Status.set("Exception in getTarget() of SQElevator");
    		e.printStackTrace();
    	}
    	try{
    		door_status = Sqelevator.getElevatorDoorStatus(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.Status.set("Exception in getElevatorDoorStatus() of SQElevator");
    		e.printStackTrace();
    	}
    	try{
    		position = Sqelevator.getElevatorFloor(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.Status.set("Exception in getElevatorFloor() of SQElevator");
    		e.printStackTrace();
    	}    	
    	try{
    		speed = Sqelevator.getElevatorSpeed(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.Status.set("Exception in getElevatorSpeed() of SQElevator");
    		e.printStackTrace();
    	}
    	try{
    		payload = Sqelevator.getElevatorWeight(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.Status.set("Exception in getElevatorWeight() of SQElevator");
    		e.printStackTrace();
    	}
    	try{
    		direction = Sqelevator.getCommittedDirection(elevator_idx);
    	}
    	catch(RemoteException e){
    		error = true;
    		StatusAlert.Status.set("Exception in getCommittedDirection() of SQElevator");
    		e.printStackTrace();
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
    
    
	private IElevatorWrapper Sqelevator;
	private IBuildingWrapper SqBuilding;
	private IBuildingModel Building;
	private IFloorModel Floor;
	private List<IElevatorModel> Elevators;
	private MainGuiController GuiController;
	private int SelectedElevator;
	StatusAlert StatusAlert;
    
    
    
}
