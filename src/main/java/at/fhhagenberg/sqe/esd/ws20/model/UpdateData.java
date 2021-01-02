package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;


/**
 * @author Florian Atzenhofer (s1910567001)
 * @since 2020-12-31 09:10
 * 
 * Represents the updater, which refreshes all data of the elevator every 10 ms
 *
 */public class UpdateData extends TimerTask {

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
			MainGuiController guiController, StatusAlert statusAlert) throws RemoteException
	{
		// assign models to the internal fields
		SqBuilding = sqbuilding;
		Building = building;
		Floor = floor;
		Elevators = elevators;
		Sqelevator = sqelevator;
		GuiController = guiController;
		StatusAlert = statusAlert;
		
		initializeBuilding();
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
	 * Initializes every elevator in the building
	 * @throws RemoteException
	 */
	public void initializeElevators() throws RemoteException
	{
		//check if the number of stored elevators is the same as the number of 
		//elevators in the buildung
		if(Building.getNumElevators() != Elevators.size()) 
		{
			throw new RuntimeException("Numer of sored elevators not the same as elevators in the building!");
		}
		
		// get all servicefloors of each elevator and store them in a list
		for(int elevator = 0; elevator < Elevators.size(); elevator++)
		{
			for(int floor = 0; floor < Building.getNumFloors(); floor++)
			{
				if(!Sqelevator.getServicesFloors(elevator, floor))
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

        	
        } catch (Exception ex) {
        	StatusAlert.Status.set("Exception when getting values from SQelevator");
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
					StatusAlert.Status.set("Could not set Target " + floor + " for Elevator" + elevator);
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
				StatusAlert.Status.set("Could not set Target " + floor);
			}
    		GuiController.update(Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    
    /**
     * Refresh list with pressed up and down buttons and notify the gui to show them
     * 
     * @throws RemoteException
     */
    public void refreshUpDownList() throws RemoteException
    {
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = Sqelevator.getClockTick();
    	
    	refreshUpList();
    	refreshDownList();
    	
    	long clocktick = Sqelevator.getClockTick();
    	// check, if clocktick of the sqelevator has changed in the meantime
    	if(clocktick != clocktickBeforeUpdate)
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
    public void refreshUpList() throws RemoteException
    {
    	Floor.clearUpButtonsList();
    	
    	// check the up buttons of the floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if button up is pressed, this button will be added to the list
    		if(SqBuilding.getFloorButtonUp(i))
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
    public void refreshDownList() throws RemoteException
    {
    	Floor.clearDownButtonsList();
    	
    	// Check the down buttons of the floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if down button is pressed in a floor, add id to the list
    		if(SqBuilding.getFloorButtonDown(i))
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
    public void refreshElevator(int elevator_idx) throws RemoteException
    {
    	if(elevator_idx < 0 || elevator_idx >= Elevators.size())
    	{
    		throw new InvalidParameterException("index of elevator out of range");
    	}
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = Sqelevator.getClockTick();
    	
    	// store values to temp elevator. Necessary to do not overwrite the real elevator with corrupted data, if we are out of sync
    	IElevatorModel tempElevator = new ElevatorModel();
    	
    	// refresh all fields in the elevator
    	tempElevator.setTarget(Sqelevator.getTarget(elevator_idx));
    	tempElevator.setDoors(Sqelevator.getElevatorDoorStatus(elevator_idx));
    	tempElevator.setPosition(Sqelevator.getElevatorFloor(elevator_idx));
    	tempElevator.setSpeed(Sqelevator.getElevatorSpeed(elevator_idx));
    	tempElevator.setPayload(Sqelevator.getElevatorWeight(elevator_idx));
    	tempElevator.setDirection(Sqelevator.getCommittedDirection(elevator_idx));
    	
    	// refresh stoplist
    	List<Integer> Stops = new ArrayList<Integer>();
    	tempElevator.setStops(Stops);
    	
    	// get pressed stops for all floors
    	for(int i = 0; i < Building.getNumFloors(); i++)
    	{
    		// if stop button was pressed in this elevator, add it to the list
    		if(Sqelevator.getElevatorButton(elevator_idx, i))
    		{
    			Stops.add(i);
    		}
    	}
    	
    	
    	// check, if clocktick of the sqelevator has changed in the meantime
    	if(Sqelevator.getClockTick() != clocktickBeforeUpdate)
    	{
    		StatusAlert.Status.set("Out of sync with the simulator when getting updownlist");
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
    
    
	private IElevatorWrapper Sqelevator;
	private IBuildingWrapper SqBuilding;
	private IBuildingModel Building;
	private IFloorModel Floor;
	private List<IElevatorModel> Elevators;
	private MainGuiController GuiController;
	private int SelectedElevator;
	StatusAlert StatusAlert;
    
    
    
}
