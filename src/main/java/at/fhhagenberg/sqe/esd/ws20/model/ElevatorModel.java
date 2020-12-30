package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public class ElevatorModel implements IElevatorModel {

	/**
	 * Add stop to elevator stop list
	 */
	@Override
	public void AddStop(Integer stop)
	{
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
		Stops = stops;
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
	
	
	private Integer Target;
	private Integer Position;
	private Integer Direction;
	private Integer Payload;
	private Integer Speed;
	private Integer Doors;
	private List<Integer> Stops;
}
