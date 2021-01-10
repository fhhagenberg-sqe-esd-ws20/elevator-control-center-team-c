package at.fhhagenberg.sqe.esd.ws20.integration;

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
public class EndToEndTest {
	
	
	private Stage mainGuiStage;
	private final static String uiDefaultLabelText = "...";
	private final static int uiUpdateWaitDelayMs = 2000;
	private TestUtils testutils = null;
	
	
	@Mock
	private IElevator mockedElevators;
	
	/**
	 * Initialize and open gui.
	 * Gets executed before each test, works like BeforeEach.
	 * 
	 * @param stage
	 * @throws Exception
	 */
	@Start
	public void start(Stage stage) throws Exception {
		mainGuiStage = stage;
	}
	
	private void startGui(FxRobot robot) throws RemoteException, TimeoutException {
		Platform.runLater(new Runnable() { public void run() {
			new ElevatorControlCenter().setup(mainGuiStage, mockedElevators);
		}});
		testutils.waitUntilNodeIsVisible("#button_send_to_floor", robot);
	}
	
	
	@BeforeEach
	void setUp() {
		testutils = new TestUtils(uiUpdateWaitDelayMs);
	}
	
	
	@Test
	public void testNoElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", uiDefaultLabelText, robot);
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.isEmpty());
		//ui should not change from default as no elevators are available
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.isEmpty());
		//there should be an error message that the application does not work with no elevators
		//make sure this access to the ui runs in ui thread
		robot.interact(() -> {
			Label label = robot.lookup("#label_status_text").query();
			assertFalse(label.getText().isEmpty());
		});
	}
	
	@Test
	public void testElevatorsWithProperties(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getTarget(0)).thenReturn(1);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(2);
		Mockito.when(mockedElevators.getCommittedDirection(0)).thenReturn(1);	//ElevatorDirection.ELEVATOR_DIRECTION_DOWN
		Mockito.when(mockedElevators.getElevatorWeight(0)).thenReturn(4);
		Mockito.when(mockedElevators.getElevatorSpeed(0)).thenReturn(5);
		Mockito.when(mockedElevators.getElevatorDoorStatus(0)).thenReturn(2);	//ELEVATOR_DOORS_CLOSED
		
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", "Closed", robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 2"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Down"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("5"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closed"));
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText("Connected to Elevator"));
	}
	
	@Test
	public void testSwitchElevatorsWithPropertiesChange(FxRobot robot) throws RemoteException, TimeoutException {
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
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", "Open", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Up"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("5"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Open"));
		
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", "Closed", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("11"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("21"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Down"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("40"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("50"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closed"));
	}
	
	@Test
	public void testDisplayedNumberOfFloors(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "25", robot);
		
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText("25"));
	}
	
	
	@Test
	public void testCheckboxDisabledOnNoElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testCheckboxEnabledOnMultipleElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testCheckboxStateChangeOnElevatorChange(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		robot.clickOn("#checkbox_manual_mode");
		//testfx currently can't check if the checkbox is checked, there is no Matchers for checkboxes
		//so here we check if the button is enabled
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testButtonDisabledOnNoElevators(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui(robot);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "0", robot);	//wait for anything, just to delay and give the ui enough time to build
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testButtonEnabledWithCheckboxChecked(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testButtonDisabledWithCheckboxNotChecked(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		
		robot.clickOn("#checkbox_manual_mode");
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testInternalTargetGreaterNumFloors(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(30);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		
		robot.interact(() -> {
			Label label = robot.lookup("#label_status_text").query();
			assertFalse(label.getText().isEmpty());
		});
	}
	
	@Disabled("This test does not work in online Github build.")
	@Test
	public void testButtonClickedEnteredFloorOutsideBoundsLower(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#label_status_text", "Connected to Elevator", robot);
		
		//robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("0");
		robot.clickOn("#button_send_to_floor");
		
		testutils.verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText("Connected to Elevator"));
	}
	
	@Disabled("This test does not work in online Github build.")
	@Test
	public void testButtonClickedEnteredFloorOutsideBoundsUpper(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#label_status_text", "Connected to Elevator", robot);
		
		//robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("30");
		robot.clickOn("#button_send_to_floor");
		
		testutils.verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText("Connected to Elevator"));
	}
	
	@Disabled("This test does not work in online Github build.")
	@Test
	public void testButtonClickedEnteredFloorEmpty(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#label_status_text", "Connected to Elevator", robot);
		
		//robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("");
		robot.clickOn("#button_send_to_floor");
		
		testutils.verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText("Connected to Elevator"));
	}
	
	@Disabled("This does not work at 0.1 speed. The target gets reset back to original to fast.")
	@Test
	public void testSetTargetManualMode(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		testutils.waitUntilLabelTextChangedTo("#label_target_text", "1", robot);
		
		
		//robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("5");
		robot.clickOn("#button_send_to_floor");
		
		testutils.waitUntilLabelTextChangedTo("#label_target_text", "5", robot);
		
		Mockito.verify(mockedElevators).setTarget(0, 4);
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("5"));
	}
	
	
	
	
	
	
	@Test
	public void testElevatorListHasElementsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(3);
		
		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 3", robot);
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(3));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 2"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 3"));
	}	
	
	@Test
	public void testServicedFloorListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(3);
		// elevator1
		Mockito.when(mockedElevators.getServicesFloors(0, 0)).thenReturn(true);
		Mockito.when(mockedElevators.getServicesFloors(0, 1)).thenReturn(false);		
		Mockito.when(mockedElevators.getServicesFloors(0, 2)).thenReturn(true);
		//elevator2
		Mockito.when(mockedElevators.getServicesFloors(1, 0)).thenReturn(true);
		Mockito.when(mockedElevators.getServicesFloors(1, 1)).thenReturn(true);
		Mockito.when(mockedElevators.getServicesFloors(1, 2)).thenReturn(true);

		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_no_service", "Floor 2", robot);
		
		// elavator1
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 2"));
		// change view	
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		//elevator2
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasItems(3));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 2"));
	}	
	
	@Test
	public void testStopListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
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
		testutils.waitUntilListviewHasCellText("#listview_stops", "Floor 2", robot);
		
		
		// elavator1
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 2"));
		// change view	
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		//elevator2
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 1"));		
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 3"));
	}	
	
	@Test
	public void testStopListContainsCorrectItemsWhenChangingBetweenElevators(FxRobot robot) throws RemoteException, TimeoutException {
		
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
		testutils.waitUntilListviewHasCellText("#listview_stops", "Floor 2", robot);
		
		
		// elavator1
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 2"));
		// change view	
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		//elevator2
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 1"));		
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 3"));
	}		
	
	@Test
	public void testUpsListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonUp(0)).thenReturn(false);
		Mockito.when(mockedElevators.getFloorButtonUp(1)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(2)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonUp(3)).thenReturn(false);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_calls_up", "Floor 2", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 2"));
	}		
	
	@Test
	public void testDownsListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonDown(0)).thenReturn(false);
		Mockito.when(mockedElevators.getFloorButtonDown(1)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonDown(2)).thenReturn(true);
		Mockito.when(mockedElevators.getFloorButtonDown(3)).thenReturn(false);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_calls_down", "Floor 2", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 2"));
	}	
	
	@Test
	public void testUpsAndDownsListContainsCorrectItemsAfterStartup(FxRobot robot) throws RemoteException, TimeoutException {
		
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
		testutils.waitUntilListviewHasCellText("#listview_calls_down", "Floor 2", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));		
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 3"));		
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 2"));
		
		// change view	(values must also be set after changing to other view)
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));		
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 3"));		
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 2"));		
	}		
	
	@Test
	public void testStopListContainsCorrectItemsAfterUpdate(FxRobot robot) throws RemoteException, TimeoutException {
		
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
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		testutils.waitUntilListviewHasCellText("#listview_stops", "Floor 3", robot);
		
		// elavator1
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(4));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 2"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 3"));

		
		// change view	
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		
		//elevator2
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(0));
	}	
	
	@Test
	public void testUpsListContainsCorrectItemsAfterUpdate(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonUp(0)).thenReturn(false, true);
		Mockito.when(mockedElevators.getFloorButtonUp(1)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonUp(2)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonUp(3)).thenReturn(false, true);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_calls_up", "Floor 3", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 3"));
	}	
	
	@Test
	public void testDownsListContainsCorrectItemsAfterUpdate(FxRobot robot) throws RemoteException, TimeoutException {
		
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(4);

		Mockito.when(mockedElevators.getFloorButtonDown(0)).thenReturn(false, true);
		Mockito.when(mockedElevators.getFloorButtonDown(1)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonDown(2)).thenReturn(true, false);
		Mockito.when(mockedElevators.getFloorButtonDown(3)).thenReturn(false, true);


		startGui(robot);
		testutils.waitUntilListviewHasCellText("#listview_calls_down", "Floor 3", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 0"));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 3"));
	}	
	
	
	
	
	
	//TODO test connection and reconnect to simulator?
	

}
