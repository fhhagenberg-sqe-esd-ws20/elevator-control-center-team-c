package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

@ExtendWith(MockitoExtension.class)

//TODO: Tests ueberlegen und erstellen

public class UpdateDataStubTest {

	@Mock
	StatusAlert statusAlert;
	IBuildingModel building;
	IFloorModel floor;
	ElevatorWrapperStub stub;
	MainGuiController mainGuiControler;
	List<IElevatorModel> elevators;
	UpdateData coreUpdater;
	
	@BeforeEach
	void setUp() throws Exception {
		statusAlert = new StatusAlert();
		building = new BuildingModel(); 
		floor = new FloorModel();
		stub = new ElevatorWrapperStub();
		mainGuiControler = new MainGuiController();
		elevators = new ArrayList<IElevatorModel>();
	}
	
	@Test
    public void testNoSqBuilding() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(null, stub, building, floor, elevators, mainGuiControler, statusAlert));

		//CreateEmptyElevators(stub.getElevatorNum());
    }
	
	@Test
    public void testNoSqElevator() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(stub, null, building, floor, elevators, mainGuiControler, statusAlert));

    }	
	
	@Test
    public void testNoBuilding() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(stub, stub, null, floor, elevators, mainGuiControler, statusAlert));

    }	
	
	@Test
    public void testNoFloor() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(stub, stub, building, null, elevators, mainGuiControler, statusAlert));
    }		
	
	@Test
    public void testNoElevators() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(stub, stub, building, floor, null, mainGuiControler, statusAlert));
    }			
	
	@Test
    public void testNoMainGuiControler() throws RemoteException {
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater = new UpdateData(stub, stub, building, floor, elevators, null, statusAlert));
    }			
	
	@Test
    public void testInitializeServicedFloorsWithTooMuchElevators() throws RemoteException {
		coreUpdater = new UpdateData(stub, stub, building, floor, elevators, mainGuiControler, statusAlert);
		
		// add one elevator to much to the list and initialize the serviced floors
		int nElevators = stub.getElevatorNum();
		nElevators++;
		for(int i = 0; i < nElevators; i++)
		{
			elevators.add(new ElevatorModel());
		}
		
		assertThrows(RuntimeException.class, () 
				-> coreUpdater.initializeServicedFloors());
	}			
	
	@Test
    public void testInitializeServicedFloors() throws RemoteException {
		coreUpdater = new UpdateData(stub, stub, building, floor, elevators, mainGuiControler, statusAlert);
		
		CreateEmptyElevators(stub.getElevatorNum());
		coreUpdater.initializeServicedFloors();
		
		assertEquals(stub.getElevatorNum(), coreUpdater.getElevators().size());
	}		
	
	/**
	 * Create empty elevators and add them to the list of elevators
	 * 
	 * @param nElevators - number of elevators, that should be created
	 * @throws RemoteException 
	 */
	void CreateEmptyElevators(int nElevators) throws RemoteException
	{
		for(int i = 0; i < nElevators; i++)
		{
			elevators.add(new ElevatorModel());
		}
		coreUpdater.initializeServicedFloors();

	}
	
}
