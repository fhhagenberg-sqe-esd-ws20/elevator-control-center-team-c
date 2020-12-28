package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;
import java.util.TimerTask;


public class UpdateData extends TimerTask {

	public UpdateData(IElevatorWrapper sqelevator, IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators)
	{
		Building = building;
		Floor = floor;
		Elevators = elevators;
		Sqelevator = sqelevator;
	}
	
	
	private IElevatorWrapper Sqelevator;
	private IBuildingModel Building;
	private IFloorModel Floor;
	private List<IElevatorModel> Elevators;
	
	
    @Override
    public void run() {
        try {
        	System.out.println("getting Data from Simulator");
        	
        	
        	
            // updateEnvironment();
            // updateElevators();
            // updateFloors();
        } catch (Exception ex) {
        }
    }
}
