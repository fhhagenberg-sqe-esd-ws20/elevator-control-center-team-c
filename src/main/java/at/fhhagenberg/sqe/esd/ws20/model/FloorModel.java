package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

public class FloorModel implements IFloorModel {

	public FloorModel()
	{
		ups = new ArrayList<>();
		downs = new ArrayList<>();
	}
	
	/**
	 * Add button press of an up button to the list
	 */
	@Override
	public void addButtonUp(int newUp)
	{
		ups.add(newUp);
	}
	
	/**
	 * Add button press of an down button to the list
	 */
	@Override
	public void addButtonDown(int newDown)
	{
		downs.add(newDown);
	}
	
	/**
	 * clear the content of the up list
	 */
	@Override
	public void clearUpButtonsList()
	{
		ups.clear();
	}
	
	/**
	 * clear the content of the down list
	 */
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
