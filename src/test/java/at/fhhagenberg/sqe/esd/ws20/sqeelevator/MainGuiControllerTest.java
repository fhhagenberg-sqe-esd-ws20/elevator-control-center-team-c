//https://sormuras.github.io/blog/2018-09-11-testing-in-the-modular-world
//https://github.com/TestFX/TestFX/issues/638
//https://stackoverflow.com/questions/12598261/maven-build-debug-in-eclipse

package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.ListViewMatchers;

import at.fhhagenberg.sqe.esd.ws20.model.AutoModeSimpleAlgo;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import at.fhhagenberg.sqe.esd.ws20.view.MainGuiController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;


@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class MainGuiControllerTest {
	
	@Mock
	private UpdateData mockedUpdater;
	@Mock
	private IBuildingModel mockedBuilding;
	//@Mock
	//private StatusAlert mockedStatusAlert;	//i don't know how to mock the beans property. So in all further tests a normal object will be used for StatusAlert.
	@Mock
	private AutoModeSimpleAlgo mockedAutoModeAlgorithm;
	@Mock
	private IFloorModel mockedFloor;
	@Mock
	private IElevatorModel mockedElevator;
	
	
	private StatusAlert statusAlert = null; 
	private MainGuiController mainGuiController = null;
	private String uiDefaultLabelText = "...";
	
	
	@Start
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
		Parent root = loader.load();
		
		Scene scene = new Scene(root);
		stage.setTitle("Wielander Inc. Elevator Control Center | Team C");
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons8-elevator-24.png")));
		scene.getStylesheets().add("/modena_dark.css");
		stage.setScene(scene);
		stage.show();
		
		mainGuiController = (MainGuiController)loader.getController();
	}
	
	@BeforeEach
	void setUp() {
		statusAlert = new StatusAlert();
	}
	
	@Test
	public void testRegisterNullExceptions() {
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);

		assertThrows(NullPointerException.class, () -> {
			mainGuiController.register(null, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		});
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.register(mockedUpdater, null, statusAlert, mockedAutoModeAlgorithm);

		});
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.register(mockedUpdater, mockedBuilding, null, mockedAutoModeAlgorithm);

		});
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, null);

		});
	}
	
	@Test
	public void testUpdateNullExceptions() {
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.update(null, mockedElevator);
		});
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.update(mockedFloor, null);
		});
	}
	
	
	@Test
	public void testFunctionCallBeforeRegister() {
		
	}
	
	@Test
	public void testNoElevators() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }	//make sure the ui thread has enough time to update the ui
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.isEmpty());
		
		//ui should not change from default as no elevators are available
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(""));
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
	}
	
	@Test
	public void testElevatorsWithPropertiesButEmptyLists() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING);
		// as we don't set thenReturn, by default Mockito returns an empty collection for all lists

		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		mainGuiController.update(mockedFloor, mockedElevator);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }

		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 2"));

		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("0"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("1"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Uncommitted"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closing"));

		FxAssert.verifyThat("#listview_stops", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.isEmpty());
	}
	
	@Test
	public void testElevatorsWithFilledLists() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPENING);
		
		Mockito.when(mockedElevator.getStopsList()).thenReturn(List.of(1, 2));
		Mockito.when(mockedElevator.getIgnoredFloorsList()).thenReturn(List.of(3, 4));
		Mockito.when(mockedFloor.getUpButtonsList()).thenReturn(List.of(5, 6));
		Mockito.when(mockedFloor.getDownButtonsList()).thenReturn(List.of(7, 8));
		
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		mainGuiController.update(mockedFloor, mockedElevator);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		
		
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 1"));
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.hasListCell("Floor 2"));
		
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 3"));
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.hasListCell("Floor 4"));

		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 5"));
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.hasListCell("Floor 6"));
		
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 7"));
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.hasListCell("Floor 8"));
	}
	
	@Test
	public void testSwitchElevatorsFunctionCalls(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPENING);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		mainGuiController.update(mockedFloor, mockedElevator);
		
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verify(mockedUpdater).setSelectedElevator(1);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
	}
	
	@Test
	public void testSwitchElevatorsWithPropertiesChange(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0, 0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1, 10);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED, ElevatorDirection.ELEVATOR_DIRECTION_UP);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3, 30);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4, 40);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING, ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		mainGuiController.update(mockedFloor, mockedElevator);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("0"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("1"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Uncommitted"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closing"));
		
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		mainGuiController.update(mockedFloor, mockedElevator);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("0"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("10"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Up"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("30"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("40"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closed"));
	}
	
	@Test
	public void testDisplayedNumberOfFloors() {
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText("25"));
	}
	
	@Test
	public void testDisplayedStatusMessage() {
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		statusAlert.Status.set("moinmoin");
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText("moinmoin"));
	}
	
	
	@Test
	public void testCheckboxDisabledOnNoElevators() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		//there is currently no extra Matchers for Checkboxes, so we use the general node one here.
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testCheckboxEnabledOnMultipleElevators() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testCheckboxStateChangeOnElevatorChange(FxRobot robot) {
		Mockito.when(mockedAutoModeAlgorithm.checkIfInAutoMode(1)).thenReturn(true);
		
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPENING);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		
		robot.clickOn("#checkbox_manual_mode");
		//testfx currently can't check if the checkbox is checked, there is no Matchers for checkboxes
		//so here we check if the button is enabled
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		mainGuiController.update(mockedFloor, mockedElevator);
		try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testCheckboxAutomodeFunctionCalls(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPENING);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		mainGuiController.update(mockedFloor, mockedElevator);
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		mainGuiController.update(mockedFloor, mockedElevator);
		
		Mockito.verify(mockedAutoModeAlgorithm).checkIfInAutoMode(0);
		Mockito.verify(mockedAutoModeAlgorithm).checkIfInAutoMode(1);
		Mockito.verifyNoMoreInteractions(mockedAutoModeAlgorithm);
	}
	
	@Test
	public void testButtonDisabledOnNoElevators() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testButtonDisabledWithCheckboxNotChecked() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	public void testButtonEnabledWithCheckboxChecked(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
	}
	
	@Test
	public void testButtonClickedFunctionCalls(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		//System.out.println("After click on checkbox");
		robot.doubleClickOn("#textfield_floor_number").write("5");
		//System.out.println("After textbox enter floor");
		robot.clickOn("#button_send_to_floor");
		//System.out.println("After click on button");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verify(mockedUpdater).setTarget(5);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
	}
	
	@Disabled
	@Test
	public void testButtonClickedEnteredFloorInsideBoundsLower(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("0");
		robot.clickOn("#button_send_to_floor");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
		verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
	}
	
	@Disabled
	@Test
	public void testButtonClickedEnteredFloorInsideBoundsUpper(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("30");
		robot.clickOn("#button_send_to_floor");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
		verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
		//robot.type(KeyCode.ESCAPE);
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
