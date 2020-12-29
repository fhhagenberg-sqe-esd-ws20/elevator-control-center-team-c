package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IBuildingModel {

	public void AddServiceFloor(int floorNumber);
	
	void SetNumElevators(int numElevators);

	void SetNumFloors(int numFloors);

	void SetServiceFloors(List<Integer> serviceFloors);

	int GetNumElevators();

	int GetNumFloors();

	List<Integer> GetServiceFloors();

}