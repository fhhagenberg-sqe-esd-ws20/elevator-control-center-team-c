package at.fhhagenberg.sqe.esd.ws20.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
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

/**
 * Tests the rmi connection
 * 
 * @author Stefan Wohlrab (s1910567010)
 * @since 2021-01-16 00:40
 */
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
		coreUpdater.setConnection(true);
		coreUpdater.setSqs(MockedBuildingWrapper, MockedElevatorWrapper);
		coreUpdater.reconnectRMI();
		

		assertFalse(coreUpdater.getConnection());
	}
}
