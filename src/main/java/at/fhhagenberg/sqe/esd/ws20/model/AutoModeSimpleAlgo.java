package at.fhhagenberg.sqe.esd.ws20.model;

import java.security.SecureRandom;

public class AutoModeSimpleAlgo extends AutoMode {
	
	private SecureRandom random = new SecureRandom();

	//TODO remove this and replace by real algo
	public AutoModeSimpleAlgo() {

	}
	//TODO remove this and replace by real algo
	@Override
	protected int doGetNext(int elevator) {
		//0 to Building.getNumFloors() exclusive
		return random.nextInt(Building.getNumFloors());
	}

}
