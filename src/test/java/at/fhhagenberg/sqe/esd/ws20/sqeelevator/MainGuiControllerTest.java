//https://sormuras.github.io/blog/2018-09-11-testing-in-the-modular-world
//https://github.com/TestFX/TestFX/issues/638
//https://stackoverflow.com/questions/12598261/maven-build-debug-in-eclipse

package at.fhhagenberg.sqe.esd.ws20.sqeelevator;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
import javafx.scene.image.Image;
import javafx.stage.Stage;


@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class MainGuiControllerTest {
	
	@Mock
	private UpdateData mockedUpdater;
	@Mock
	private IBuildingModel mockedBuilding;
	//@Mock
	//private StatusAlert mockedStatusAlert;	//i don't know how to mock the beans property. So in all further tests normal objects are used.
	@Mock
	private AutoModeSimpleAlgo mockedAutoModeAlgorithm;
	@Mock
	private IFloorModel mockedFloor;
	@Mock
	private IElevatorModel mockedElevator;
	
	
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
		
	}
	
	@Test
	public void testRegisterNullExceptions() {
		StatusAlert statusAlert = new StatusAlert();
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
	
	@Disabled
	@Test
	public void testFunctionCallBeforeRegister() {
		
	}
	
	@Test
	public void testNoElevators() {
		StatusAlert statusAlert = new StatusAlert();
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
		StatusAlert statusAlert = new StatusAlert();
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
		StatusAlert statusAlert = new StatusAlert();
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
	public void testSwitchElevators() {
		//nur schauen ob die Funktionsaufrufe passen beim Wechsel
	}
	
	
	@Test
	public void testSwitchElevatorsWithPropertiesWithFilledLists() {
		//nur schauen ob sich der Inhalt der Listen aktualisiert hat
	}
	

	
	
	
	//TODO tests
	//Anzahl der Floors in der gui richtig
	//StatusAlert neue Nachricht
	//ob das button aktivieren funktioniert
		//bei leerer uns bei gefüllter Liste
		//ob der aktuell ausgewählte elevator richtig übermnittelt wird
	//ob beim clicken des Buttons eh die Stockwerkgrenzen gcheckt werden
}
