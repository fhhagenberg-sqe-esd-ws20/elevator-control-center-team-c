package at.fhhagenberg.sqe.esd.ws20.model;

import java.security.SecureRandom;

/**
 * Automatic mode algorithm. Returns a random number in Building.getNumFloors()
 * range.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-12 02:11
 */
public class AutoModeRandomAlgo extends AutoMode {

	private SecureRandom random = new SecureRandom();

	@Override
	protected int doGetNext(int elevator) {
		// 0 to Building.getNumFloors() exclusive
		return random.nextInt(Building.getNumFloors());
	}

}
