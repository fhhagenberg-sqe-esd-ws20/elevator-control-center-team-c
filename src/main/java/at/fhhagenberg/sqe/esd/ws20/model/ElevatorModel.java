package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

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
	public void setDirection(ElevatorDirection direction)
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
	public void setDoors(ElevatorDoorStatus doors)
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
	public ElevatorDirection getDirection()
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
	public ElevatorDoorStatus getDoors()
	{
		return Doors;
	}
	
	@Override
	public List<Integer> getStopsList()
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
	public List<Integer> getIgnoredFloorsList()
	{
		return IgnoredFloors;
	}	
	
	public void clearIgnoredFloorsList() {
		IgnoredFloors.clear();
	}
	
	private Integer Target = 0;
	private Integer Position = 0;
	private ElevatorDirection Direction = ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED;
	private Integer Payload = 0;
	private Integer Speed = 0;
	private ElevatorDoorStatus Doors = ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED;
	private List<Integer> Stops;
	 private List<Integer> IgnoredFloors;
}
