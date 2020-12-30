package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

public class BuildingModel implements IBuildingModel {
	
	@Override
	public void setNumElevators(int numElevators)
	{
		NumElevators = numElevators;
	}
	
	@Override
	public void setNumFloors(int numFloors)
	{
		NumFloors = numFloors;
	}
		
	@Override
	public int getNumElevators()
	{
		return NumElevators;
	}
	
	@Override
	public int getNumFloors()
	{
		return NumFloors;
	}
	
	 private int NumElevators;
	 private int NumFloors;

}
