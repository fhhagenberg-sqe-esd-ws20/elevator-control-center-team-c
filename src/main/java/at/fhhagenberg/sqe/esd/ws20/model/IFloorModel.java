package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

public interface IFloorModel {
	
	/**
	 * Add button press of an up button to the list
	 * @param 
	 */
	public void addButtonUp(int up);
	
	/**
	 * Add button press of an down button to the list
	 */
	public void addButtonDown(int down);
	
	
	/**
	 * clear the content of the up list
	 */
	public void clearUpButtonsList();
	
	/**
	 * clear the content of the down list
	 */
	public void clearDownButtonsList();
	
	void setUpButtonsList(List<Integer> ups);

	void setDownButtonsList(List<Integer> downs);

	List<Integer> getUpButtonsList();

	List<Integer> getDownButtonsList();

}