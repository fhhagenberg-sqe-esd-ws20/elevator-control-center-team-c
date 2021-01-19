package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;

/**
 * Model of an elevator. Holds all information of an elevator.
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2020-12-30 18:00
 */
public class ElevatorModel implements IElevatorModel {

	public ElevatorModel()
	{
		stops = new ArrayList<>();
		ignoredFloors = new ArrayList<>();
	}

	@Override
	public void addStop(Integer newStop)
	{
		if (stops == null)
		{
			throw new NullPointerException("Nullpointer in ElevatorModel.AddStop()");
		}
		
		if(!(stops.contains(newStop)))
		{
			stops.add(newStop);
		}
	}
	
	@Override
	public void setTarget(Integer newTarget)
	{
		target = newTarget;
	}
	
	@Override
	public void setPosition(Integer newPosition)
	{
		position = newPosition;
	}
	
	@Override
	public void setDirection(ElevatorDirection newDirection)
	{
		direction = newDirection;
	}
	
	@Override
	public void setPayload(Integer newPayload)
	{
		payload = newPayload;
	}
	
	@Override
	public void setSpeed(Integer newSpeed)
	{
		speed = newSpeed;
	}
	
	@Override
	public void setDoors(ElevatorDoorStatus newDoorsStatus)
	{
		doors = newDoorsStatus;
	}
	
	@Override
	public void setStops(List<Integer> newStops)
	{
		if(newStops != null)
		{
			stops = newStops;
		}
	}
	
	@Override
	public Integer getTarget()
	{
		return target;
	}
	
	@Override
	public Integer getPosition()
	{
		return position;
	}
	
	@Override
	public ElevatorDirection getDirection()
	{
		return direction;
	}
	
	@Override
	public Integer getPayload()
	{
		return payload;
	}
	
	@Override
	public Integer getSpeed()
	{
		return speed;
	}
	
	@Override
	public ElevatorDoorStatus getDoors()
	{
		return doors;
	}
	
	@Override
	public List<Integer> getStopsList()
	{
		return stops;
	}
	
	@Override
	public void addIgnoredFloor(int floorNumber)
	{
		if(!(ignoredFloors.contains(floorNumber)))
		{
			ignoredFloors.add(floorNumber);
		}
	}
	
	@Override
	public void setIgnoredFloors(List<Integer> serviceFloors)
	{
		if(serviceFloors != null)
		{
			ignoredFloors = serviceFloors;
		}
	}
	
	@Override
	public List<Integer> getIgnoredFloorsList()
	{
		return ignoredFloors;
	}	
	
	public void clearIgnoredFloorsList() {
		ignoredFloors.clear();
	}
	
	private Integer target = 0;
	private Integer position = 0;
	private ElevatorDirection direction = ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED;
	private Integer payload = 0;
	private Integer speed = 0;
	private ElevatorDoorStatus doors = ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED;
	private List<Integer> stops;
	private List<Integer> ignoredFloors;
}
