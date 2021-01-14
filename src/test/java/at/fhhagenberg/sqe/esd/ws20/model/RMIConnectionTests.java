package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IBuildingWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class RMIConnectionTests {

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
	AutoMode AutoModeAlgo;
	@Mock
	StatusAlert StatusAlert;
	
	List<IElevatorModel> Elevators;
	
	UpdateData coreUpdater;

	
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
