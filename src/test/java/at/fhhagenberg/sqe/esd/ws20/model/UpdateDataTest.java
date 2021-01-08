package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

/**
 * @author Florian Atzenhofer
 *
 *	Testing Class UpdateData
 */
@ExtendWith(MockitoExtension.class)

public class UpdateDataTest {

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

	@BeforeEach
	void setUp() throws Exception {
		StatusAlert = new StatusAlert();
		Elevators = new ArrayList<IElevatorModel>();
	}
	
	@Test
    public void testNoSqBuilding() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(null, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert));
    }
	
	@Test
    public void testNoSqElevator() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, null, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert));

    }	
	
	@Test
    public void testNoBuilding() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, null, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert));

    }	
	
	@Test
    public void testNoFloor() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, null, Elevators, MockedmainGuiControler, StatusAlert));
    }		
	
	@Test
    public void testNoElevators() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, null, MockedmainGuiControler, StatusAlert));
    }			
	
	@Test
    public void testNoMainGuiControler() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, null, StatusAlert));
    }			
	
	
	@Test
    public void testCtorWithoutFloors() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}		
	
	
	@Test
    public void testCtorWithouttwoFloors() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(5);		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		assertEquals(2, MockedBuilding.getNumElevators());
		assertEquals(5, MockedBuilding.getNumFloors());
	}	
	
	
	@Test
    public void testInitializeServicedFloorsWithNoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);	
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
				
		coreUpdater.initializeServicedFloors();
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}			
	
	@Test
    public void testInitializeServicedFloorsWithTooMuchElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(3);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		// add one elevator to much to the list and initialize the serviced floors
		for(int i = 0; i < 4; i++)
		{
			Elevators.add(new ElevatorModel());
		}
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.initializeServicedFloors());
	}			
	
	@Test
    public void testInitializeThreeServicedFloorsInTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(3);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		List<Integer> ignoredFloors0 = Elevators.get(0).getIgnoredFloorsList();
		List<Integer> ignoredFloors1 = Elevators.get(1).getIgnoredFloorsList();

		assertEquals(0, ignoredFloors0.size());
		assertEquals(0, ignoredFloors1.size());

	}		
	

	@Test
    public void testSetSelectedElevatorToInValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setSelectedElevator(2);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}		
	
	@Test
    public void testSetSelectedElevatorToNegativeIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setSelectedElevator(-1);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}			
	
	@Test
    public void testSetSelectedElevatorToValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setSelectedElevator(1);
		
		assertEquals(1, coreUpdater.getSelectedElevator());
	}			
	
	@Test
    public void testSetTargetInvalidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setTarget(11);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}		
	
	@Test
    public void testSetTargetNegativeFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setTarget(-1);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}	
	
	@Test
    public void testSetTargetValidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setTarget(10);
		
		assertEquals(10, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 10);
		
	}		
	
	@Test
    public void testSetTargetValidDifferentTargetsForTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.setSelectedElevator(0);
		coreUpdater.setTarget(10);
		coreUpdater.setSelectedElevator(1);
		coreUpdater.setTarget(5);
		
		assertEquals(10, Elevators.get(0).getTarget());
		assertEquals(5, Elevators.get(1).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 10);
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(1, 5);

	}		
	
	@Test
    public void testSetInvalidTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}			
	
	@Test
    public void testSetValidTargetInvalidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.setTarget(10, 2);
		coreUpdater.setTarget(10, 3);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}		
	
	@Test
    public void testSetNegativeTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}		
	
	@Test
    public void testSetValidTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.setTarget(6, 0);
		coreUpdater.setTarget(7, 1);

		
		assertEquals(6, Elevators.get(0).getTarget());
		assertEquals(7, Elevators.get(1).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 6);
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(1, 7);
	}		
	
	@Test
    public void testRefreshUpList() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.refreshUpList();
		
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(2);
	}			
	
	@Test
    public void testRefreshDownList() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.refreshDownList();
		
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
	}	
	
	@Test
    public void testRefreshUpDownList() throws RemoteException {
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
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.refreshUpDownList();
		
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(1);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(3);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
	}		
	
	@Test
    public void testRefreshUpDownListTimeout() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		Mockito.when(MockedBuildingWrapper.getClockTick()).thenReturn((long) 4, (long)5);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(0)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(2)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonUp(3)).thenReturn(true);	
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(0)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(1)).thenReturn(false);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(MockedBuildingWrapper.getFloorButtonDown(3)).thenReturn(false);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		coreUpdater.refreshUpDownList();
		
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(1);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(3);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(0));
		assertEquals("Out of sync with the simulator at clocktick " + 5, StatusAlert.Status.get());
	}			
	
	@Test
    public void testRefreshElevatorInvalidIndex() throws RemoteException {

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(2));
	}	
	
	@Test
    public void testRefreshElevatorNegativedIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);


		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(-1));
	}			
	
	@Test
    public void testRefreshElevatorInvalidTarget() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getTarget(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.refreshElevator(0);

		Mockito.verify(MockedmainGuiControler, never()).update(Mockedfloor, Elevators.get(0));
		assertEquals("Sanity Check failed in UpdateData.refreshElevator()", StatusAlert.Status.get());
	}			
	
	@Test
    public void testRefreshElevatorInvalidPosition() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getElevatorFloor(0)).thenReturn(5);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		coreUpdater.refreshElevator(0);

		Mockito.verify(MockedmainGuiControler, never()).update(Mockedfloor, Elevators.get(0));
		assertEquals("Sanity Check failed in UpdateData.refreshElevator()", StatusAlert.Status.get());
	}			
	
	
	@Test
    public void testRefreshElevatorValidValuesElevator0() throws RemoteException {
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

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
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
    public void testRefreshElevatorValidValuesElevator1() throws RemoteException {
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

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);


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
    public void testRefreshElevatorValidValuesNotSelectedElevator() throws RemoteException {
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

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		
		coreUpdater.refreshElevator(1);
		
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
	}			
	
	
	@Test
    public void testRefreshElevatorTimeout() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		Mockito.when(MockedElevatorWrapper.getClockTick()).thenReturn((long) 4, (long)5);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		
		coreUpdater.refreshElevator(0);
		
		
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(1));
		
		assertEquals("Out of sync with the simulator when getting updownlist", StatusAlert.Status.get());
		Mockito.verify(MockedmainGuiControler, times(0)).update(Mockedfloor, Elevators.get(0));
	}			
	
	@Test
    public void testRunMethode() throws RemoteException {
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
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);
		
		
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
    public void testElevatorListFill() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, StatusAlert);

		assertEquals(2 , Elevators.size());		
	}
}
	