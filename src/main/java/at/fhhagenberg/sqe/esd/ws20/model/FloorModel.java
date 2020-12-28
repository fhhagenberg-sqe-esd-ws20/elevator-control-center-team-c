package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public class FloorModel implements IFloorModel {

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
