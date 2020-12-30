package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.rmi.RemoteException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;

@ExtendWith(MockitoExtension.class)


public class ElevatorWrapperTest {

    @Mock
    private IElevator mockedIElevator;

	private ElevatorWrapper elevatorWrapper;

	@BeforeEach
	void setUp() throws Exception {
		elevatorWrapper = new ElevatorWrapper(mockedIElevator);
	}
    
    
    @Test
    public void testGetElevatorNum() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorNum()).thenReturn(3);
		
        assertEquals(3, elevatorWrapper.getElevatorNum());
    }
    
    @Test
    public void testGetFloorNum() throws RemoteException {
        Mockito.when(mockedIElevator.getFloorNum()).thenReturn(22);
		
        assertEquals(22, elevatorWrapper.getFloorNum());
    }
    
    @Test
    public void testGetFloorButtonDown_true() throws RemoteException {
        Mockito.when(mockedIElevator.getFloorButtonDown(1)).thenReturn(true);
		
        assertTrue(elevatorWrapper.getFloorButtonDown(1));
    }
    
    @Test
    public void testGetFloorButtonDown_false() throws RemoteException {
        Mockito.when(mockedIElevator.getFloorButtonDown(1)).thenReturn(false);
		
        assertFalse(elevatorWrapper.getFloorButtonDown(1));
    }
    
    @Test
    public void testGetFloorButtonUp_true() throws RemoteException {
        Mockito.when(mockedIElevator.getFloorButtonUp(2)).thenReturn(true);
		
        assertTrue(elevatorWrapper.getFloorButtonUp(2));
    }
    
    @Test
    public void testGetFloorButtonUp_false() throws RemoteException {
        Mockito.when(mockedIElevator.getFloorButtonUp(2)).thenReturn(false);
		
        assertFalse(elevatorWrapper.getFloorButtonUp(2));
    }
    
    @Test
    public void testSetTarget() throws RemoteException {
    	elevatorWrapper.setTarget(1, 10);
    	
        // verify method calls
        Mockito.verify(mockedIElevator).setTarget(1, 10);
        Mockito.verifyNoMoreInteractions(mockedIElevator);
    }
    
    @Test
    public void testGetTarget() throws RemoteException {
        Mockito.when(mockedIElevator.getTarget(3)).thenReturn(6);
		
        assertEquals(6, elevatorWrapper.getTarget(3));
    }
    
    @Test
    public void testSetCommittedDirection() throws RemoteException {
    	elevatorWrapper.setCommittedDirection(1, ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
    	
        // verify method calls
        Mockito.verify(mockedIElevator).setCommittedDirection(1, IElevator.ELEVATOR_DIRECTION_DOWN);
        Mockito.verifyNoMoreInteractions(mockedIElevator);
    }
    
    @Test
    public void testGetCommittedDirection() throws RemoteException {
        Mockito.when(mockedIElevator.getCommittedDirection(4)).thenReturn(IElevator.ELEVATOR_DIRECTION_DOWN);
		
        assertEquals(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_DOWN, elevatorWrapper.getCommittedDirection(4));
    }
    
    @Test
    public void testGetElevatorDoorStatus() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorDoorStatus(5)).thenReturn(IElevator.ELEVATOR_DOORS_CLOSED);
		
        assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED, elevatorWrapper.getElevatorDoorStatus(5));
    }
    
    @Test
    public void testGetElevatorFloor() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorFloor(6)).thenReturn(18);
		
        assertEquals(18, elevatorWrapper.getElevatorFloor(6));
    }
    
    @Test
    public void testGetElevatorSpeed() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorSpeed(7)).thenReturn(300);
		
        assertEquals(300, elevatorWrapper.getElevatorSpeed(7));
    }
    
    @Test
    public void testGetElevatorWeight() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorWeight(8)).thenReturn(260);
		
        assertEquals(260, elevatorWrapper.getElevatorWeight(8));
    }
    
    @Test
    public void testGetElevatorButton_true() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorButton(1,2)).thenReturn(true);
		
        assertTrue(elevatorWrapper.getElevatorButton(1,2));
    }

    @Test
    public void testGetElevatorButton_false() throws RemoteException {
        Mockito.when(mockedIElevator.getElevatorButton(4,5)).thenReturn(false);
		
        assertFalse(elevatorWrapper.getElevatorButton(4,5));
    }
	
    @Test
    public void testGetServicesFloors_true() throws RemoteException {
        Mockito.when(mockedIElevator.getServicesFloors(6,7)).thenReturn(true);
		
        assertTrue(elevatorWrapper.getServicesFloors(6,7));
    }

    @Test
    public void testGetServicesFloors_false() throws RemoteException {
        Mockito.when(mockedIElevator.getServicesFloors(8,9)).thenReturn(false);
		
        assertFalse(elevatorWrapper.getServicesFloors(8,9));
    }

    @Test
    public void testSetServicesFloors() throws RemoteException {
    	elevatorWrapper.setServicesFloors(3, 4, true);
    	elevatorWrapper.setServicesFloors(3, 8, false);
    	elevatorWrapper.setServicesFloors(6, 5, false);
    	
        // verify method calls
        Mockito.verify(mockedIElevator).setServicesFloors(3, 4, true);
        Mockito.verify(mockedIElevator).setServicesFloors(3, 8, false);
        Mockito.verify(mockedIElevator).setServicesFloors(6, 5, false);
        Mockito.verifyNoMoreInteractions(mockedIElevator);
    }
    
    @Test
    public void testGetClockTick() throws RemoteException {
        Mockito.when(mockedIElevator.getClockTick()).thenReturn((long)123456);
		
        assertEquals(123456, elevatorWrapper.getClockTick());
    }
	
}
