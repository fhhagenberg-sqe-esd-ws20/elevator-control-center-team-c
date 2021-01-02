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
    	
        assertEquals(0, elevatorModel.getIgnoredFloorsList().size());
        assertEquals(0, elevatorModel.getStopsList().size());
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
    	
        assertEquals(list, elevatorModel.getStopsList());
    }
	
    @Test
    void testIgnoredFloorsSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	List<Integer> list = new ArrayList<Integer>();
    	
    	elevatorModel.setIgnoredFloors(list);
    	
        assertEquals(list, elevatorModel.getIgnoredFloorsList());
    }
    
    
    @Test
    void testAddStop() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.addStop(3);
    	
    	assertEquals(1, elevatorModel.getStopsList().size());
    	assertEquals(3, elevatorModel.getStopsList().get(0));
    }
    
    @Test
    void testAddIgnoredFloor() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.addIgnoredFloor(4);
    	
    	assertEquals(1, elevatorModel.getIgnoredFloorsList().size());
    	assertEquals(4, elevatorModel.getIgnoredFloorsList().get(0));
    }
    
    @Test
    void testTargetSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.setTarget(1);
    	
        assertEquals(1, elevatorModel.getTarget());
    }

    @Test
    void testPositionSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.setPosition(2);
    	
        assertEquals(2, elevatorModel.getPosition());
    }
    
    @Test
    void testDirectionSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.setDirection(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
    	
        assertEquals(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_DOWN, elevatorModel.getDirection());
    }
    
    @Test
    void testPayloadSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.setPayload(44);
    	
        assertEquals(44, elevatorModel.getPayload());
    }
    
    @Test
    void testSpeedSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.setSpeed(41);
    	
        assertEquals(41, elevatorModel.getSpeed());
    }
    
    @Test
    void testDoorsSet_Get() {
    	ElevatorModel elevatorModel = new ElevatorModel();
    	
    	elevatorModel.setDoors(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
    	
        assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_OPEN, elevatorModel.getDoors());
    }
    
    
}
