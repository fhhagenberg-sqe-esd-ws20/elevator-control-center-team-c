package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IFloorModel {

	/**
	 * Add button press of an up button to the list
	 */
	public void AddUp(int up);
	
	/**
	 * Add button press of an down button to the list
	 */
	public void AddDown(int down);
	
	/**
	 * clear the content of the up list
	 */
	public void ClearUps();
	
	/**
	 * clear the content of the down list
	 */
	public void ClearDowns();
	
	void SetUps(List<Integer> ups);

	void SetDowns(List<Integer> downs);

	List<Integer> GetUps();

	List<Integer> GetDowns();

}