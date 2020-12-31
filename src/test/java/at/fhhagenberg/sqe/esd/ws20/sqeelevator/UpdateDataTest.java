package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

@ExtendWith(MockitoExtension.class)

//TODO: Tests ueberlegen und erstellen

public class UpdateDataTest {

	@Mock
	StatusAlert statusAlert;
	IBuildingModel building;
	IFloorModel floor;
	ElevatorWrapperStub stub;
	MainGuiController mainGuiController;
	List<IElevatorModel> elevators;
	UpdateData coreUpdater;
	
	@BeforeEach
	void setUp() throws Exception {
		statusAlert = new StatusAlert();
		building = new BuildingModel(); 
		floor = new FloorModel();
		stub = new ElevatorWrapperStub();
		mainGuiController = new MainGuiController();
		elevators = new ArrayList<IElevatorModel>();
		coreUpdater = new UpdateData(stub, stub, building, floor, elevators, mainGuiController, statusAlert);
	}
	
	@Test
    public void testDummy() {
		CreateEmptyElevators(2);
		assertEquals(1, 1);
    }
	
	/**
	 * Create empty elevators and add them to the list of elevators
	 * 
	 * @param nElevators - number of elevators, that should be created
	 */
	void CreateEmptyElevators(int nElevators)
	{
		for(int i = 0; i < nElevators; i++)
		{
			elevators.add(new ElevatorModel());
		}
	}
	
}
