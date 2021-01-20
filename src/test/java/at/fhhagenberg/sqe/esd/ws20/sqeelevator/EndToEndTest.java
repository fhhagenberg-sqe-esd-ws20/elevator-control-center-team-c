package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.ListViewMatchers;

import at.fhhagenberg.sqe.esd.ws20.others.TestUtils;
import at.fhhagenberg.sqe.esd.ws20.view.ElevatorControlCenter;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import sqelevator.IElevator;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * End to end test for the whole program. Only the elevator system gets mocked.
 * After every call to the update method we have to wait till the ui finished updating its fields.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @author Florian Atzenhofer (s1910567001)
 * @since 2021-01-09 02:12
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class EndToEndTest {
	
	
	private Stage mainGuiStage;
	private final static String uiDefaultLabelText = "...";
	private final static int uiUpdateWaitDelayMs = 2000;
	private TestUtils testutils = null;
	
	
	@Mock
	private IElevator mockedElevators;
	
	@Mock
	private IRMIConnection mockedRMI;
	
	/**
	 * Initialize and open gui.
	 * Gets executed before each test, works like BeforeEach.
	 * 
	 * @param stage
	 * @throws Exception
	 */
	@Start
	void start(Stage stage) throws Exception {
		mainGuiStage = stage;
	}
	
	private void startGui(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedRMI.getElevator()).thenReturn(mockedElevators);
		Platform.runLater(new Runnable() { public void run() {
			new ElevatorControlCenter().setup(mainGuiStage, mockedRMI);
		}});
		testutils.waitUntilNodeIsVisible("#buttonSendToFloor", robot);
	}
	
	
	@BeforeEach
	void setUp() {
		testutils = new TestUtils(uiUpdateWaitDelayMs);
	}
	
	
	@Test
	void testNoElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelDoorsText", uiDefaultLabelText, robot);
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.isEmpty());
		//ui should not change from default as no elevators are available
		FxAssert.verifyThat("#labelTargetText", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#labelPositionText", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#labelDirectionText", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#labelPayloadText", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#labelSpeedText", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#labelDoorsText", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.isEmpty());
		//there should be an error message that the application does not work with no elevators
		//make sure this access to the ui runs in ui thread
		robot.interact(() -> {
			Label label = robot.lookup("#labelStatusText").query();
			assertFalse(label.getText().isEmpty());
		});
	}
	
	@Test
	void testElevatorsWithProperties(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getTarget(0)).thenReturn(1);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(2);
		Mockito.when(mockedElevators.getCommittedDirection(0)).thenReturn(1);	//ElevatorDirection.ELEVATOR_DIRECTION_DOWN
		Mockito.when(mockedElevators.getElevatorWeight(0)).thenReturn(4);
		Mockito.when(mockedElevators.getElevatorSpeed(0)).thenReturn(5);
		Mockito.when(mockedElevators.getElevatorDoorStatus(0)).thenReturn(2);	//ELEVATOR_DOORS_CLOSED
		
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelDoorsText", "Closed", robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasListCell("Elevator 2"));
		FxAssert.verifyThat("#labelTargetText", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#labelPositionText", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#labelDirectionText", LabeledMatchers.hasText("Down"));
		FxAssert.verifyThat("#labelPayloadText", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#labelSpeedText", LabeledMatchers.hasText("5"));
		FxAssert.verifyThat("#labelDoorsText", LabeledMatchers.hasText("Closed"));
		FxAssert.verifyThat("#labelStatusText", LabeledMatchers.hasText("Connected to Elevator"));
	}
	
	@Test
	void testSwitchElevatorsWithPropertiesChange(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getTarget(0)).thenReturn(1);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(2);
		Mockito.when(mockedElevators.getCommittedDirection(0)).thenReturn(0);	//ELEVATOR_DIRECTION_UP
		Mockito.when(mockedElevators.getElevatorWeight(0)).thenReturn(4);
		Mockito.when(mockedElevators.getElevatorSpeed(0)).thenReturn(5);
		Mockito.when(mockedElevators.getElevatorDoorStatus(0)).thenReturn(1);	//ELEVATOR_DOORS_OPEN
		Mockito.when(mockedElevators.getTarget(1)).thenReturn(10);
		Mockito.when(mockedElevators.getElevatorFloor(1)).thenReturn(20);
		Mockito.when(mockedElevators.getCommittedDirection(1)).thenReturn(1);	//ElevatorDirection.ELEVATOR_DIRECTION_DOWN
		Mockito.when(mockedElevators.getElevatorWeight(1)).thenReturn(40);
		Mockito.when(mockedElevators.getElevatorSpeed(1)).thenReturn(50);
		Mockito.when(mockedElevators.getElevatorDoorStatus(1)).thenReturn(2);	//ELEVATOR_DOORS_CLOSED
		
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#labelDoorsText", "Open", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#labelTargetText", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#labelPositionText", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#labelDirectionText", LabeledMatchers.hasText("Up"));
		FxAssert.verifyThat("#labelPayloadText", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#labelSpeedText", LabeledMatchers.hasText("5"));
		FxAssert.verifyThat("#labelDoorsText", LabeledMatchers.hasText("Open"));
		
		
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		testutils.waitUntilLabelTextChangedTo("#labelDoorsText", "Closed", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#labelTargetText", LabeledMatchers.hasText("11"));
		FxAssert.verifyThat("#labelPositionText", LabeledMatchers.hasText("21"));
		FxAssert.verifyThat("#labelDirectionText", LabeledMatchers.hasText("Down"));
		FxAssert.verifyThat("#labelPayloadText", LabeledMatchers.hasText("40"));
		FxAssert.verifyThat("#labelSpeedText", LabeledMatchers.hasText("50"));
		FxAssert.verifyThat("#labelDoorsText", LabeledMatchers.hasText("Closed"));
	}
	
	@Test
	void testDisplayedNumberOfFloors(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "25", robot);
		
		FxAssert.verifyThat("#labelFloorsText", LabeledMatchers.hasText("25"));
	}
	
	
	@Test
	void testCheckboxDisabledOnNoElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		FxAssert.verifyThat("#checkboxManualMode", NodeMatchers.isDisabled());
	}
	
	@Test
	void testCheckboxEnabledOnMultipleElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		FxAssert.verifyThat("#checkboxManualMode", NodeMatchers.isEnabled());
	}
	
	@Test
	void testCheckboxStateChangeOnElevatorChange(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		robot.clickOn("#checkboxManualMode");
		//testfx currently can't check if the checkbox is checked, there is no Matchers for checkboxes
		//so here we check if the button is enabled
		FxAssert.verifyThat("#buttonSendToFloor", NodeMatchers.isEnabled());
		
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		
		FxAssert.verifyThat("#buttonSendToFloor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testButtonDisabledOnNoElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		FxAssert.verifyThat("#buttonSendToFloor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testButtonDisabledWithCheckboxNotChecked(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "0", robot);
		
		FxAssert.verifyThat("#buttonSendToFloor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testButtonEnabledWithCheckboxChecked(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "0", robot);
		
		robot.clickOn("#checkboxManualMode");
		testutils.waitUntilNodeIsEnabled("#buttonSendToFloor", robot);
		
		FxAssert.verifyThat("#buttonSendToFloor", NodeMatchers.isEnabled());
	}
	
	@Test
	void testInternalTargetGreaterNumFloors(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(30);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		
		robot.interact(() -> {
			Label label = robot.lookup("#labelStatusText").query();
			assertFalse(label.getText().isEmpty());
		});
	}
	
	
	@Disabled("This does not work at 0.1 speed. The target gets reset back to original to fast.")
	@Test
	void testSetTargetManualMode(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#labelTargetText", "1", robot);
		
		
		//robot.clickOn("#checkboxManualMode");
		robot.doubleClickOn("#textfieldFloorNumber").write("5");
		robot.clickOn("#buttonSendToFloor");
		
		testutils.waitUntilLabelTextChangedTo("#labelTargetText", "5", robot);
		
		Mockito.verify(mockedElevators).setTarget(0, 4);
		FxAssert.verifyThat("#labelTargetText", LabeledMatchers.hasText("5"));
	}
	
	
	
	
	
	
	@Test
	void testElevatorListHasElementsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(3);
		
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 3", robot);
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(3));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasListCell("Elevator 2"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasListCell("Elevator 3"));
	}
	
	@Test
	void testServicedFloorListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(3);
		// elevator1
		Mockito.when(mockedElevators.getServicesFloors(0, 0)).thenReturn(false);
		Mockito.when(mockedElevators.getServicesFloors(0, 1)).thenReturn(true);		
		Mockito.when(mockedElevators.getServicesFloors(0, 2)).thenReturn(false);
		//elevator2
		Mockito.when(mockedElevators.getServicesFloors(1, 0)).thenReturn(false);
		Mockito.when(mockedElevators.getServicesFloors(1, 1)).thenReturn(false);
		Mockito.when(mockedElevators.getServicesFloors(1, 2)).thenReturn(false);

		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "3", robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		testutils.waitUntilListviewHasCellText("#listviewNoService", "Floor 3", robot);
		
		// elavator1
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasListCell("Floor 3"));
		// change view
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		//elevator2
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasItems(3));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewNoService", ListViewMatchers.hasListCell("Floor 3"));
	}
	
	@Test
	void testStopListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getElevatorButton(0, 0)).thenReturn(false);
		Mockito.when(mockedElevators.getElevatorButton(0, 1)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(0, 2)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(0, 3)).thenReturn(false);
		
		Mockito.when(mockedElevators.getElevatorButton(1, 0)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(1, 1)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(1, 2)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(1, 3)).thenReturn(true);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewStops", "Floor 2", robot);
		
		
		// elavator1
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 3"));
		// change view	
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		//elevator2
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 2"));		
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 3"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 4"));
	}
	
	@Test
	void testStopListContainsCorrectItemsWhenChangingBetweenElevators(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getElevatorButton(0, 0)).thenReturn(false);
		Mockito.when(mockedElevators.getElevatorButton(0, 1)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(0, 2)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(0, 3)).thenReturn(false);
		
		Mockito.when(mockedElevators.getElevatorButton(1, 0)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(1, 1)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(1, 2)).thenReturn(true);
		Mockito.when(mockedElevators.getElevatorButton(1, 3)).thenReturn(true);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewStops", "Floor 2", robot);
		
		
		// elavator1
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 3"));
		// change view	
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		//elevator2
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 2"));		
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 3"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 4"));
	}
	
	@Test
	void testUpsListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonUp(0)).thenReturn(false);
		Mockito.when(mockedElevators.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(2)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(3)).thenReturn(false);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewCallsUp", "Floor 3", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 3"));
	}
	
	@Test
	void testDownsListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonDown(0)).thenReturn(false);
		Mockito.when(mockedElevators.getFloorButtonDown(1)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonDown(3)).thenReturn(false);


		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "4", robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		testutils.waitUntilListviewHasCellText("#listviewCallsDown", "Floor 3", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 3"));
	}
	
	@Test
	void testUpsAndDownsListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonUp(0)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(2)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(3)).thenReturn(true);		
		Mockito.when(mockedElevators.getFloorButtonDown(0)).thenReturn(false);
		Mockito.when(mockedElevators.getFloorButtonDown(1)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonDown(3)).thenReturn(false);


		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "4", robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		testutils.waitUntilListviewHasCellText("#listviewCallsDown", "Floor 3", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 3"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 4"));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 3"));
		
		// change view	(values must also be set after changing to other view)
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 3"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 4"));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 3"));
	}
	
	@Test
	void testStopListContainsCorrectItemsAfterUpdate(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getElevatorButton(0, 0)).thenReturn(false, true);
		Mockito.when(mockedElevators.getElevatorButton(0, 1)).thenReturn(true, true);
		Mockito.when(mockedElevators.getElevatorButton(0, 2)).thenReturn(true, true);
		Mockito.when(mockedElevators.getElevatorButton(0, 3)).thenReturn(false, true);
		
		Mockito.when(mockedElevators.getElevatorButton(1, 0)).thenReturn(true, false);
		Mockito.when(mockedElevators.getElevatorButton(1, 1)).thenReturn(true, false);
		Mockito.when(mockedElevators.getElevatorButton(1, 2)).thenReturn(true, false);
		Mockito.when(mockedElevators.getElevatorButton(1, 3)).thenReturn(true, false);


		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#labelFloorsText", "4", robot);
		testutils.waitUntilListviewHasCellText("#listviewElevators", "Elevator 2", robot);
		testutils.waitUntilListviewHasCellText("#listviewStops", "Floor 4", robot);
		
		// elavator1
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 3"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasListCell("Floor 4"));

		
		// change view	
		robot.clickOn("#listviewElevators");
		robot.type(KeyCode.DOWN);
		
		//elevator2
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listviewStops", ListViewMatchers.hasItems(0));
	}
	
	@Test
	void testUpsListContainsCorrectItemsAfterUpdate(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonUp(0)).thenReturn(false, true);
		Mockito.when(mockedElevators.getFloorButtonUp(1)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonUp(2)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonUp(3)).thenReturn(false, true);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewCallsUp", "Floor 4", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewCallsUp", ListViewMatchers.hasListCell("Floor 4"));
	}
	
	@Test
	void testDownsListContainsCorrectItemsAfterUpdate(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonDown(0)).thenReturn(false, true);
		Mockito.when(mockedElevators.getFloorButtonDown(1)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonDown(2)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonDown(3)).thenReturn(false, true);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listviewCallsDown", "Floor 4", robot);
		
		
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listviewElevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listviewCallsDown", ListViewMatchers.hasListCell("Floor 4"));
	}
	
	@Test
	void testConnectionStatusIsConnected(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		startGui(robot);
		
		testutils.waitUntilLabelTextChangedTo("#labelStatusText", "Connected to Elevator", robot);
		FxAssert.verifyThat("#labelStatusText", LabeledMatchers.hasText("Connected to Elevator"));
	}
	
	@Test
	void testOutOfSync(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getClockTick()).thenReturn((long)0, (long)1, (long)2, (long)3, (long)4, (long)5, (long)6, (long)7, 
				(long)8, (long)9, (long)10, (long)11, (long)12, (long)13, (long)14, (long)15, (long)16, (long)17, (long)19, (long)20, (long)21);

		startGui(robot);
		
		testutils.waitUntilLabelTextChangedTo("#labelStatusText", "Out of sync with the simulator. We are to slow with polling values from the Elevator Interface.", robot);
		
		FxAssert.verifyThat("#labelStatusText", LabeledMatchers.hasText("Out of sync with the simulator. We are to slow with polling values from the Elevator Interface."));
	}	
	
	@Test
	void testReconnectionAfterRemoteException(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);
		Mockito.when(mockedElevators.getElevatorSpeed(0)).thenThrow(new RemoteException());

		startGui(robot);
		
		testutils.waitUntilLabelTextChangedTo("#labelStatusText", "Connection lost to Elevator Simulator", robot);
		//FxAssert.verifyThat("#labelStatusText", LabeledMatchers.hasText("Connection lost to Elevator Simulator"));
	}
}
