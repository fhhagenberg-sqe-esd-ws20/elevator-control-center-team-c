package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

/**
 * Testing Class UpdateData
 *
 * @author Florian Atzenhofer
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
	
	List<IElevatorModel> Elevators;
	UpdateData coreUpdater;
	StatusAlert StatusAlert;
	AutoMode AutoModeAlgo = new AutoModeRandomAlgo();	//TODO Von Lukas: Da ich die Schnittstelle von UpdateData(...) geändert habe, musste ich diese Objekt hier einfügen, damit die Tests wieder durchlaufen. Muss man das auch mocken?

	@BeforeEach
	void setUp() throws Exception {
		StatusAlert = new StatusAlert();
		Elevators = new ArrayList<IElevatorModel>();
	}
	
	
	
	@Test
	void testNoBuilding() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(null, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo));
	}
	
	@Test
	void testNoFloor() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, null, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo));
	}
	
	@Test
	void testNoElevators() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, null, MockedmainGuiControler, StatusAlert, AutoModeAlgo));
	}			
	
	@Test
	void testNoMainGuiControler() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, null, StatusAlert, AutoModeAlgo));
	}
	
	@Test
	void testNoStatusAlert() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, null, AutoModeAlgo));
	}
	
	@Test
	void testNoAutoModeAlgo() throws RemoteException {
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, null));
	}
	
	
	@Test
	void testCtorWithoutFloors() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}
	
	
	@Test
	void testCtorWithouttwoFloors() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertEquals(2, MockedBuilding.getNumElevators());
		assertEquals(5, MockedBuilding.getNumFloors());
	}
	
	
	@Test
	void testInitializeServicedFloorsWithNoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.initializeServicedFloors();
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}
	
	@Test
	void testInitializeServicedFloorsWithTooMuchElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(3);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
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
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		List<Integer> ignoredFloors0 = Elevators.get(0).getIgnoredFloorsList();
		List<Integer> ignoredFloors1 = Elevators.get(1).getIgnoredFloorsList();

		assertEquals(0, ignoredFloors0.size());
		assertEquals(0, ignoredFloors1.size());
	}
	

	@Test
	void testSetSelectedElevatorToInValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(2);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}
	
	@Test
	void testSetSelectedElevatorToNegativeIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(-1);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}
	
	@Test
	void testSetSelectedElevatorToValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setSelectedElevator(1);
		
		assertEquals(1, coreUpdater.getSelectedElevator());
	}
	
	@Test
	void testSetTargetInvalidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(11);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}
	
	@Test
	void testSetTargetNegativeFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(-1);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}
	
	@Test
	void testSetTargetValidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(9);
		
		assertEquals(9, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 9);
	}
	
	@Test
	void testSetTargetValidDifferentTargetsForTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
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
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}
	
	@Test
	void testSetValidTargetInvalidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(10, 2);
		coreUpdater.setTarget(10, 3);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}
	
	@Test
	void testSetNegativeTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}
	
	@Test
	void testSetValidTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
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
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshUpList();
		
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
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshDownList();
		
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
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		coreUpdater.refreshUpDownList();
		
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
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);

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
		assertEquals(5, coreUpdater.GetOutOfSyncCounter());
	}
	
	@Test
	void testRefreshElevatorInvalidIndex() throws RemoteException {

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(2));
	}
	
	@Test
	void testRefreshElevatorNegativedIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);


		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(-1));
	}

	@Test
	void testRefreshElevatorInvalidTarget() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getTarget(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshElevator(0);

		Mockito.verify(MockedmainGuiControler, never()).update(Mockedfloor, Elevators.get(0));
	}
	
	@Test
	void testRefreshElevatorInvalidPosition() throws RemoteException, InterruptedException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		coreUpdater.refreshElevator(0);
		
		Thread.sleep(100);

		Mockito.verify(MockedmainGuiControler, never()).update(Mockedfloor, Elevators.get(0));
		assertEquals("Sanity Check failed in UpdateData.refreshElevator()", StatusAlert.Status.get());
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

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
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

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);


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

		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		
		coreUpdater.refreshElevator(1);
		
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
	}
	
	@Test
	void testRefreshElevatorTimeout() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getClockTick()).thenReturn((long) 0, (long)1, (long)1, (long)2, (long)2, (long)3, (long)3, (long)4, (long)4, (long)5, (long)5, (long)6);
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);

		
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		coreUpdater.refreshElevator(0);
		
		
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
		
		assertEquals(5, coreUpdater.GetOutOfSyncCounter());
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
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		
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
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		assertEquals(2 , Elevators.size());
	}
	
	@Test
	void setSQsNullpointerforElevator() throws RemoteException {		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.SetSqs(MockedBuildingWrapper, null));		
	}
	
	@Test
	void setSQsNullpointerforBuilding() throws RemoteException {		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.SetSqs(null, MockedElevatorWrapper));		
	}	
	
	@Test
	void setSQsIsCallingMethods() throws RemoteException {		
		Mockito.when(MockedBuildingWrapper.getElevatorNum()).thenReturn(2);
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		
		Mockito.verify(MockedmainGuiControler, times(1)).reUpdate();
		Mockito.verify(MockedBuilding, times(1)).setNumFloors(4);
		Mockito.verify(MockedBuilding, times(1)).setNumElevators(2);
	}		
	
	@Disabled
	@Test
	void ReconnectRMIFailureTest() throws RemoteException {		
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert, AutoModeAlgo);
		coreUpdater.SetSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.ReconnectRMI();
		coreUpdater.run();
		assertEquals("No Elevator Connection", StatusAlert.Status.get());
	}	
	
	//ToDo: Test RMI connection
	//ToDo: Test return bool values
	//ToDo: SetRMIs, ReconnectRMI
}
	