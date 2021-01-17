package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import sqelevator.IElevator;

@ExtendWith(MockitoExtension.class)


class ElevatorWrapperTest {

	@Mock
	private IElevator mockedIElevator;

	private ElevatorWrapper elevatorWrapper;

	@BeforeEach
	void setUp() throws Exception {
		elevatorWrapper = new ElevatorWrapper(mockedIElevator);
	}

	@Test
	void testConstructor_IElevator_NullPtr() throws RemoteException {
		assertThrows(NullPointerException.class, () -> new ElevatorWrapper(null));
	}

	@Test
	void testGetElevatorNum() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorNum()).thenReturn(3);

		assertEquals(3, elevatorWrapper.getElevatorNum());
	}

	@Test
	void testGetFloorNum() throws RemoteException {
		Mockito.when(mockedIElevator.getFloorNum()).thenReturn(22);

		assertEquals(22, elevatorWrapper.getFloorNum());
	}

	@Test
	void testGetFloorButtonDown_true() throws RemoteException {
		Mockito.when(mockedIElevator.getFloorButtonDown(1)).thenReturn(true);

		assertTrue(elevatorWrapper.getFloorButtonDown(1));
	}

	@Test
	void testGetFloorButtonDown_false() throws RemoteException {
		Mockito.when(mockedIElevator.getFloorButtonDown(1)).thenReturn(false);

		assertFalse(elevatorWrapper.getFloorButtonDown(1));
	}

	@Test
	void testGetFloorButtonUp_true() throws RemoteException {
		Mockito.when(mockedIElevator.getFloorButtonUp(2)).thenReturn(true);

		assertTrue(elevatorWrapper.getFloorButtonUp(2));
	}

	@Test
	void testGetFloorButtonUp_false() throws RemoteException {
		Mockito.when(mockedIElevator.getFloorButtonUp(2)).thenReturn(false);

		assertFalse(elevatorWrapper.getFloorButtonUp(2));
	}

	@Test
	void testSetTarget() throws RemoteException {
		elevatorWrapper.setTarget(1, 10);

		// verify method calls
		Mockito.verify(mockedIElevator).setTarget(1, 10);
		Mockito.verifyNoMoreInteractions(mockedIElevator);
	}

	@Test
	void testGetTarget() throws RemoteException {
		Mockito.when(mockedIElevator.getTarget(3)).thenReturn(6);

		assertEquals(6, elevatorWrapper.getTarget(3));
	}

	@Test
	void testSetCommittedDirection_Down() throws RemoteException {
		elevatorWrapper.setCommittedDirection(1, ElevatorDirection.ELEVATOR_DIRECTION_DOWN);

		// verify method calls
		Mockito.verify(mockedIElevator).setCommittedDirection(1, IElevator.ELEVATOR_DIRECTION_DOWN);
		Mockito.verifyNoMoreInteractions(mockedIElevator);
	}
	
	@Test
	void testSetCommittedDirection_Up() throws RemoteException {
		elevatorWrapper.setCommittedDirection(1, ElevatorDirection.ELEVATOR_DIRECTION_UP);

		// verify method calls
		Mockito.verify(mockedIElevator).setCommittedDirection(1, IElevator.ELEVATOR_DIRECTION_UP);
		Mockito.verifyNoMoreInteractions(mockedIElevator);
	}
	
	@Test
	void testSetCommittedDirection_Uncommited() throws RemoteException {
		elevatorWrapper.setCommittedDirection(1, ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);

		// verify method calls
		Mockito.verify(mockedIElevator).setCommittedDirection(1, IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.verifyNoMoreInteractions(mockedIElevator);
	}

	@Test
	void testGetCommittedDirection_Down() throws RemoteException {
		Mockito.when(mockedIElevator.getCommittedDirection(4)).thenReturn(IElevator.ELEVATOR_DIRECTION_DOWN);

		assertEquals(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_DOWN, elevatorWrapper.getCommittedDirection(4));
	}
	
	@Test
	void testGetCommittedDirection_Up() throws RemoteException {
		Mockito.when(mockedIElevator.getCommittedDirection(4)).thenReturn(IElevator.ELEVATOR_DIRECTION_UP);

		assertEquals(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_UP, elevatorWrapper.getCommittedDirection(4));
	}
	
	@Test
	void testGetCommittedDirection_Uncommited() throws RemoteException {
		Mockito.when(mockedIElevator.getCommittedDirection(4)).thenReturn(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);

		assertEquals(IElevatorWrapper.ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED, elevatorWrapper.getCommittedDirection(4));
	}
	
	@Test
	void testGetCommittedDirection_Error() throws RemoteException {
		Mockito.when(mockedIElevator.getCommittedDirection(4)).thenReturn(10);

		assertThrows(InvalidParameterException.class, () 
				-> elevatorWrapper.getCommittedDirection(4));
	}

	@Test
	void testGetElevatorDoorStatus_Closed() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorDoorStatus(5)).thenReturn(IElevator.ELEVATOR_DOORS_CLOSED);

		assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED, elevatorWrapper.getElevatorDoorStatus(5));
	}
	
	@Test
	void testGetElevatorDoorStatus_Closing() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorDoorStatus(5)).thenReturn(IElevator.ELEVATOR_DOORS_CLOSING);

		assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING, elevatorWrapper.getElevatorDoorStatus(5));
	}
	
	@Test
	void testGetElevatorDoorStatus_Open() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorDoorStatus(5)).thenReturn(IElevator.ELEVATOR_DOORS_OPEN);

		assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_OPEN, elevatorWrapper.getElevatorDoorStatus(5));
	}
	
	@Test
	void testGetElevatorDoorStatus_Opening() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorDoorStatus(5)).thenReturn(IElevator.ELEVATOR_DOORS_OPENING);

		assertEquals(IElevatorWrapper.ElevatorDoorStatus.ELEVATOR_DOORS_OPENING, elevatorWrapper.getElevatorDoorStatus(5));
	}
	
	@Test
	void testGetElevatorDoorStatus_Error() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorDoorStatus(5)).thenReturn(10);

		assertThrows(InvalidParameterException.class, () 
				-> elevatorWrapper.getElevatorDoorStatus(5));
	}

	@Test
	void testGetElevatorFloor() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorFloor(6)).thenReturn(18);

		assertEquals(18, elevatorWrapper.getElevatorFloor(6));
	}

	@Test
	void testGetElevatorSpeed() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorSpeed(7)).thenReturn(300);

		assertEquals(300, elevatorWrapper.getElevatorSpeed(7));
	}

	@Test
	void testGetElevatorWeight() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorWeight(8)).thenReturn(260);

		assertEquals(260, elevatorWrapper.getElevatorWeight(8));
	}

	@Test
	void testGetElevatorButton_true() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorButton(1, 2)).thenReturn(true);

		assertTrue(elevatorWrapper.getElevatorButton(1, 2));
	}

	@Test
	void testGetElevatorButton_false() throws RemoteException {
		Mockito.when(mockedIElevator.getElevatorButton(4, 5)).thenReturn(false);

		assertFalse(elevatorWrapper.getElevatorButton(4, 5));
	}

	@Test
	void testGetServicesFloors_true() throws RemoteException {
		Mockito.when(mockedIElevator.getServicesFloors(6, 7)).thenReturn(true);

		assertTrue(elevatorWrapper.getServicesFloors(6, 7));
	}

	@Test
	void testGetServicesFloors_false() throws RemoteException {
		Mockito.when(mockedIElevator.getServicesFloors(8, 9)).thenReturn(false);

		assertFalse(elevatorWrapper.getServicesFloors(8, 9));
	}

	@Test
	void testSetServicesFloors() throws RemoteException {
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
	void testGetClockTick() throws RemoteException {
		Mockito.when(mockedIElevator.getClockTick()).thenReturn((long) 123456);

		assertEquals(123456, elevatorWrapper.getClockTick());
	}
	
}
