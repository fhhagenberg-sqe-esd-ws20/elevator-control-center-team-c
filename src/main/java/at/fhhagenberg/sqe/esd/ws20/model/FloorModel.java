package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;


public class FloorModel implements IFloorModel {

	/**
	 * Add button press of an up button to the list
	 */
	@Override
	public void AddUp(int up)
	{
		Ups.add(up);
	}
	
	/**
	 * Add button press of an down button to the list
	 */
	@Override
	public void AddDown(int down)
	{
		Ups.add(down);
	}
	
	/**
	 * clear the content of the up list
	 */
	@Override
	public void ClearUps()
	{
		Ups.clear();
	}
	
	/**
	 * clear the content of the down list
	 */
	@Override
	public void ClearDowns()
	{
		Downs.clear();
	}
	
	@Override
	public void SetUps(List<Integer> ups)
	{
		Ups = ups;
	}
	
	@Override
	public void SetDowns(List<Integer> downs)
	{
		Downs = downs;
	}
	
	@Override
	public List<Integer> GetUps()
	{
		return Ups;
	}
	
	@Override
	public List<Integer> GetDowns()
	{
		return Downs;
	}
	
	private List<Integer> Ups;
	private List<Integer> Downs;
}
