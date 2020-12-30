package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public class BuildingModel implements IBuildingModel {

	//TODO add constructor, which is creating list
	@Override
	public void AddServiceFloor(int floorNumber)
	{
		if(!(ServiceFloors.contains(floorNumber)))
		{
			ServiceFloors.add(floorNumber);
		}
	}
	
	@Override
	public void SetNumElevators(int numElevators)
	{
		NumElevators = numElevators;
	}
	
	@Override
	public void SetNumFloors(int numFloors)
	{
		NumFloors = numFloors;
	}
	
	@Override
	public void SetServiceFloors(List<Integer> serviceFloors)
	{
		ServiceFloors = serviceFloors;
	}	
	
	@Override
	public int GetNumElevators()
	{
		return NumElevators;
	}
	
	@Override
	public int GetNumFloors()
	{
		return NumFloors;
	}
	
	@Override
	public List<Integer> GetServiceFloors()
	{
		return ServiceFloors;
	}	
	
	 private int NumElevators;
	 private int NumFloors;
	 private List<Integer> ServiceFloors;

}
