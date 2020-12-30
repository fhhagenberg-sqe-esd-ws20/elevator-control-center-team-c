package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IElevatorModel {
	
	/**
	 * Add stop to elevator stop list
	 */
	void AddStop(Integer stop);


	void SetTarget(Integer target);

	void SetPosition(Integer position);

	void SetDirection(Integer direction);

	void SetPayload(Integer payload);

	void SetSpeed(Integer speed);

	void SetDoors(Integer doors);

	void SetStops(List<Integer> stops);

	Integer GetTarget();

	Integer GetPosition();

	Integer GetDirection();

	Integer GetPayload();

	Integer GetSpeed();

	Integer GetDoors();

	List<Integer> GetStops();



}