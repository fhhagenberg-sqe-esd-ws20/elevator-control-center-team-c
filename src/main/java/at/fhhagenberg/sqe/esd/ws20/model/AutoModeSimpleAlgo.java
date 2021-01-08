package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.concurrent.ThreadLocalRandom;

public class AutoModeSimpleAlgo extends AutoMode {

	//TODO remove this and replace by real algo
	public AutoModeSimpleAlgo() {

	}
	//TODO remove this and replace by real algo
	@Override
	protected int doGetNext(int elevator) {
		return ThreadLocalRandom.current().nextInt(0, Building.getNumFloors() + 1);
	}

}
