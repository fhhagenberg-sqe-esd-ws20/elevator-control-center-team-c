package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import at.fhhagenberg.sqe.esd.ws20.model.BuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.FloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

@ExtendWith(MockitoExtension.class)



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
	
	
	
}
