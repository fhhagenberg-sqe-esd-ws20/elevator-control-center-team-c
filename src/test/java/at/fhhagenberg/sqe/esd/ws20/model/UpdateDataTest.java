package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.sqe.esd.ws20.others.TestUtils;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IRMIConnection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;
import sqelevator.IElevator;

/**
 * Testing Class UpdateData
 *
 * @author Florian Atzenhofer
* @since 2021-01-16 00:40
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class UpdateDataTest {

	@Mock
	IBuildingModel MockedBuilding;
	@Mock
	IFloorModel Mockedfloor;
	@Mock
	IElevatorWrapper MockedElevatorWrapper;
	@Mock
	IBuildingWrapper MockedBuildingWrapper;	
	@Mock
	MainGuiController MockedmainGuiControler;
	@Mock
	AutoMode MockedAutoModeAlgo;
	@Mock
	IRMIConnection MockedRMIConnection;
	@Mock
	IElevator IElevatorMock;
	
	List<IElevatorModel> Elevators;
	UpdateData coreUpdater;
	StatusAlert MockedStatusAlert;

	private final static int uiUpdateWaitDelayMs = 100;
	private TestUtils testutils = null;
	
	
	@BeforeEach
	void setUp() throws Exception {
		MockedStatusAlert = new StatusAlert();
		Elevators = new ArrayList<IElevatorModel>();
		
		testutils = new TestUtils(uiUpdateWaitDelayMs);
	}
	
	
	@Test
	void testNoBuilding() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(null, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection));
	}
	
	@Test
	void testNoFloor() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, null, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection));
	}
	
	@Test
	void testNoElevators() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, null, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection));
	}			
	
	@Test
	void testNoMainGuiControler() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, null, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection));
	}
	
	@Test
	void testNoStatusAlert() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, null, MockedAutoModeAlgo, MockedRMIConnection));
	}
	
	@Test
	void testNoAutoModeAlgo() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, null, MockedRMIConnection));
	}
	
	@Test
	void testNoRMIConnection() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, null));
	}
	
	@Test
	void testCtorWithoutFloors() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}
	
	
	@Test
	void testCtorWithouttwoFloors() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertEquals(2, MockedBuilding.getNumElevators());
		assertEquals(5, MockedBuilding.getNumFloors());
	}
	
	
	@Test
	void testInitializeServicedFloorsWithNoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.initializeServicedFloors();
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}
	
	@Test
	void testInitializeServicedFloorsWithTooMuchElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(3);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		// add one elevator to much to the list and initialize the serviced floors
		for(int i = 0; i < 4; i++)
		{
			Elevators.add(new ElevatorModel());
		}
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.initializeServicedFloors());
	}
	
	@Test
	void testInitializeThreeServicedFloorsInTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(3);
		
		Mockito.when(MockedElevatorWrapper.getServicesFloors(0, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(0, 1)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(0, 2)).thenReturn(true);
		
		Mockito.when(MockedElevatorWrapper.getServicesFloors(1, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(1, 1)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(1, 2)).thenReturn(true);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		List<Integer> ignoredFloors0 = Elevators.get(0).getIgnoredFloorsList();
		List<Integer> ignoredFloors1 = Elevators.get(1).getIgnoredFloorsList();

		assertEquals(0, ignoredFloors0.size());
		assertEquals(0, ignoredFloors1.size());
	}
	
	@Test
	void testInitializeThreeNotServicedFloorsInTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(3);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		List<Integer> ignoredFloors0 = Elevators.get(0).getIgnoredFloorsList();
		List<Integer> ignoredFloors1 = Elevators.get(1).getIgnoredFloorsList();

		assertEquals(3, ignoredFloors0.size());
		assertEquals(3, ignoredFloors1.size());
	}

	@Test
	void testSetSelectedElevatorToInValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(2);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}
	
	@Test
	void testSetSelectedElevatorToNegativeIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(-1);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}
	
	@Test
	void testSetSelectedElevatorToValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(1);
		
		assertEquals(1, coreUpdater.getSelectedElevator());
	}
	
	@Test
	void testSetTargetInvalidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(11);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}
	
	@Test
	void testSetTargetNegativeFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(-1);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}
	
	@Test
	void testSetTargetValidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(9);
		
		assertEquals(9, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 9);
	}
	
	@Test
	void testSetTargetCommitedDirectionUp() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(8);

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(9);

		
		assertEquals(9, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 9);
		Mockito.verify(MockedElevatorWrapper, times(1)).setCommittedDirection(0, ElevatorDirection.ELEVATOR_DIRECTION_UP);
	}	
	
	@Test
	void testSetTargetCommitedDirectionDown() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(4);

		
		assertEquals(4, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 4);
		Mockito.verify(MockedElevatorWrapper, times(1)).setCommittedDirection(0, ElevatorDirection.ELEVATOR_DIRECTION_DOWN);
	}		
	
	@Test
	void testSetTargetCommitedDirectionNoChange() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(5);

		
		assertEquals(5, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 5);
		Mockito.verify(MockedElevatorWrapper, never()).setCommittedDirection(0, ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
	}
	
	@Test
	void testSetTargetValidDifferentTargetsForTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(0);
		coreUpdater.setTarget(9);
		coreUpdater.setSelectedElevator(1);
		coreUpdater.setTarget(5);
		
		assertEquals(9, Elevators.get(0).getTarget());
		assertEquals(5, Elevators.get(1).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 9);
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(1, 5);
	}
	
	@Test
	void testSetInvalidTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}
	
	@Test
	void testSetValidTargetInvalidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(10, 2);
		coreUpdater.setTarget(10, 3);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}
	
	@Test
	void testSetNegativeTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}
	
	@Test
	void testSetValidTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(6, 0);
		coreUpdater.setTarget(7, 1);

		
		assertEquals(6, Elevators.get(0).getTarget());
		assertEquals(7, Elevators.get(1).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 6);
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(1, 7);
	}
	
	@Test
	void testRefreshUpList() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		Boolean retVal = coreUpdater.refreshUpList();
		
		assertFalse(retVal.booleanValue());
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(2);
	}
	
	@Test
	void testRefreshDownList() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		Boolean retVal = coreUpdater.refreshDownList();
		
		assertFalse(retVal.booleanValue());
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
	}
	
	@Test
	void testRefreshUpDownList() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(2)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(3)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		Boolean retVal = coreUpdater.refreshUpDownList();
		
		assertFalse(retVal.booleanValue());
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(1);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(3);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
	}
	
	@Test
	void testRefreshUpDownListTimeout() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		Mockito.when(MockedBuildingWrapper.getClockTick()).thenReturn((long) 0, (long)1, (long)1, (long)2, (long)2, (long)3, (long)3, (long)4, (long)4, (long)5, (long)5, (long)6);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(2)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(3)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		coreUpdater.refreshUpDownList();
		coreUpdater.refreshUpDownList();
		coreUpdater.refreshUpDownList();
		coreUpdater.refreshUpDownList();
		coreUpdater.refreshUpDownList();
		
		Mockito.verify(Mockedfloor, times(5)).addButtonUp(1);
		Mockito.verify(Mockedfloor, times(5)).addButtonUp(3);
		Mockito.verify(Mockedfloor, times(5)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(5)).addButtonDown(2);
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(0));
		assertEquals(5, coreUpdater.getOutOfSyncCounter());
	}
	
	@Test
	void testRefreshElevatorInvalidIndex() throws RemoteException {

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(2));
	}
	
	@Test
	void testRefreshElevatorNegativedIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);


		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(-1));
	}

	@Test
	void testRefreshElevatorInvalidTarget() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getTarget(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshElevator(0);

		Mockito.verify(MockedmainGuiControler, never()).update(Mockedfloor, Elevators.get(0));
	}
	
	@Test
	void testRefreshElevatorInvalidPosition() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshElevator(0);

		String expectedStatus = "Sanity Check failed in UpdateData.refreshElevator()";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
		Mockito.verify(MockedmainGuiControler, never()).update(Mockedfloor, Elevators.get(0));
	}
	
	
	@Test
	void testRefreshElevatorValidValuesElevator0() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		Mockito.when(MockedElevatorWrapper.getTarget(0)).thenReturn(1);
		Mockito.when(MockedElevatorWrapper.getElevatorDoorStatus(0)).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(2);
		Mockito.when(MockedElevatorWrapper.getElevatorSpeed(0)).thenReturn(4711);
		Mockito.when(MockedElevatorWrapper.getElevatorWeight(0)).thenReturn(4712);
		Mockito.when(MockedElevatorWrapper.getCommittedDirection(0)).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);

		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 1)).thenReturn(false);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 2)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 3)).thenReturn(false);

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshElevator(0);
		
		Mockito.verify(MockedmainGuiControler, times(1)).update(Mockedfloor, Elevators.get(0));
		assertEquals(1 , Elevators.get(0).getTarget());
		assertEquals(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN , Elevators.get(0).getDoors());
		assertEquals(2 , Elevators.get(0).getPosition());
		assertEquals(4711 , Elevators.get(0).getSpeed());
		assertEquals(4712 , Elevators.get(0).getPayload());
		assertEquals(ElevatorDirection.ELEVATOR_DIRECTION_UP , Elevators.get(0).getDirection());
		
		assertEquals(0 , Elevators.get(0).getStopsList().get(0));
		assertEquals(2 , Elevators.get(0).getStopsList().get(1));
	}
	
	@Test
	void testRefreshElevatorValidValuesElevator1() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		Mockito.when(MockedElevatorWrapper.getTarget(1)).thenReturn(1);
		Mockito.when(MockedElevatorWrapper.getElevatorDoorStatus(1)).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(1)).thenReturn(2);
		Mockito.when(MockedElevatorWrapper.getElevatorSpeed(1)).thenReturn(4711);
		Mockito.when(MockedElevatorWrapper.getElevatorWeight(1)).thenReturn(4712);
		Mockito.when(MockedElevatorWrapper.getCommittedDirection(1)).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);

		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 1)).thenReturn(false);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 2)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 3)).thenReturn(false);

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);


		coreUpdater.setSelectedElevator(1);
		coreUpdater.refreshElevator(1);
		
		
		Mockito.verify(MockedmainGuiControler, times(1)).update(Mockedfloor, Elevators.get(1));
		assertEquals(1 , Elevators.get(1).getTarget());
		assertEquals(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN , Elevators.get(1).getDoors());
		assertEquals(2 , Elevators.get(1).getPosition());
		assertEquals(4711 , Elevators.get(1).getSpeed());
		assertEquals(4712 , Elevators.get(1).getPayload());
		assertEquals(ElevatorDirection.ELEVATOR_DIRECTION_UP , Elevators.get(1).getDirection());
		
		assertEquals(0 , Elevators.get(1).getStopsList().get(0));
		assertEquals(2 , Elevators.get(1).getStopsList().get(1));
	}
	
	@Test
	void testRefreshElevatorValidValuesNotSelectedElevator() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		Mockito.when(MockedElevatorWrapper.getTarget(1)).thenReturn(1);
		Mockito.when(MockedElevatorWrapper.getElevatorDoorStatus(1)).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(1)).thenReturn(2);
		Mockito.when(MockedElevatorWrapper.getElevatorSpeed(1)).thenReturn(4711);
		Mockito.when(MockedElevatorWrapper.getElevatorWeight(1)).thenReturn(4712);
		Mockito.when(MockedElevatorWrapper.getCommittedDirection(1)).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);

		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 1)).thenReturn(false);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 2)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 3)).thenReturn(false);

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		
		coreUpdater.refreshElevator(1);
		
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
	}
	
	@Test
	void testRefreshElevatorTimeout() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getClockTick()).thenReturn((long) 0, (long)1, (long)1, (long)2, (long)2, (long)3, (long)3, (long)4, (long)4, (long)5, (long)5, (long)6);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		
		
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
		
		assertEquals(5, coreUpdater.getOutOfSyncCounter());
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(0));
	}
	
	@Test
	void testRunMethode() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		// elevator0
		Mockito.when(MockedElevatorWrapper.getTarget(0)).thenReturn(1);
		Mockito.when(MockedElevatorWrapper.getElevatorDoorStatus(0)).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(2);
		Mockito.when(MockedElevatorWrapper.getElevatorSpeed(0)).thenReturn(4711);
		Mockito.when(MockedElevatorWrapper.getElevatorWeight(0)).thenReturn(4712);
		Mockito.when(MockedElevatorWrapper.getCommittedDirection(0)).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UP);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 1)).thenReturn(false);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 2)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 3)).thenReturn(false);

		// elevator1
		Mockito.when(MockedElevatorWrapper.getTarget(1)).thenReturn(2);
		Mockito.when(MockedElevatorWrapper.getElevatorDoorStatus(1)).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(1)).thenReturn(1);
		Mockito.when(MockedElevatorWrapper.getElevatorSpeed(1)).thenReturn(32);
		Mockito.when(MockedElevatorWrapper.getElevatorWeight(1)).thenReturn(33);
		Mockito.when(MockedElevatorWrapper.getCommittedDirection(1)).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 1)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 2)).thenReturn(false);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(1, 3)).thenReturn(false);
		
		//updownlist
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(2)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(3)).thenReturn(true);	
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(3)).thenReturn(false);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		
		//update
		coreUpdater.run();
		
		// elevator0
		Mockito.verify(MockedmainGuiControler, times(1)).update(Mockedfloor, Elevators.get(0));
		assertEquals(1 , Elevators.get(0).getTarget());
		assertEquals(ElevatorDoorStatus.ELEVATOR_DOORS_OPEN , Elevators.get(0).getDoors());
		assertEquals(2 , Elevators.get(0).getPosition());
		assertEquals(4711 , Elevators.get(0).getSpeed());
		assertEquals(4712 , Elevators.get(0).getPayload());
		assertEquals(ElevatorDirection.ELEVATOR_DIRECTION_UP , Elevators.get(0).getDirection());
		assertEquals(0 , Elevators.get(0).getStopsList().get(0));
		assertEquals(2 , Elevators.get(0).getStopsList().get(1));
		
		// elevator1
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
		assertEquals(2 , Elevators.get(1).getTarget());
		assertEquals(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING , Elevators.get(1).getDoors());
		assertEquals(1 , Elevators.get(1).getPosition());
		assertEquals(32 , Elevators.get(1).getSpeed());
		assertEquals(33 , Elevators.get(1).getPayload());
		assertEquals(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED , Elevators.get(1).getDirection());
		assertEquals(0 , Elevators.get(1).getStopsList().get(0));
		assertEquals(1 , Elevators.get(1).getStopsList().get(1));
		
		// updownlist
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(1);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(3);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
		
	}
	
	@Test
	void testElevatorListFill() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertEquals(2 , Elevators.size());
	}
	
	@Test
	void testSetSQsNullpointerforElevator() throws RemoteException {
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.setSqs(MockedBuildingWrapper, null));
	}
	
	@Test
	void testSetSQsNullpointerforBuilding() throws RemoteException {
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.setSqs(null, MockedElevatorWrapper));
	}
	
	@Test
	void testExceptionWhengettingServicedFloors() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(5);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(0, 0)).thenThrow(new RemoteException());
		
		// elevator1
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		String expectedStatus = "Exception in getServicesFloors() of SQElevator with floor 0 and elevator 0";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	@Test
	void testRemoteExceptionInSetTargetWithSelectedElevator() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.setTarget(0);
		
		String expectedStatus = "Exception in setTarget of SQElevator with floor: 0";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	@Test
	void testRemoteExceptionInSetTargetWithElevator() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.setTarget(0, 0);
		
		String expectedStatus = "Exception in setTarget of SQElevator with floor: 0 for Elevator0";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	
	@Test
	void testRefreshElevatorWithRemoteExceptionWhenGettingTicks() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getClockTick()).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.refreshElevator(0);
		
		String expectedStatus = "Exception in getClockTick() of SQElevator";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	@Test
	void testRefreshElevatorWithRemoteExceptionWhenGettingElevatorButton() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorButton(0, 0)).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.refreshElevator(0);
		
		String expectedStatus = "Exception in getTarget() of SQElevator 1";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}

	@Test
	void testrefreshUpDownlistWithRemoteExceptionWhenGettingTicks() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getClockTick()).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.refreshUpDownList();
		

		String expectedStatus = "Exception in getClockTick() of SQElevator";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	@Test
	void testrefreshUpistWithRemoteExceptionWhenGettingUpButton() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.refreshUpList();
		
		String expectedStatus = "Exception in getFloorButtonUp of SQElevator";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	@Test
	void testrefreshDownistWithRemoteExceptionWhenGettingDownButton() throws RemoteException, InterruptedException, TimeoutException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenThrow(new RemoteException());
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.refreshDownList();
		
		String expectedStatus = "Exception in getFloorButtonDown of SQElevator";
		testutils.waitUntilStatusAlertHasStatus(expectedStatus, MockedStatusAlert);
		assertEquals(expectedStatus, MockedStatusAlert.status.get());
	}
	
	@Test
	void testSetSQsIsCallingMethods() throws RemoteException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		Mockito.verify(MockedmainGuiControler, times(1)).reUpdate();
		Mockito.verify(MockedBuilding, times(1)).setNumFloors(4);
		Mockito.verify(MockedBuilding, times(1)).setNumElevators(2);
	}
	
	@Test
	void testReconnectionIsCalledAfterRemoteException() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorSpeed(0)).thenThrow(new RemoteException());

		// elevator1
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		//update
		coreUpdater.run();
		
		Mockito.verify(MockedRMIConnection, times(1)).getElevator();
	}
	
	@Test
	void testgetElevatorClockTick_Error() throws RemoteException {
		Mockito.when(MockedElevatorWrapper.getClockTick()).thenThrow(new RemoteException());

		// elevator1
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertTrue(coreUpdater.updateElevatorClockTick());
	}
	
	@Test
	void testgetElevatorClockTick() throws RemoteException {
		Mockito.when(MockedElevatorWrapper.getClockTick()).thenReturn((long) 1234);

		// elevator1
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertFalse(coreUpdater.updateElevatorClockTick());
		assertEquals(1234, coreUpdater.getCurrentTick());
	}
	
	@Test
	void testGetElevatorIgnoredListNoElevators() {
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		assertTrue(coreUpdater.getIgnoredFloorsFromSelectedElevator().isEmpty());
	}
	
	@Disabled
	@Test
	void testGetElevatorIgnoredList() throws RemoteException {
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(0, 0)).thenReturn(true);
		Mockito.when(MockedElevatorWrapper.getServicesFloors(0, 1)).thenReturn(false);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedStatusAlert, MockedAutoModeAlgo, MockedRMIConnection);
		coreUpdater.setSelectedElevator(0);
		
		System.out.println(coreUpdater.getIgnoredFloorsFromSelectedElevator());
		assertFalse(coreUpdater.getIgnoredFloorsFromSelectedElevator().isEmpty());
	}
}
