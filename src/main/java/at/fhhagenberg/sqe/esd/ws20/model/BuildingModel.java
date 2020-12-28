package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public class BuildingModel implements IBuildingModel {

	
	public void SetNumElevators(int numElevators)
	{
		NumElevators = numElevators;
	}
	
	public void SetNumFloors(int numFloors)
	{
		NumFloors = numFloors;
	}
	
	public void SetServiceFloors(List<Integer> serviceFloors)
	{
		ServiceFloors = serviceFloors;
	}	
	
	public int GetNumElevators()
	{
		return NumElevators;
	}
	
	public int GetNumFloors()
	{
		return NumFloors;
	}
	
	public List<Integer> GetServiceFloors()
	{
		return ServiceFloors;
	}	
	
	 private int NumElevators;
	 private int NumFloors;
	 private List<Integer> ServiceFloors;

}
