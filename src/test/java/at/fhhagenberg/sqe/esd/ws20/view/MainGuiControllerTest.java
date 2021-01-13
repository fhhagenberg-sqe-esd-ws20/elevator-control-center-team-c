//https://sormuras.github.io/blog/2018-09-11-testing-in-the-modular-world
//https://github.com/TestFX/TestFX/issues/638
//https://stackoverflow.com/questions/12598261/maven-build-debug-in-eclipse
//https://stackoverflow.com/questions/43636216/how-to-wait-until-element-is-visible-using-testfx
//https://stackoverflow.com/questions/45631046/in-testfx-version-4-how-can-i-wait-until-an-element-of-the-gui-becomes-visible
//https://stackoverflow.com/questions/20936101/get-listcell-via-listview

package at.fhhagenberg.sqe.esd.ws20.view;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.TimeoutException;

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
import org.testfx.matcher.control.TextInputControlMatchers;

import at.fhhagenberg.sqe.esd.ws20.model.AutoModeRandomAlgo;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import at.fhhagenberg.sqe.esd.ws20.others.TestUtils;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDirection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IElevatorWrapper.ElevatorDoorStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


/**
 * Unit test for the MainGuiController for the MainGui.fxml.
 * After change to the ui (every call to the update or reUpdate method) we have to wait till the ui finished updating its fields.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-09 02:12
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
class MainGuiControllerTest {
	
	@Mock
	private UpdateData mockedUpdater;
	@Mock
	private IBuildingModel mockedBuilding;
	//@Mock
	//private StatusAlert mockedStatusAlert;	//i don't know how to mock the beans property. So in all further tests a normal object will be used for StatusAlert.
	@Mock
	private AutoModeRandomAlgo mockedAutoModeAlgorithm;
	@Mock
	private IFloorModel mockedFloor;
	@Mock
	private IElevatorModel mockedElevator;
	
	
	private StatusAlert statusAlert = null; 
	private MainGuiController mainGuiController = null;
	private final static String uiDefaultLabelText = "...";
	private final static int uiUpdateWaitDelayMs = 200;
	private TestUtils testutils = null;
	
	
	/**
	 * Initialize and open gui.
	 * Gets executed before each test, works like BeforeEach.
	 * 
	 * @param stage
	 * @throws Exception
	 */
	@Start
	void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
		Parent root = loader.load();
		
		Scene scene = new Scene(root);
		stage.setTitle("Wielander Inc. Elevator Control Center | Team C");
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons8-elevator-96.png")));
		scene.getStylesheets().add("/modena_dark.css");
		stage.setScene(scene);
		stage.show();
		
		mainGuiController = (MainGuiController)loader.getController();
	}
	
	@BeforeEach
	void setUp() {
		statusAlert = new StatusAlert();
		testutils = new TestUtils(uiUpdateWaitDelayMs);
	}
	
	@Test
	void testRegisterNullExceptions() {
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
	void testUpdateNullExceptions() {
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.update(null, mockedElevator);
		});
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.update(mockedFloor, null);
		});
	}
	
	
	@Test
	void testHandlersBeforeRegister(FxRobot robot) {
		//no elements are in the elevator, nothing should happen as the checkbox is disabled by default
		robot.clickOn("#checkbox_manual_mode");
		Mockito.verifyNoMoreInteractions(mockedAutoModeAlgorithm);
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
		
		//no elements are in the elevator, nothing should happen as the checkbox is disabled by default and therefore the button never gets enabled
		robot.clickOn("#button_send_to_floor");
		Mockito.verifyNoMoreInteractions(mockedUpdater);
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testHandlersBeforeRegisterManualCall(FxRobot robot) {
		//this tests doesn't click the ui, but calls the handlers manually. This is to check if the null checks work.
		
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.checkboxManualAutomatic(new ActionEvent());
		});
		assertThrows(NullPointerException.class, () -> {
			mainGuiController.buttonSendToFloor(new ActionEvent());
		});
	}
	
	@Test
	void testUpdateBeforeRegister() {
		mainGuiController.update(mockedFloor, mockedElevator);
		
		//nothing should change from the default
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText(uiDefaultLabelText));
		Mockito.verifyNoMoreInteractions(mockedElevator);
		Mockito.verifyNoMoreInteractions(mockedFloor);
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.isEmpty());
	}
	
	@Test
	void testNoElevatorsRegister(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		//make sure the ui thread has enough time to update the ui
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "0", robot);
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.isEmpty());
		//ui should not change from default as no elevators are available
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText("0"));
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
	void testNoElevatorsUpdateAfterRegister(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		mainGuiController.update(mockedFloor, mockedElevator);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", uiDefaultLabelText, robot);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "0", robot);
		
		//nothing should change from the default
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText("0"));
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText(""));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText(uiDefaultLabelText));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText(uiDefaultLabelText));
		Mockito.verifyNoMoreInteractions(mockedElevator);
		Mockito.verifyNoMoreInteractions(mockedFloor);
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_stops", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_no_service", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_up", ListViewMatchers.isEmpty());
		FxAssert.verifyThat("#listview_calls_down", ListViewMatchers.isEmpty());
	}
	
	
	@Test
	void testElevatorsWithPropertiesButEmptyLists(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(0);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING);
		// as we don't set thenReturn, by default Mockito returns an empty collection for all lists
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		mainGuiController.update(mockedFloor, mockedElevator);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", "Closing", robot);
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasItems(2));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 1"));
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasListCell("Elevator 2"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("1"));
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
	void testElevatorsWithFilledLists(FxRobot robot) throws TimeoutException {
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
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		mainGuiController.update(mockedFloor, mockedElevator);
		testutils.waitUntilListviewHasCellText("#listview_calls_down", "Floor 8", robot);
		
		
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
	void testElevatorChangeAutomodeFunctionCalls(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_OPENING);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		
		mainGuiController.update(mockedFloor, mockedElevator);
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		mainGuiController.update(mockedFloor, mockedElevator);
		
		Mockito.verify(mockedAutoModeAlgorithm).checkIfInAutoMode(0);
		Mockito.verify(mockedAutoModeAlgorithm).checkIfInAutoMode(1);
		Mockito.verifyNoMoreInteractions(mockedAutoModeAlgorithm);
	}
	
	@Test
	void testSwitchElevatorsFunctionCalls(FxRobot robot) {
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
	void testSwitchElevatorsWithPropertiesChange(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedElevator.getTarget()).thenReturn(0, 0);
		Mockito.when(mockedElevator.getPosition()).thenReturn(1, 10);
		Mockito.when(mockedElevator.getDirection()).thenReturn(ElevatorDirection.ELEVATOR_DIRECTION_UNCOMMITTED, ElevatorDirection.ELEVATOR_DIRECTION_UP);
		Mockito.when(mockedElevator.getPayload()).thenReturn(3, 30);
		Mockito.when(mockedElevator.getSpeed()).thenReturn(4, 40);
		Mockito.when(mockedElevator.getDoors()).thenReturn(ElevatorDoorStatus.ELEVATOR_DOORS_CLOSING, ElevatorDoorStatus.ELEVATOR_DOORS_CLOSED);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		mainGuiController.update(mockedFloor, mockedElevator);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", "Closing", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 1"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("1"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("2"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Uncommitted"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("3"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("4"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closing"));
		
		
		robot.clickOn("#listview_elevators");
		robot.type(KeyCode.DOWN);
		mainGuiController.update(mockedFloor, mockedElevator);
		testutils.waitUntilLabelTextChangedTo("#label_doors_text", "Closed", robot);
		
		
		FxAssert.verifyThat("#listview_elevators", ListViewMatchers.hasSelectedRow("Elevator 2"));
		FxAssert.verifyThat("#label_target_text", LabeledMatchers.hasText("1"));
		FxAssert.verifyThat("#label_position_text", LabeledMatchers.hasText("11"));
		FxAssert.verifyThat("#label_direction_text", LabeledMatchers.hasText("Up"));
		FxAssert.verifyThat("#label_payload_text", LabeledMatchers.hasText("30"));
		FxAssert.verifyThat("#label_speed_text", LabeledMatchers.hasText("40"));
		FxAssert.verifyThat("#label_doors_text", LabeledMatchers.hasText("Closed"));
	}
	
	@Test
	void testDisplayedNumberOfFloors(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		testutils.waitUntilLabelTextChangedTo("#label_floors_text", "25", robot);
		
		FxAssert.verifyThat("#label_floors_text", LabeledMatchers.hasText("25"));
	}
	
	@Test
	void testDisplayedStatusMessage(FxRobot robot) throws TimeoutException {
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		statusAlert.Status.set("moinmoin");
		testutils.waitUntilLabelTextChangedTo("#label_status_text", "moinmoin", robot);
		
		FxAssert.verifyThat("#label_status_text", LabeledMatchers.hasText("moinmoin"));
	}
	
	
	@Test
	void testCheckboxDisabledOnNoElevators() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		//there is currently no extra Matchers for Checkboxes, so we use the general node one here.
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isDisabled());
	}
	
	@Test
	void testCheckboxEnabledOnMultipleElevators(FxRobot robot) throws TimeoutException {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		testutils.waitUntilListviewHasCellText("#listview_elevators", "Elevator 2", robot);
		
		FxAssert.verifyThat("#checkbox_manual_mode", NodeMatchers.isEnabled());
	}
	
	@Test
	void testCheckboxStateChangeOnElevatorChange(FxRobot robot) throws TimeoutException {
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
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testCheckboxSelectDeselectFunctionCalls(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		//enable checkbox
		robot.clickOn("#checkbox_manual_mode");
		Mockito.verify(mockedAutoModeAlgorithm).disable(0);
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
		
		//diable checkbox
		robot.clickOn("#checkbox_manual_mode");
		Mockito.verify(mockedAutoModeAlgorithm).enable(0);
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	
	@Test
	void testButtonDisabledOnNoElevators() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(0);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testButtonDisabledWithCheckboxNotChecked() {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isDisabled());
	}
	
	@Test
	void testButtonEnabledWithCheckboxChecked(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		
		FxAssert.verifyThat("#button_send_to_floor", NodeMatchers.isEnabled());
	}
	
	@Test
	void testButtonClickedFunctionCalls(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("5");
		robot.clickOn("#button_send_to_floor");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verify(mockedUpdater).setTarget(4);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
	}
	
	@Test
	void testButtonClickedEnteredFloorValidRegexFormatChecks(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("0");
		
		FxAssert.verifyThat("#textfield_floor_number", TextInputControlMatchers.hasText(""));
		
		robot.doubleClickOn("#textfield_floor_number").write("-");
		
		FxAssert.verifyThat("#textfield_floor_number", TextInputControlMatchers.hasText(""));
	}
	
	@Disabled
	@Test
	void testButtonClickedEnteredFloorOutsideBoundsLower(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("0");
		robot.clickOn("#button_send_to_floor");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
		testutils.verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
	}
	
	@Disabled
	@Test
	void testButtonClickedEnteredFloorOutsideBoundsUpper(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("30");
		robot.clickOn("#button_send_to_floor");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
		testutils.verifyAlertDialogHasHeader("Error");
		//robot.clickOn("OK");
		robot.type(KeyCode.ESCAPE);
	}
	
	@Disabled
	@Test
	void testButtonClickedEnteredFloorEmpty(FxRobot robot) {
		Mockito.when(mockedBuilding.getNumElevators()).thenReturn(2);
		Mockito.when(mockedBuilding.getNumFloors()).thenReturn(25);
		mainGuiController.register(mockedUpdater, mockedBuilding, statusAlert, mockedAutoModeAlgorithm);
		
		robot.clickOn("#checkbox_manual_mode");
		robot.doubleClickOn("#textfield_floor_number").write("");
		robot.clickOn("#button_send_to_floor");
		
		Mockito.verify(mockedUpdater).setSelectedElevator(0);
		Mockito.verifyNoMoreInteractions(mockedUpdater);
		testutils.verifyAlertDialogHasHeader("Error");
		robot.clickOn("OK");
	}
	
	
	//ToDo: Test reUpdate
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
