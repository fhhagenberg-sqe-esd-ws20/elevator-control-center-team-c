package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;


//TODO: update javadoc
public class UpdateData extends TimerTask {

	public UpdateData(IBuildingWrapper sqbuilding, IElevatorWrapper sqelevator,IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators, 
			MainGuiController guiController) throws RemoteException
	{
		// assign models to the internal fields
		SqBuilding = sqbuilding;
		Building = building;
		Floor = floor;
		Elevators = elevators;
		Sqelevator = sqelevator;
		GuiController = guiController;
		
		initializeBuilding();
	}
	


	/** 
	 * Initializes all constant properties of the building
	 * @throws RemoteException
	 */
	public void initializeBuilding() throws RemoteException
	{
		// get number of floors and number of Elevators from the building
		Building.SetNumFloors(SqBuilding.getFloorNum());
		Building.SetNumElevators(SqBuilding.getElevatorNum());
	}
	
	/** 
	 * Initializes every elevator in the building
	 * @throws RemoteException
	 */
	public void initializeElevators() throws RemoteException
	{
		//check if the number of stored elevators is the same as the number of 
		//elevators in the buildung
		if(Building.GetNumElevators() != Elevators.size()) 
		{
			throw new RuntimeException("Numer of sored elevators not the same as elevators in the building!");
		}
		
		// get all servicefloors of each elevator and store them in a list
		for(int elevator = 0; elevator < Elevators.size(); elevator++)
		{
			for(int floor = 0; floor < Building.GetNumFloors(); floor++)
			{
				if(Sqelevator.getServicesFloors(elevator, floor))
				{
					Elevators.get(elevator).AddServiceFloor(floor);
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
        	System.out.println("Getting Data from Simulator");
        	
        	// refresh list with the up and down buttons of the floors
        	refreshUpDownList();
        	
        	// refresh the fields of all elevators
        	for(int i = 0; i < Elevators.size(); i++)
        	{
        		refreshElevator(i);
        	}

        	
        } catch (Exception ex) {
        	//TODO: Statusmessage hier printen, oder kapseln wir das?
        	System.out.println("Exception when getting values from SQelevator");
        }
    }
    
    /**
     * Set the index of the elevator, which is currently selected in the view
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
     * Stop was pressed in an elevator. The Target will be added to the targetlist of the elevator
     */
    public void setTarget(int floor, int elevator)
    {
    	if(floor >= 0 && floor <= Building.GetNumFloors())
		{
			if(elevator >= 0 && elevator < Elevators.size())
			{
				Elevators.get(elevator).SetTarget(floor);
				
				// set new target for SQElevator
				try {
					Sqelevator.setTarget(elevator, floor);
				} catch (RemoteException e) {
					// TODO Update status here?
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
     * Press stop Button at the current selected elevator
     */
    public void setTarget(int floor)
    {
    	if(floor >= 0 && floor <= Building.GetNumFloors())
    	{
    		Elevators.get(SelectedElevator).SetTarget(floor);
    		
			// set new target for SQElevator
			try {
				Sqelevator.setTarget(SelectedElevator, floor);
			} catch (RemoteException e) {
				// TODO Update status here?
				e.printStackTrace();
			}
    		GuiController.update(Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    
    /**
     * Refresh list with pressed up and down buttons and notify the gui to show them
     */
    public void refreshUpDownList() throws RemoteException
    {
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = Sqelevator.getClockTick();
    	
    	refreshUpList();
    	refreshDownList();
    	
    	// check, if clocktick of the sqelevator has changed in the meantime
    	if(Sqelevator.getClockTick() != clocktickBeforeUpdate)
    	{
    		System.out.println("out of sync with the simulator");
    	}
    	else
    	{
    		// everything is okay. Notify the Controller, that updownlist has been updated
    		GuiController.update(Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    /**
     * Refresh list with pressed up buttons
     */
    public void refreshUpList() throws RemoteException
    {
    	Floor.ClearUps();
    	
    	// check the up buttons of the floors
    	for(int i = 0; i < Building.GetNumFloors(); i++)
    	{
    		// if button up is pressed, this button will be added to the list
    		if(SqBuilding.getFloorButtonUp(i))
    		{
    			Floor.AddUp(i);
    		}
    	}
    }
    
    /**
     * Refresh list with pressed down buttons
     */
    public void refreshDownList() throws RemoteException
    {
    	Floor.ClearDowns();
    	
    	// Check the down buttons of the floors
    	for(int i = 0; i < Building.GetNumFloors(); i++)
    	{
    		// if down button is pressed in a floor, add id to the list
    		if(SqBuilding.getFloorButtonDown(i))
    		{
    			Floor.AddDown(i);
    		}
    	}
    }
    
    /**
     * Refresh whole content of an elevator
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
    	tempElevator.SetTarget(Sqelevator.getTarget(elevator_idx));
    	tempElevator.SetDoors(Sqelevator.getElevatorDoorStatus(elevator_idx));
    	tempElevator.SetPosition(Sqelevator.getElevatorFloor(elevator_idx));
    	tempElevator.SetSpeed(Sqelevator.getElevatorSpeed(elevator_idx));
    	tempElevator.SetPayload(Sqelevator.getElevatorWeight(elevator_idx));
    	tempElevator.SetDirection(Sqelevator.getCommittedDirection(elevator_idx));
    	
    	// refresh stoplist
    	List<Integer> Stops = new ArrayList<Integer>();
    	tempElevator.SetStops(Stops);
    	
    	// get pressed stops for all floors
    	for(int i = 0; i < Building.GetNumFloors(); i++)
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
    		System.out.println("Out of sync with the simulator when getting updownlist");
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
    
    
    
}
