package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model of a floor. Holds all information of a floor.
 * 
 * @author Florian Atzenhofer (s1910567001)
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-01 21:00
 */
public class FloorModel implements IFloorModel {

	public FloorModel()
	{
		ups = new ArrayList<>();
		downs = new ArrayList<>();
	}
	
	@Override
	public void addButtonUp(int newUp)
	{
		ups.add(newUp);
	}
	
	@Override
	public void addButtonDown(int newDown)
	{
		downs.add(newDown);
	}

	@Override
	public void clearUpButtonsList()
	{
		ups.clear();
	}

	@Override
	public void clearDownButtonsList()
	{
		downs.clear();
	}
	
	@Override
	public void setUpButtonsList(List<Integer> newUps)
	{
		if(newUps != null)
		{
			ups = newUps;
		}
	}
	
	@Override
	public void setDownButtonsList(List<Integer> newDowns)
	{
		if(newDowns != null)
		{
			downs = newDowns;
		}
	}
	
	@Override
	public List<Integer> getUpButtonsList()
	{
		return ups;
	}
	
	@Override
	public List<Integer> getDownButtonsList()
	{
		return downs;
	}
	
	private List<Integer> ups;
	private List<Integer> downs;
}
