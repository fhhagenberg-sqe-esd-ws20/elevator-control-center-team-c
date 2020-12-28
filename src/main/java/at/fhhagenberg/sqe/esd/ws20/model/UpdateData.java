package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;
import java.util.TimerTask;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;


public class UpdateData extends TimerTask {

	public UpdateData(IElevatorWrapper sqelevator, IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators, MainGuiController guiController)
	{
		Building = building;
		Floor = floor;
		Elevators = elevators;
		Sqelevator = sqelevator;
		GuiController = guiController;
		
		// TODO: Daten, die sich nur einmal ändern (Anzahl der Lifte, ServicesFloors, ... hier einmalig im Konstruktor zuweisen?)
	}
	
	
	private IElevatorWrapper Sqelevator;
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
