package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

public abstract class AutoMode {

	protected List<Integer> autoModeEnabledFloors = new ArrayList<Integer>();
	
	protected IBuildingModel Building;
	protected IFloorModel Floor;
	protected UpdateData Updater;
	protected List<IElevatorModel> Elevators;
	protected StatusAlert StatusAlert;
	
	
	/**
	 * Initialization for needed Objects
	 * @param building - the internal model of the building
	 * @param floor - the internal model for the floors
	 * @param elevators - the internal list with the models for the elevators
	 * @param statusAlert - status alert
	 * @throws RemoteException
	 */
	public void Init(IBuildingModel building, IFloorModel floor, List<IElevatorModel> elevators, StatusAlert statusAlert, UpdateData updater)
	{
		if(building == null || floor == null || elevators == null || statusAlert == null || updater == null)
		{
			throw new NullPointerException("Nullpointer in AutoMode!");
		}
		
		// assign models to the internal fields
		Building = building;
		Floor = floor;
		Elevators = elevators;
		StatusAlert = statusAlert;
	}
	
	
	public boolean enable(Integer floor) {
		if(!autoModeEnabledFloors.contains(floor)) {
			autoModeEnabledFloors.add(floor);
			return true;
		}
		return false;
	}
	public boolean disable(Integer floor) {
		if(autoModeEnabledFloors.contains(floor)) {
			autoModeEnabledFloors.remove(floor);	//if floor:int remove at index floor, if floor:Integer remove Object floor
			return true;
		}
		return false;
	}
	public boolean checkIfInAutoMode(Integer floor) {
		if(autoModeEnabledFloors.contains(floor)) {
			return true;
		}
		return false;
	}
	

	public void UpdateElevatorTargets() {
		for (int i = 0; i < Elevators.size(); i++) {
			Updater.setTarget(doGetNext(i), i);
		}
	}
	
	
	
	/**
	 * Replace with algorithm to get the next floor
	 * @param elevator number of elevator to update
	 * @return number of the next target floor
	 */
	protected abstract int doGetNext(int elevator);
}
