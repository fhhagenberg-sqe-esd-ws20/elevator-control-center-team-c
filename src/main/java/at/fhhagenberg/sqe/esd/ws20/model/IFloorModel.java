package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IFloorModel {

	void SetUps(List<Integer> ups);

	void SetDowns(List<Integer> downs);

	List<Integer> GetUps();

	List<Integer> GetDowns();

}