package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

public class FloorModel implements IFloorModel {

	public FloorModel()
	{
		Ups = new ArrayList<Integer>();
		Downs = new ArrayList<Integer>();
	}
	
	/**
	 * Add button press of an up button to the list
	 */
	@Override
	public void addButtonUp(int up)
	{
		Ups.add(up);
	}
	
	/**
	 * Add button press of an down button to the list
	 */
	@Override
	public void addButtonDown(int down)
	{
		Ups.add(down);
	}
	
	/**
	 * clear the content of the up list
	 */
	@Override
	public void clearUpButtonsList()
	{
		Ups.clear();
	}
	
	/**
	 * clear the content of the down list
	 */
	@Override
	public void clearDownButtonsList()
	{
		Downs.clear();
	}
	
	@Override
	public void setUpButtonsList(List<Integer> ups)
	{
		if(ups != null)
		{
			Ups = ups;
		}
	}
	
	@Override
	public void setDownButtonsList(List<Integer> downs)
	{
		if(downs != null)
		{
			Downs = downs;
		}
	}
	
	@Override
	public List<Integer> getUpButtonsList()
	{
		return Ups;
	}
	
	@Override
	public List<Integer> getDownButtonsList()
	{
		return Downs;
	}
	
	private List<Integer> Ups;
	private List<Integer> Downs;
}
