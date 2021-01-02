package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.atLeastOnce;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import at.fhhagenberg.sqe.esd.ws20.model.BuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.ElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.FloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

/**
 * @author Florian Atzenhofer
 *
 *	Testing Class UpdateData
 */
@ExtendWith(MockitoExtension.class)

//TODO: Tests ueberlegen und erstellen

public class UpdateDataStubTest {

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
	StatusAlert MockedstatusAlert;

	@BeforeEach
	void setUp() throws Exception {
		MockedstatusAlert = new StatusAlert();
		//Mockedbuilding = new BuildingModel(); 
		//Mockedfloor = new FloorModel();
		//MockedElevatorWrapper = new ElevatorWrapperStub();
		//MockedmainGuiControler = new MainGuiController();
		Elevators = new ArrayList<IElevatorModel>();
	}
	
	@Test
    public void testNoSqBuilding() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(null, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert));

		//CreateEmptyElevators(stub.getElevatorNum());
    }
	
	@Test
    public void testNoSqElevator() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, null, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert));

    }	
	
	@Test
    public void testNoBuilding() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, null, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert));

    }	
	
	@Test
    public void testNoFloor() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, null, Elevators, MockedmainGuiControler, MockedstatusAlert));
    }		
	
	@Test
    public void testNoElevators() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, null, MockedmainGuiControler, MockedstatusAlert));
    }			
	
	@Test
    public void testNoMainGuiControler() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, null, MockedstatusAlert));
    }			
	
	
	@Test
    public void testCtorWithoutFloors() throws RemoteException {
		
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);		
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}		
	
	
	@Test
    public void testCtorWithouttwoFloors() throws RemoteException {
		
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(5);		
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		
		assertEquals(2, MockedBuilding.getNumElevators());
		assertEquals(5, MockedBuilding.getNumFloors());
	}	
	
	
	@Test
    public void testInitializeServicedFloorsWithNoElevators() throws RemoteException {
		
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(0);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(0);	
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
				
		coreUpdater.initializeServicedFloors();
		assertEquals(0, MockedBuilding.getNumElevators());
		assertEquals(0, MockedBuilding.getNumFloors());
	}			
	
	@Test
    public void testInitializeServicedFloorsWithTooMuchElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(3);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		
		// add one elevator to much to the list and initialize the serviced floors
		for(int i = 0; i < 4; i++)
		{
			Elevators.add(new ElevatorModel());
		}
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.initializeServicedFloors());
	}			
	
	@Test
    public void testInitializeTreeServicedFloorsInTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(3);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		
		for(int i = 0; i < 2; i++)
		{
			Elevators.add(new ElevatorModel());
		}
		
		coreUpdater.initializeServicedFloors();
		
		List<Integer> ignoredFloors0 = Elevators.get(0).getIgnoredFloorsList();
		List<Integer> ignoredFloors1 = Elevators.get(0).getIgnoredFloorsList();

		assertEquals(0, ignoredFloors0.get(0));
		assertEquals(1, ignoredFloors0.get(1));
		assertEquals(2, ignoredFloors0.get(2));

		assertEquals(0, ignoredFloors1.get(0));
		assertEquals(1, ignoredFloors1.get(1));
		assertEquals(2, ignoredFloors1.get(2));

	}		
	

	@Test
    public void testSetSelectedElevatorToInValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setSelectedElevator(2);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}		
	
	@Test
    public void testSetSelectedElevatorToNegativeIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setSelectedElevator(-1);
		
		assertEquals(0, coreUpdater.getSelectedElevator());
	}			
	
	@Test
    public void testSetSelectedElevatorToValidIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setSelectedElevator(1);
		
		assertEquals(1, coreUpdater.getSelectedElevator());
	}			
	
	@Test
    public void testSetTargetInvalidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setTarget(11);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}		
	
	@Test
    public void testSetTargetNegativeFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setTarget(-1);
		
		assertEquals(0, Elevators.get(0).getTarget());
	}	
	
	@Test
    public void testSetTargetValidFloor() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setTarget(10);
		
		assertEquals(10, Elevators.get(0).getTarget());
		Mockito.verify(MockedElevatorWrapper, times(1)).setTarget(0, 10);
		
	}		
	
	@Test
    public void testSetTargetValidDifferentTargetsForTwoElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
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
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}			
	
	@Test
    public void testSetValidTargetInvalidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setTarget(10, 2);
		coreUpdater.setTarget(10, 3);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}		
	
	@Test
    public void testSetNegativeTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.setTarget(11, 0);
		coreUpdater.setTarget(11, 1);

		
		assertEquals(0, Elevators.get(0).getTarget());
		assertEquals(0, Elevators.get(1).getTarget());
	}		
	
	@Test
    public void testSetValidTargetValidElevators() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(10);
		
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
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

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
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

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
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

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();
		coreUpdater.refreshUpDownList();
		
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(1);
		Mockito.verify(Mockedfloor, times(1)).addButtonUp(3);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(0);
		Mockito.verify(Mockedfloor, times(1)).addButtonDown(2);
	}		
	
	@Test
    public void testRefreshElevatorInvalidIndex() throws RemoteException {

		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(2));
	}	
	
	@Test
    public void testRefreshElevatorNegativedIndex() throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();

		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(-1));
	}			
	
	@Test
    public void testRefreshElevatorInvalidTarget) throws RemoteException {
		Mockito.when(MockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(MockedBuilding.getNumFloors()).thenReturn(4);
		coreUpdater = new UpdateData(MockedBuildingWrapper, MockedElevatorWrapper, MockedBuilding, Mockedfloor, Elevators, MockedmainGuiControler, MockedstatusAlert);
		AddEmptyElevators(2);
		coreUpdater.initializeServicedFloors();

		assertThrows(RuntimeException.class, () 
				-> coreUpdater.refreshElevator(-1));
	}			
	
	
	void AddEmptyElevators(int numElevators)
	{
		for(int i = 0; i < numElevators; i++)
		{
			Elevators.add(new ElevatorModel());
		}
	}
	
}
	