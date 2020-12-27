package at.fhhagenberg.sqe.esd.ws2020.controller;

import java.util.TimerTask;


public class PeriodicTask extends TimerTask {


    @Override
    public void run() {
        try {
        	System.out.println("test");
        	
        	
            // updateEnvironment();
            // updateElevators();
            // updateFloors();
        } catch (Exception ex) {
        }
    }
	
	
}
