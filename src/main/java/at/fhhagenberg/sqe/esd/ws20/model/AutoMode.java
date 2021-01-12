package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for automatic mode algorithms. Implements all needed functions,
 * except the algorithm.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-12 01:42
 */
public abstract class AutoMode {

	protected List<Integer> autoModeEnabledElevators = new ArrayList<Integer>();

	protected IBuildingModel Building;
	protected IFloorModel Floor;
	protected UpdateData Updater;
	protected List<IElevatorModel> Elevators;
	protected StatusAlert StatusAlert;

	/**
	 * Initialization for needed Objects
	 * 
	 * @param building     the internal model of the building
	 * @param numElevators the internal list with the models for the elevators
	 */
	public void Init(IBuildingModel building, int numElevators, UpdateData updater) {
		if (building == null || updater == null) {
			throw new NullPointerException("AutoMode.Init() NullPointerException!");
		}

		Building = building;
		Updater = updater;

		// at the beginning set all elevators to automatic mode
		for (int i = 0; i < numElevators; ++i) {
			enable(i);
		}
	}

	/**
	 * Enables the automatic mode for the given elevator
	 * 
	 * @param elevatorNr
	 * @return false if the elevator is already in auto mode, true if enable was
	 *         successful.
	 */
	public boolean enable(Integer elevatorNr) {
		if (!autoModeEnabledElevators.contains(elevatorNr)) {
			autoModeEnabledElevators.add(elevatorNr);
			return true;
		}
		return false;
	}

	/**
	 * Disables the automatic mode for the given elevator
	 * 
	 * @param elevatorNr
	 * @return false if the elevator is already in manual mode, true if disable was
	 *         successful.
	 */

	public boolean disable(Integer elevatorNr) {
		if (autoModeEnabledElevators.contains(elevatorNr)) {
			autoModeEnabledElevators.remove(elevatorNr); // if floor:int remove at index floor, if floor:Integer remove Object floor
			return true;
		}
		return false;
	}

	/**
	 * Checks whether a given elevator is in automatic mode
	 * 
	 * @param elevatorNr
	 * @return
	 */
	public boolean checkIfInAutoMode(Integer elevatorNr) {
		return autoModeEnabledElevators.contains(elevatorNr);
	}

	/**
	 * Update the targets of all elevators in automatic mode, according to automode
	 * algorithm
	 */
	public void updateAllElevatorTargets() {
		for (Integer e : autoModeEnabledElevators) {
			Updater.setTarget(doGetNext(e), e);
		}
	}

	/**
	 * Returns the next floor of elevator in automatic mode according to algorithm
	 * 
	 * @param elevator number of elevator to update
	 * @return number of the next target floor
	 */
	protected abstract int doGetNext(int elevator);

}
