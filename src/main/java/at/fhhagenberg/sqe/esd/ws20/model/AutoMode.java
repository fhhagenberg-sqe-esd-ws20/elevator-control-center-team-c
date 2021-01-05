package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

public abstract class AutoMode {

	protected List<Integer> autoModeEnabledElevators = new ArrayList<Integer>();
	
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
		Updater = updater;
	}
	
	
	public boolean enable(Integer elevatorNr) {
		if(!autoModeEnabledElevators.contains(elevatorNr)) {
			autoModeEnabledElevators.add(elevatorNr);
			return true;
		}
		return false;
	}
	public boolean disable(Integer elevatorNr) {
		if(autoModeEnabledElevators.contains(elevatorNr)) {
			autoModeEnabledElevators.remove(elevatorNr);	//if floor:int remove at index floor, if floor:Integer remove Object floor
			return true;
		}
		return false;
	}
	public boolean checkIfInAutoMode(Integer elevatorNr) {
		if(autoModeEnabledElevators.contains(elevatorNr)) {
			return true;
		}
		return false;
	}
	

	public void UpdateElevatorTargets() {
		for (int i = 0; i < Elevators.size(); i++) {
			if(autoModeEnabledElevators.contains(i)) {
				Updater.setTarget(doGetNext(i), i);
			}	
		}
	}
	
	
	
	/**
	 * Replace with algorithm to get the next floor
	 * @param elevator number of elevator to update
	 * @return number of the next target floor
	 */
	protected abstract int doGetNext(int elevator);
}
