package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class RMIConnectionTests {

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
	IRMIConnection RMIConnection;
	@Mock
	List<IElevatorModel> MockedElevators;
	
	UpdateData coreUpdater;
	StatusAlert StatusAlert;
	private TestUtils testutils = null;
	private final static int uiUpdateWaitDelayMs = 100;

	
	@BeforeEach
	void setUp() throws Exception {
		StatusAlert = new StatusAlert();
		
		testutils = new TestUtils(uiUpdateWaitDelayMs);
	}
	
	@Test
	void testReconnectRMIFailureTest() throws RemoteException, InterruptedException, TimeoutException {		
		Mockito.when(MockedBuildingWrapper.getFloorNum()).thenReturn(4);
		
		coreUpdater = new UpdateData(MockedBuilding, Mockedfloor, MockedElevators, 
				MockedmainGuiControler, StatusAlert, AutoModeAlgo, RMIConnection);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.reconnectRMI();
		String expectedStatus = "No Elevator Connection";

		testutils.waitUntilStatusAlertHasStatus(expectedStatus, StatusAlert);
		assertEquals(expectedStatus, StatusAlert.status.get());
	}
	
	//ToDo: Test RMI connection
	//ToDo: SetRMIs, ReconnectRMI
	//TODO check if up-/down-lists were cleared at reconnect to simulator
	
}
