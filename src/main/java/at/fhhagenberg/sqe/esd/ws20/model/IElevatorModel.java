package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IElevatorModel {
	
	/**
	 * Add stop to elevator stop list
	 */
	void AddStop(int stop);


	void SetTarget(int target);

	void SetPosition(int position);

	void SetDirection(int direction);

	void SetPayload(int payload);

	void SetSpeed(int speed);

	void SetDoors(int doors);

	void SetStops(List<Integer> stops);

	int GetTarget();

	int GetPosition();

	int GetDirection();

	int GetPayload();

	int GetSpeed();

	int GetDoors();

	List<Integer> GetStops();



}