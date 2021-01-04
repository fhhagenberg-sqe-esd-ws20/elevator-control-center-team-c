package at.fhhagenberg.sqe.esd.ws20.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevator;
import at.fhhagenberg.sqe.esd.ws20.view.ElevatorControlCenter;
import javafx.application.Platform;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class EndToEndTest {
	
	
	private Stage mainGuiStage;
	private final static String uiDefaultLabelText = "...";
	private final static int uiUpdateWaitDelay = 2000;
	
	
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
	
	private void startGui() throws RemoteException {
		Platform.runLater(new Runnable() { public void run() {
			new ElevatorControlCenter().setup(mainGuiStage, mockedElevators);
		}});
		try { Thread.sleep(uiUpdateWaitDelay); } catch (InterruptedException e) { e.printStackTrace(); }	//make sure the ui thread has enough time to update the ui
	}
	
	
	@BeforeEach
	void setUp() {
	}
	
	
	@Test
	public void testNoElevators(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui();
		
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
	public void testElevatorsWithProperties(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getTarget(0)).thenReturn(1);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(2);
		Mockito.when(mockedElevators.getCommittedDirection(0)).thenReturn(1);	//ElevatorDirection.ELEVATOR_DIRECTION_DOWN
		Mockito.when(mockedElevators.getElevatorWeight(0)).thenReturn(4);
		Mockito.when(mockedElevators.getElevatorSpeed(0)).thenReturn(5);
		Mockito.when(mockedElevators.getElevatorDoorStatus(0)).thenReturn(2);	//ELEVATOR_DOORS_CLOSED
		startGui();
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("1"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Down"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("5"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closed"));
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(""));
	}
	
	@Test
	public void testSwitchElevatorsWithPropertiesChange(FxRobot robot) throws RemoteException {
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
		startGui();
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("1"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Up"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("5"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Open"));
		
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		//try { Thread.sleep(uiUpdateWaitDelay); } catch (InterruptedException e) { e.printStackTrace(); }
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("10"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("20"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Down"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("40"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("50"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closed"));
	}
	
	@Test
	public void testDisplayedNumberOfFloors() throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui();
		
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText("25"));
	}
	
	@Test
	public void testCheckboxDisabledOnNoElevators() throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui();
		
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testCheckboxEnabledOnMultipleElevators() throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui();
		
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testCheckboxStateChangeOnElevatorChange(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui();
		
		robot.clickOn("#checkbox_manual_mode");
		//testfx currently can't check if the checkbox is checked, there is no Matchers for checkboxes
		//so here we check if the button is enabled
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Disabled
	@Test
	public void testButtonDisabledOnNoElevators() throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(0);
		startGui();
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testButtonDisabledWithCheckboxNotChecked() throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui();
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testButtonEnabledWithCheckboxChecked(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		startGui();
		
		robot.clickOn("#checkbox_manual_mode");
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testInternalTargetGreaterNumFloors(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		Mockito.when(mockedElevators.getElevatorFloor(0)).thenReturn(30);
		startGui();
		
		robot.interact(() -> {
			Label label = robot.lookup("#label_status_text").query();
			assertFalse(label.getText().isEmpty());
		});
	}
	
	@Disabled
	@Test
	public void testButtonClickedEnteredFloorOutsideBoundsLower(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui();
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("0");
		robot.clickOn("#button_send_to_floor");
		
		verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(""));
	}
	
	@Disabled
	@Test
	public void testButtonClickedEnteredFloorOutsideBoundsUpper(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui();
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("30");
		robot.clickOn("#button_send_to_floor");
		
		verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(""));
	}
	
	@Disabled
	@Test
	public void testButtonClickedEnteredFloorEmpty(FxRobot robot) throws RemoteException {
		Mockito.when(mockedElevators.getElevatorNum()).thenReturn(2);
		Mockito.when(mockedElevators.getFloorNum()).thenReturn(25);
		startGui();
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("");
		robot.clickOn("#button_send_to_floor");
		
		verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(""));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Checks the current alert dialog displayed (on the top of the window stack)
	 * has the expected contents.
	 *
	 * From https://stackoverflow.com/a/48654878/8355496
	 * 
	 * @param expectedHeader  Expected header of the dialog
	 * @param expectedContent Expected content of the dialog
	 */
	private void verifyAlertDialogHasHeaderAndContent(final String expectedHeader, final String expectedContent) {
		final Stage actualAlertDialog = getTopModalStage();
		assertNotNull(actualAlertDialog);

		final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
		assertEquals(expectedHeader, dialogPane.getHeaderText());
		assertEquals(expectedContent, dialogPane.getContentText());
	}
	/**
	 * Checks the current alert dialog displayed (on the top of the window stack)
	 * has the expected contents.
	 *
	 * Adapted from https://stackoverflow.com/a/48654878/8355496
	 * 
	 * @param expectedHeader  Expected header of the dialog
	 */
	private void verifyAlertDialogHasHeader(final String expectedHeader) {
		final Stage actualAlertDialog = getTopModalStage();
		assertNotNull(actualAlertDialog);

		final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
		assertEquals(expectedHeader, dialogPane.getHeaderText());
	}

	/**
	 * Get the top modal window.
	 *
	 * Adapted from https://stackoverflow.com/a/48654878/8355496
	 * 
	 * @return the top modal window
	 */
	private Stage getTopModalStage() {
		// Get a list of windows but ordered from top[0] to bottom[n] ones.
		// It is needed to get the first found modal window.
		final List<Window> allWindows = new ArrayList<>(new FxRobot().robotContext().getWindowFinder().listWindows());
		Collections.reverse(allWindows);

		return (Stage) allWindows.stream().filter(window -> window instanceof Stage).findFirst().orElse(null);
	}
}
