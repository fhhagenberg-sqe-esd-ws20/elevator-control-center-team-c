package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

public class ElevatorModel implements IElevatorModel {

	public ElevatorModel()
	{
		Stops = new ArrayList<Integer>();
		ServiceFloors = new ArrayList<Integer>();
	}
	
	
	/**
	 * Add stop to elevator stop list
	 */
	@Override
	public void AddStop(Integer stop)
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
	public void SetTarget(Integer target)
	{
		Target = target;
	}
	
	@Override
	public void SetPosition(Integer position)
	{
		Position = position;
	}
	
	@Override
	public void SetDirection(Integer direction)
	{
		Direction = direction;
	}
	
	@Override
	public void SetPayload(Integer payload)
	{
		Payload = payload;
	}
	
	@Override
	public void SetSpeed(Integer speed)
	{
		Speed = speed;
	}
	
	@Override
	public void SetDoors(Integer doors)
	{
		Doors = doors;
	}
	
	@Override
	public void SetStops(List<Integer> stops)
	{
		if(stops != null)
		{
			Stops = stops;
		}
	}
	
	@Override
	public Integer GetTarget()
	{
		return Target;
	}
	
	@Override
	public Integer GetPosition()
	{
		return Position;
	}
	
	@Override
	public Integer GetDirection()
	{
		return Direction;
	}
	
	@Override
	public Integer GetPayload()
	{
		return Payload;
	}
	
	@Override
	public Integer GetSpeed()
	{
		return Speed;
	}
	
	@Override
	public Integer GetDoors()
	{
		return Doors;
	}
	
	@Override
	public List<Integer> GetStops()
	{
		return Stops;
	}
	
	@Override
	public void AddServiceFloor(int floorNumber)
	{
		if(!(ServiceFloors.contains(floorNumber)))
		{
			ServiceFloors.add(floorNumber);
		}
	}
	
	@Override
	public void SetServiceFloors(List<Integer> serviceFloors)
	{
		if(serviceFloors != null)
		{
			ServiceFloors = serviceFloors;
		}
	}
	
	@Override
	public List<Integer> GetServiceFloors()
	{
		return ServiceFloors;
	}	
	
	private Integer Target = 0;
	private Integer Position = 0;
	private Integer Direction = 0;
	private Integer Payload = 0;
	private Integer Speed = 0;
	private Integer Doors = 0;
	private List<Integer> Stops;
	 private List<Integer> ServiceFloors;
}
