//https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number

package at.fhhagenberg.sqe.esd.ws20.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.model.AutoModeSimpleAlgo;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


/**
 * Main Controller for the MainGui.fxml.
 * Also handles checkbox/button events.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-01 05:19
 */
public class MainGuiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="label_payload_text"
    private Label label_payload_text; // Value injected by FXMLLoader

    @FXML // fx:id="label_floors_text"
    private Label label_floors_text; // Value injected by FXMLLoader

    @FXML // fx:id="label_direction_text"
    private Label label_direction_text; // Value injected by FXMLLoader

    @FXML // fx:id="textfield_floor_number"
    private TextField textfield_floor_number; // Value injected by FXMLLoader

    @FXML // fx:id="label_status_text"
    private Label label_status_text; // Value injected by FXMLLoader

    @FXML // fx:id="listview_elevators"
    private ListView<String> listview_elevators; // Value injected by FXMLLoader

    @FXML // fx:id="label_target_text"
    private Label label_target_text; // Value injected by FXMLLoader

    @FXML // fx:id="listview_calls_down"
    private ListView<String> listview_calls_down; // Value injected by FXMLLoader

    @FXML // fx:id="label_speed_text"
    private Label label_speed_text; // Value injected by FXMLLoader

    @FXML // fx:id="listview_calls_up"
    private ListView<String> listview_calls_up; // Value injected by FXMLLoader

    @FXML // fx:id="listview_stops"
    private ListView<String> listview_stops; // Value injected by FXMLLoader

    @FXML // fx:id="listview_no_service"
    private ListView<String> listview_no_service; // Value injected by FXMLLoader

    @FXML // fx:id="label_doors_text"
    private Label label_doors_text; // Value injected by FXMLLoader

    @FXML // fx:id="button_send_to_floor"
    private Button button_send_to_floor; // Value injected by FXMLLoader

    @FXML // fx:id="label_position_text"
    private Label label_position_text; // Value injected by FXMLLoader

    @FXML // fx:id="checkbox_manual_mode"
    private CheckBox checkbox_manual_mode; // Value injected by FXMLLoader
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert label_payload_text != null : "fx:id=\"label_payload_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_floors_text != null : "fx:id=\"label_floors_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_direction_text != null : "fx:id=\"label_direction_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert textfield_floor_number != null : "fx:id=\"textfield_floor_number\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_status_text != null : "fx:id=\"label_status_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_elevators != null : "fx:id=\"listview_elevators\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_target_text != null : "fx:id=\"label_target_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_calls_down != null : "fx:id=\"listview_calls_down\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_speed_text != null : "fx:id=\"label_speed_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_calls_up != null : "fx:id=\"listview_calls_up\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_stops != null : "fx:id=\"listview_stops\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_no_service != null : "fx:id=\"listview_no_service\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_doors_text != null : "fx:id=\"label_doors_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert button_send_to_floor != null : "fx:id=\"button_send_to_floor\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_position_text != null : "fx:id=\"label_position_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert checkbox_manual_mode != null : "fx:id=\"checkbox_manual_mode\" was not injected: check your FXML file 'MainGui.fxml'.";
        
        setup();
    }
    
    @FXML
    void checkboxManualAutomatic(ActionEvent event) {
    	if (autoModeAlgo == null) {
    		throw new NullPointerException("MainGuiController.checkboxManualAutomatic() setDisable");
    	}
    	
    	//check checkbox state, if checked enable button, otherwise disable. The elevator can only be sent to a floor if in manual mode. 
    	if(checkbox_manual_mode.isSelected()) {
    		//disable the automatic mode -> enable manual mode
    		autoModeAlgo.disable(selectedElevator);
    		button_send_to_floor.setDisable(false);
    	}
    	else {
    		autoModeAlgo.enable(selectedElevator);
    		button_send_to_floor.setDisable(true);
    	}
    }
    
    @FXML
    void buttonSendToFloor(ActionEvent event) {
    	//get floor number from textfield
    	int floorNumber;
    	try {
    		floorNumber = Integer.parseInt(textfield_floor_number.getText());
    	} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-24.png"));
			alert.showAndWait();
			return;
		}
    	
    	//check if in range of available floors
    	if(floorNumber > numFloorsInBuilding) {
    		Alert alert = new Alert(AlertType.ERROR, "Entered floor number (" + floorNumber + ") exceeds number of floors in building (" + numFloorsInBuilding + ") !");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-24.png"));
			alert.showAndWait();
			return;
    	}
    	
    	//send to UpdateData and set as new floor
    	if(updateData == null) {
			throw new NullPointerException("MainGuiController.buttonSendToFloor()");
		}
    	updateData.setTarget(floorNumber);
    }
    
    
	private void setup() {
		//only allow integers without decimal separator in textfield
		DecimalFormat format = new DecimalFormat("#");
		format.setParseIntegerOnly(true);
		textfield_floor_number.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));
		
		//listen for selection changes on the elevator listview. Update the selected index.
		listview_elevators.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				selectedElevator = listview_elevators.getSelectionModel().getSelectedIndex();
				
				if(updateData == null) {
					throw new NullPointerException("MainGuiController.setup()");
				}
				//getSelectedIndex() returned -1 if no line is selected
				if(selectedElevator < 0) {
					return;
				}
				updateData.setSelectedElevator(selectedElevator);
			}
		});
	}
    
    
    private UpdateData updateData = null;
    private IBuildingModel iBuildingModel = null;
    private AutoModeSimpleAlgo autoModeAlgo = null;
    private Integer numFloorsInBuilding = 0;
    private int selectedElevator = -1;
    
    public void update(IFloorModel floor, IElevatorModel elevator) {
    	if(floor == null || elevator == null) {
    		throw new NullPointerException("MainGuiController.update()");
    	}
    	
    	//if there are no elevators, do nothing on update. Nothing should or can be updated
    	if(listview_elevators.getItems().isEmpty()) {
    		return;
    	}
    	
    	//get current selected elevator
    	//getSelectedIndex() returned -1 if no line is selected
    	if(selectedElevator < 0) {
    		throw new IllegalStateException("listview_elevators no line selected!");
    	}
    	//check if the selected elevator is in manual or automatic mode -> set checkbox and button to right state.
    	if(autoModeAlgo.checkIfInAutoMode(selectedElevator)) {
    		checkbox_manual_mode.setSelected(false);
    		button_send_to_floor.setDisable(true);
    	}
    	else {
    		checkbox_manual_mode.setSelected(true);
    		button_send_to_floor.setDisable(false);
    	}
    	
    	//update gui with new values from the given elevator
    	//elevator data
    	Platform.runLater(new Runnable() {
			public void run() {
				label_target_text.setText(elevator.getTarget().toString());
		    	label_position_text.setText(elevator.getPosition().toString());
		    	
		    	String direction = elevator.getDirection().toString();
		    	direction = direction.substring(direction.lastIndexOf('_') + 1);
		    	direction = direction.substring(0,1).toUpperCase() + direction.substring(1).toLowerCase();
		    	label_direction_text.setText(direction);
		    	
		    	label_payload_text.setText(elevator.getPayload().toString());
		    	label_speed_text.setText(elevator.getSpeed().toString());
		    	
		    	String doorsState = elevator.getDoors().toString();
		    	doorsState = doorsState.substring(doorsState.lastIndexOf('_') + 1); 	//get the last part of the enum, this contains the state.
		    	doorsState = doorsState.substring(0,1).toUpperCase() + doorsState.substring(1).toLowerCase();	//all to lower, except the first character
		    	label_doors_text.setText(doorsState);
			}
		});
    	
    	//stops
    	List<Integer> stops = elevator.getStopsList();
    	listview_stops.getItems().clear();
		if (stops == null) {
			throw new NullPointerException("MainGuiController.update() stops");
		}
		for (Integer e : stops) {
			listview_stops.getItems().add("Floor " + e);
		}
    	
		//not serviced floors
		List<Integer> serviceFloorsInteger = elevator.getIgnoredFloorsList();
		if (serviceFloorsInteger == null) {
			throw new NullPointerException("MainGuiController.register() serviceFloorsInteger");
		}
		for (Integer e : serviceFloorsInteger) {
			listview_no_service.getItems().add("Floor " + e);
		}
		
    	//calls
    	List<Integer> callsUp = floor.getUpButtonsList();
    	listview_calls_up.getItems().clear();
		if (callsUp == null) {
			throw new NullPointerException("MainGuiController.update() callsUp");
		}
		for (Integer e : callsUp) {
			listview_calls_up.getItems().add("Floor " + e);
		}
		
		List<Integer> callsDown = floor.getDownButtonsList();
		listview_calls_down.getItems().clear();
		if (callsDown == null) {
			throw new NullPointerException("MainGuiController.update() callsDown");
		}
		for (Integer e : callsDown) {
			listview_calls_down.getItems().add("Floor " + e);
		}
	}

	public void register(UpdateData updater, IBuildingModel building, StatusAlert statusAlert, AutoModeSimpleAlgo autoModeAlgorithm) {
		if(updater == null || building == null || statusAlert == null || autoModeAlgorithm == null) {
			throw new NullPointerException("MainGuiController.register()");
		}
		
		updateData = updater;
		iBuildingModel = building;
		autoModeAlgo = autoModeAlgorithm;
		
		//set/initialize elements that don't change anymore
		numFloorsInBuilding = iBuildingModel.getNumFloors();
		label_floors_text.setText(numFloorsInBuilding.toString());
		
		for(int i = 1; i < iBuildingModel.getNumElevators() + 1; ++i) {
		//for(int i = 1; i < 5 + 1; ++i) {
			listview_elevators.getItems().add("Elevator " + i);
		}
		//automatically select the first elevator. If the list is empty no item will be selected.
		listview_elevators.getFocusModel().focus(0);
		listview_elevators.getSelectionModel().select(0);
		
		//bind status so that gui always show the latest status automatically
		Platform.runLater(new Runnable() {
			public void run() {
				label_status_text.textProperty().bind(statusAlert.Status);
			}
		});
	}
}
