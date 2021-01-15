package at.fhhagenberg.sqe.esd.ws20.model;

public class BuildingModel implements IBuildingModel {
	
	@Override
	public void setNumElevators(int newNumElevators)
	{
		numElevators = newNumElevators;
	}
	
	@Override
	public void setNumFloors(int newNumFloors)
	{
		numFloors = newNumFloors;
	}
		
	@Override
	public int getNumElevators()
	{
		return numElevators;
	}
	
	@Override
	public int getNumFloors()
	{
		return numFloors;
	}
	
	 private int numElevators;
	 private int numFloors;

}
