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
		
		initializeBuilding();

		
	}
	

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
	
	
	private IElevatorWrapper Sqelevator;
	private IBuildingWrapper SqBuilding;
	private IBuildingModel Building;
	private IFloorModel Floor;
	private List<IElevatorModel> Elevators;
	private MainGuiController GuiController;
	
	private int SelectedElevator;
	
	
    @Override
    public void run() {
        try {
        	System.out.println("getting Data from Simulator");
        	
        	//get current clocktick to guarantee atomar access
        	long clocktickBeforeUpdate = Sqelevator.getClockTick();
        	
        	//
        	//TODO: Alle Lifte hier updaten? Oder jeden Lift einzeln? Es gibt nur eine einzige Updatemethode
        	// Get parameters for all elevators
        	//.
        	//.
        	//.
        	// ready with reading params for the elevator
        	
        	
        	// check, if clocktick of the sqelevator has changed in the meantime
        	if(Sqelevator.getClockTick() != clocktickBeforeUpdate)
        	{
        		System.out.println("out of sync with the simulator");
        	}
        	else
        	{
        		// everything is okay. Notify the Controller, that the elevator has been updated
        		GuiController.update();
        	}

        	
        } catch (Exception ex) {
        }
    }
}
