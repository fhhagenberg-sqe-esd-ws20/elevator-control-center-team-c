package at.fhhagenberg.sqe.esd.ws20.model;

/**
 * Model of a building. Holds all information of a building.
 * 
 * @author Florian Atzenhofer (s1910567001)
 * @author Stefan Wohlrab (s1910567010)
 * @since 2020-12-30 18:20
 */
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
