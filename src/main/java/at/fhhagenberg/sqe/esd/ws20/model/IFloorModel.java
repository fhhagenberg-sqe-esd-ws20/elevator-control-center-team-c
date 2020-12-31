package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.List;

/**
 * @author Florian Atzenhofer (s1910567001)
 * @since 2020-12-31 09:06
 * 
 * Interface for the FloorModel
 * The floor model has a list of up buttons pressed and a list of down buttons pressed.
 *
 */
public interface IFloorModel {
	
	/**
	 * Add button press of an up button to the list
	 * @param floorUp the number of floor in which the up button is pressed and should be added to the list
	 */
	public void addButtonUp(int floorUp);
	
	/**
	 * Add button press of an down button to the list
	 * @param floorDown the number of floor in which the down button is pressed and should be added to the list
	 */
	public void addButtonDown(int floorDown);
	
	/**
	 * Clear the content of the up list
	 */
	public void clearUpButtonsList();
	
	/**
	 * Clear the content of the down list
	 */
	public void clearDownButtonsList();
	
	/**
	 * Set the list of up buttons pressed
	 * @param ups list of up buttons pressed
	 */
	void setUpButtonsList(List<Integer> ups);

	/**
	 * Set the list of down buttons pressed
	 * @param downs list of down buttons pressed
	 */
	void setDownButtonsList(List<Integer> downs);

	/**
	 * Get the list of floors in which the up button is pressed
	 * @return the list of up buttons pressed
	 */
	List<Integer> getUpButtonsList();

	/**
	 * Get the list of floors in which the down button is pressed
	 * @return the list of down buttons pressed
	 */
	List<Integer> getDownButtonsList();

}