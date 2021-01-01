package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;

public class ElevatorModelTest {

    @Test
    void testCreate_and_get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
        assertEquals(0, elevatorModel.getIgnoredFloors().size());
        assertEquals(0, elevatorModel.getStops().size());
        assertEquals(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED, elevatorModel.getDirection());
        assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED, elevatorModel.getDoors());
        assertEquals(0, elevatorModel.getPayload());
        assertEquals(0, elevatorModel.getPosition());
        assertEquals(0, elevatorModel.getTarget());
    }
	
	
	
    @Test
    void testStopsListSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	List<Integer> list = new ArrayList<Integer>();
    	
    	elevatorModel.setStops(list);
    	
        assertEquals(list, elevatorModel.getStops());
    }
	
    @Test
    void testIgnoredFloorsSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	List<Integer> list = new ArrayList<Integer>();
    	
    	elevatorModel.setIgnoredFloors(list);
    	
        assertEquals(list, elevatorModel.getIgnoredFloors());
    }
    
    
    
    
}
