package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public class ElevatorModel implements IElevatorModel {

	@Override
	public void SetTarget(int target)
	{
		Target = target;
	}
	
	@Override
	public void SetPosition(int position)
	{
		Position = position;
	}
	
	@Override
	public void SetDirection(int direction)
	{
		Direction = direction;
	}
	
	@Override
	public void SetPayload(int payload)
	{
		Payload = payload;
	}
	
	@Override
	public void SetSpeed(int speed)
	{
		Speed = speed;
	}
	
	@Override
	public void SetDoors(int doors)
	{
		Doors = doors;
	}
	
	@Override
	public void SetStops(List<Integer> stops)
	{
		Stops = stops;
	}
	
	@Override
	public int GetTarget()
	{
		return Target;
	}
	
	@Override
	public int GetPosition()
	{
		return Position;
	}
	
	@Override
	public int GetDirection()
	{
		return Direction;
	}
	
	@Override
	public int GetPayload()
	{
		return Payload;
	}
	
	@Override
	public int GetSpeed()
	{
		return Speed;
	}
	
	@Override
	public int GetDoors()
	{
		return Doors;
	}
	
	@Override
	public List<Integer> GetStops()
	{
		return Stops;
	}
	
	
	
	@Override
	public void AddStop(int stop)
	{
		if(!(Stops.contains(stop)))
		{
			Stops.add(stop);
		}
	}
	
	
	private int Target;
	private int Position;
	private int Direction;
	private int Payload;
	private int Speed;
	private int Doors;
	private List<Integer> Stops;
}
