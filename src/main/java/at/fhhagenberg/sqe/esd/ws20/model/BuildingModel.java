package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

public class BuildingModel implements IBuildingModel {
	
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
	public int GetNumElevators()
	{
		return NumElevators;
	}
	
	@Override
	public int GetNumFloors()
	{
		return NumFloors;
	}
	
	 private int NumElevators;
	 private int NumFloors;

}
