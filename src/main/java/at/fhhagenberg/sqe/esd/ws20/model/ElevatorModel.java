package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

public class ElevatorModel implements IElevatorModel {

	public ElevatorModel()
	{
		Stops = new ArrayList<Integer>();
		IgnoredFloors = new ArrayList<Integer>();
	}
	
	
	/**
	 * Add stop to elevator stop list
	 */
	@Override
	public void addStop(Integer stop)
	{
		if (Stops == null)
		{
			throw new NullPointerException("Nullpointer in ElevatorModel.AddStop()");
		}
		
		if(!(Stops.contains(stop)))
		{
			Stops.add(stop);
		}
	}
	
	@Override
	public void setTarget(Integer target)
	{
		Target = target;
	}
	
	@Override
	public void setPosition(Integer position)
	{
		Position = position;
	}
	
	@Override
	public void setDirection(Integer direction)
	{
		Direction = direction;
	}
	
	@Override
	public void setPayload(Integer payload)
	{
		Payload = payload;
	}
	
	@Override
	public void setSpeed(Integer speed)
	{
		Speed = speed;
	}
	
	@Override
	public void setDoors(Integer doors)
	{
		Doors = doors;
	}
	
	@Override
	public void setStops(List<Integer> stops)
	{
		if(stops != null)
		{
			Stops = stops;
		}
	}
	
	@Override
	public Integer getTarget()
	{
		return Target;
	}
	
	@Override
	public Integer getPosition()
	{
		return Position;
	}
	
	@Override
	public Integer getDirection()
	{
		return Direction;
	}
	
	@Override
	public Integer getPayload()
	{
		return Payload;
	}
	
	@Override
	public Integer getSpeed()
	{
		return Speed;
	}
	
	@Override
	public Integer getDoors()
	{
		return Doors;
	}
	
	@Override
	public List<Integer> getStops()
	{
		return Stops;
	}
	
	@Override
	public void addIgnoredFloor(int floorNumber)
	{
		if(!(IgnoredFloors.contains(floorNumber)))
		{
			IgnoredFloors.add(floorNumber);
		}
	}
	
	@Override
	public void setIgnoredFloors(List<Integer> serviceFloors)
	{
		if(serviceFloors != null)
		{
			IgnoredFloors = serviceFloors;
		}
	}
	
	@Override
	public List<Integer> getIgnoredFloors()
	{
		return IgnoredFloors;
	}	
	
	private Integer Target = 0;
	private Integer Position = 0;
	private Integer Direction = 0;
	private Integer Payload = 0;
	private Integer Speed = 0;
	private Integer Doors = 0;
	private List<Integer> Stops;
	 private List<Integer> IgnoredFloors;
}
