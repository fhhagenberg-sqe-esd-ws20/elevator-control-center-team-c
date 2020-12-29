package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.util.List;
import java.util.TimerTask;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.ElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;


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
		
		//TODO: Activate this line, when mock is used for the elevatorinterface
		//initializeBuilding();
	}
	

    /**
     * Initializes all constant properties of the building
     */
	public void initializeBuilding() throws RemoteException
	{
		// get number of floors and number of Elevators from the building
		Building.SetNumFloors(SqBuilding.getFloorNum());
		Building.SetNumElevators(SqBuilding.getElevatorNum());
		
		// get all servicefloors of each elevator and store them in a list, which contains all service floors of the whole building
		for(int elevator = 0; elevator < Elevators.size(); elevator++)
		{
			for(int floor = 0; floor < Building.GetNumFloors(); floor++)
			{
				if(Sqelevator.getServicesFloors(elevator, floor))
				{
					Building.AddServiceFloor(floor);
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
        	

        	
        } catch (Exception ex) {
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
				IElevatorModel el = Elevators.get(elevator);
				el.AddStop(floor);
			}
		}
    }
    
    
    //TODO: Was soll diese Funktion machen?
    /**
     * 
     */
    public void setTarget(int floor)
    {

    }
    
    
    /**
     * Refresh list with pressed up and down buttons and notify the gui to show them
     */
    public void refreshUpDownList() throws RemoteException
    {
    	//get current clocktick to guarantee atomar access
    	long clocktickBeforeUpdate = Sqelevator.getClockTick();
    	
    	
    	
    	
    	
    	// check, if clocktick of the sqelevator has changed in the meantime
    	if(Sqelevator.getClockTick() != clocktickBeforeUpdate)
    	{
    		System.out.println("out of sync with the simulator");
    	}
    	else
    	{
    		// everything is okay. Notify the Controller, that updownlist has been updated
    		GuiController.update(Building, Floor, Elevators.get(SelectedElevator));
    	}
    }
    
    /**
     * Refresh list with pressed up buttons
     */
    public void refreshUpList() throws RemoteException
    {
    	for(int i = 0; i < Building.GetNumFloors(); i++)
    	{
    		
    	}
    	
    }
    
    /**
     * Refresh list with pressed down buttons
     */
    public void refreshDownList() throws RemoteException
    {
    	
    	
    }
    
    
	private IElevatorWrapper Sqelevator;
	private IBuildingWrapper SqBuilding;
	private IBuildingModel Building;
	private IFloorModel Floor;
	private List<IElevatorModel> Elevators;
	private MainGuiController GuiController;
	private int SelectedElevator;
    
    
    
}
