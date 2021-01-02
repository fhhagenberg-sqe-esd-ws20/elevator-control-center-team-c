package at.fhhagenberg.sqe.esd.ws20.model;

import java.util.concurrent.ThreadLocalRandom;

public class AutoModeSimpleAlgo extends AutoMode {

	//TODO remove this and replace by real algo
	private int max = 0;
	//TODO remove this and replace by real algo
	public AutoModeSimpleAlgo(int numFloors) {
		max = numFloors;
	}
	//TODO remove this and replace by real algo
	@Override
	public int doGetNext() {
		return ThreadLocalRandom.current().nextInt(0, max + 1);
	}

}
