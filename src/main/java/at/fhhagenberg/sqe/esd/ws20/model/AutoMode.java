package at.fhhagenberg.sqe.esd.ws20.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

public abstract class AutoMode {

	protected List<Integer> autoModeEnabledFloors = new ArrayList<Integer>();
	

	
	
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
	
	//TODO remove this and replace by real algo
	public abstract int doGetNext();
}
