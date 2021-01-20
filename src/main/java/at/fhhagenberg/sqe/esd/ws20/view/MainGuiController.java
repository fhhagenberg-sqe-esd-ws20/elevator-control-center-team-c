package at.fhhagenberg.sqe.esd.ws20.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import at.fhhagenberg.sqe.esd.ws20.model.AutoMode;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * The register method has to be called before any interaction with the gui is possible!
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-16 01:53
 */
public class MainGuiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="labelPayloadText"
    private Label labelPayloadText; // Value injected by FXMLLoader

    @FXML // fx:id="labelFloorsText"
    private Label labelFloorsText; // Value injected by FXMLLoader

    @FXML // fx:id="labelDirectionText"
    private Label labelDirectionText; // Value injected by FXMLLoader

    @FXML // fx:id="textfieldFloorNumber"
    private TextField textfieldFloorNumber; // Value injected by FXMLLoader

    @FXML // fx:id="labelStatusText"
    private Label labelStatusText; // Value injected by FXMLLoader

    @FXML // fx:id="listviewElevators"
    private ListView<String> listviewElevators; // Value injected by FXMLLoader

    @FXML // fx:id="labelTargetText"
    private Label labelTargetText; // Value injected by FXMLLoader

    @FXML // fx:id="listviewCallsDown"
    private ListView<String> listviewCallsDown; // Value injected by FXMLLoader

    @FXML // fx:id="labelSpeedText"
    private Label labelSpeedText; // Value injected by FXMLLoader

    @FXML // fx:id="listviewCallsUp"
    private ListView<String> listviewCallsUp; // Value injected by FXMLLoader

    @FXML // fx:id="listviewStops"
    private ListView<String> listviewStops; // Value injected by FXMLLoader

    @FXML // fx:id="listviewNoService"
    private ListView<String> listviewNoService; // Value injected by FXMLLoader

    @FXML // fx:id="labelDoorsText"
    private Label labelDoorsText; // Value injected by FXMLLoader

    @FXML // fx:id="buttonSendToFloor"
    private Button buttonSendToFloor; // Value injected by FXMLLoader

    @FXML // fx:id="labelPositionText"
    private Label labelPositionText; // Value injected by FXMLLoader

    @FXML // fx:id="checkboxManualMode"
    private CheckBox checkboxManualMode; // Value injected by FXMLLoader
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert labelPayloadText != null : "fx:id=\"labelPayloadText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelFloorsText != null : "fx:id=\"labelFloorsText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelDirectionText != null : "fx:id=\"labelDirectionText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert textfieldFloorNumber != null : "fx:id=\"textfieldFloorNumber\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelStatusText != null : "fx:id=\"labelStatusText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listviewElevators != null : "fx:id=\"listviewElevators\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelTargetText != null : "fx:id=\"labelTargetText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listviewCallsDown != null : "fx:id=\"listviewCallsDown\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelSpeedText != null : "fx:id=\"labelSpeedText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listviewCallsUp != null : "fx:id=\"listviewCallsUp\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listviewStops != null : "fx:id=\"listviewStops\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listviewNoService != null : "fx:id=\"listviewNoService\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelDoorsText != null : "fx:id=\"labelDoorsText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert buttonSendToFloor != null : "fx:id=\"buttonSendToFloor\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert labelPositionText != null : "fx:id=\"labelPositionText\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert checkboxManualMode != null : "fx:id=\"checkboxManualMode\" was not injected: check your FXML file 'MainGui.fxml'.";
        
        //my own initialization after the one from fxml loader are finished
        setup();
    }
    
    /**
     * Checkbox handler that gets called on interaction with the checkbox
     * 
     * @param event
     */
    @FXML
    void checkboxManualAutomatic(ActionEvent event) {
    	if (autoModeAlgo == null) {
    		throw new NullPointerException("MainGuiController.checkboxManualAutomatic() autoModeAlgo == null");
    	}
    	
    	//check checkbox state, if checked enable button, otherwise disable. The elevator can only be sent to a floor if in manual mode. 
    	if(checkboxManualMode.isSelected()) {
    		//disable the automatic mode -> enable manual mode
    		autoModeAlgo.disable(selectedElevator);
    		buttonSendToFloor.setDisable(false);
    	}
    	else {
    		autoModeAlgo.enable(selectedElevator);
    		buttonSendToFloor.setDisable(true);
    	}
    }
    
    /**
     * Button handler that gets called on interaction with the button
     * 
     * @param event
     */
    @FXML
    void buttonSendToFloor(ActionEvent event) {
    	if(updateData == null) {
    		throw new NullPointerException("MainGuiController.buttonSendToFloor() NullPointerException");
    	}
    	
    	//get floor number from textfield
    	int floorNumber;
    	try {
    		floorNumber = Integer.parseInt(textfieldFloorNumber.getText());
    	} catch (NumberFormatException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-96.png"));
			alert.showAndWait();
			return;
		}
    	
    	//check if in range of available floors
    	if(floorNumber > numFloorsInBuilding) {
    		Alert alert = new Alert(AlertType.ERROR, "The entered floor number (" + floorNumber + ") exceeds number of floors in building (" + numFloorsInBuilding + ")!");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-96.png"));
			alert.showAndWait();
			return;
    	}
    	if(floorNumber <= 0) {
    		//this should never happen, the textformatter of the textfield should not allow input that is <=0
    		throw new NumberFormatException("MainGuiController.buttonSendToFloor() floorNumber<=0");
    	}
    	//check if target floor is an ignored one -> don't move the elevator to this ignored floor
    	if(updateData.getIgnoredFloorsFromSelectedElevator().contains(floorNumber-1)) {
    		Alert alert = new Alert(AlertType.ERROR, "The entered floor number (" + floorNumber + ") is not serviced!");
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-96.png"));
			alert.showAndWait();
    		return;
    	}
    	
    	//send to UpdateData and set as new floor
    	//internal the floors are in range 0 to numFloorsInBuilding-1. But the user enters 1 to numFloorsInBuilding. To be have to subtract 1 here to convert the ranges.
    	updateData.setTarget(floorNumber-1);
    }
    
    
	/**
	 * Sets up a formatter for the textfield and registers a listener for the elevator listview
	 */
	private void setup() {
		//set which chars are allowed in the textfield
		textfieldFloorNumber.setTextFormatter(new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			//only positive integers without leading sing
			if (newText.matches("([1-9][0-9]*)?")) {
				return change;
			}
			return null;
		}));
		
		//listen for selection changes on the elevator listview. Update the selected index.
		listviewElevators.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
				selectedElevator = listviewElevators.getSelectionModel().getSelectedIndex();
				
				if(updateData == null) {
					throw new NullPointerException("MainGuiController.setup() NullPointerException");
				}
				//getSelectedIndex() returns -1 if no line is selected
				if(selectedElevator < 0) {
					return;
				}
				updateData.setSelectedElevator(selectedElevator);
			}
		});
	}
	
	
	private UpdateData updateData = null;
	private IBuildingModel buildingModel = null;
	private AutoMode autoModeAlgo = null;
	private Integer numFloorsInBuilding = 0;
	private int selectedElevator = -1;
	
	
	/**
	 * Updates the gui with information from the provided objects
	 * 
	 * @param floor		All information about the floors in the building
	 * @param elevator	The informatino about a elevator that should be displayed in the gui
	 */
	public void update(IFloorModel floor, IElevatorModel elevator) {
		if(floor == null || elevator == null) {
			throw new NullPointerException("MainGuiController.update() NullPointerException");
		}
		
		//if there are no elevators, do nothing on update. Nothing should or can be updated
		if(listviewElevators.getItems().isEmpty()) {
			return;
		}
		
		//get current selected elevator
		//getSelectedIndex() returned -1 if no line is selected
		if(selectedElevator < 0) {
			throw new IllegalStateException("MainGuiController.update() listviewElevators no line selected!");
		}
		//check if the selected elevator is in manual or automatic mode -> set checkbox and button to right state.
		if(autoModeAlgo.checkIfInAutoMode(selectedElevator)) {
			checkboxManualMode.setSelected(false);
			buttonSendToFloor.setDisable(true);
		}
		else {
			checkboxManualMode.setSelected(true);
			buttonSendToFloor.setDisable(false);
		}
		
		//update gui with new values from the given elevator
		//elevator data
		Platform.runLater(() -> {
			Integer targetReranged = elevator.getTarget() + 1;
			labelTargetText.setText(targetReranged.toString());
			Integer positionReranged = elevator.getPosition() + 1;
			labelPositionText.setText(positionReranged.toString());
			
			String direction = elevator.getDirection().toString();
			direction = direction.substring(direction.lastIndexOf('_') + 1);
			direction = direction.substring(0,1).toUpperCase() + direction.substring(1).toLowerCase();
			labelDirectionText.setText(direction);
			
			labelPayloadText.setText(elevator.getPayload().toString());
			Integer speedAbs = Math.abs(elevator.getSpeed());
			labelSpeedText.setText(speedAbs.toString());
			
			String doorsState = elevator.getDoors().toString();
			doorsState = doorsState.substring(doorsState.lastIndexOf('_') + 1); 	//get the last part of the enum, this contains the state.
			doorsState = doorsState.substring(0,1).toUpperCase() + doorsState.substring(1).toLowerCase();	//all to lower, except the first character
			labelDoorsText.setText(doorsState);
		});
		
		String floorListViewPrefix = "Floor ";
		//stops
		List<Integer> stops = elevator.getStopsList();
		if (stops == null) {
			throw new NullPointerException("MainGuiController.update() stops");
		}
		ObservableList<String> stopsOl = FXCollections.observableArrayList();
		for (Integer e : stops) {
			stopsOl.add(floorListViewPrefix + (e+1));
		}
		Platform.runLater(() -> listviewStops.getItems().setAll(stopsOl));
		
		//not serviced floors
		List<Integer> ignoredFloors = elevator.getIgnoredFloorsList();
		if (ignoredFloors == null) {
			throw new NullPointerException("MainGuiController.register() ignoredFloors");
		}
		ObservableList<String> ignoredFloorsOl = FXCollections.observableArrayList();
		for (Integer e : ignoredFloors) {
			ignoredFloorsOl.add(floorListViewPrefix + (e+1));
		}
		Platform.runLater(() -> listviewNoService.getItems().setAll(ignoredFloorsOl));
		
		//calls
		List<Integer> callsUp = floor.getUpButtonsList();
		if (callsUp == null) {
			throw new NullPointerException("MainGuiController.update() callsUp");
		}
		ObservableList<String> callsUpOl = FXCollections.observableArrayList();
		for (Integer e : callsUp) {
			callsUpOl.add(floorListViewPrefix + (e+1));
		}
		Platform.runLater(() -> listviewCallsUp.getItems().setAll(callsUpOl));
		
		List<Integer> callsDown = floor.getDownButtonsList();
		if (callsDown == null) {
			throw new NullPointerException("MainGuiController.update() callsDown");
		}
		ObservableList<String> callsDownOl = FXCollections.observableArrayList();
		for (Integer e : callsDown) {
			callsDownOl.add(floorListViewPrefix + (e+1));
		}
		Platform.runLater(() -> listviewCallsDown.getItems().setAll(callsDownOl));
	}
	
	
	/**
	 * Register needed objects in the controller that don't change any more. See update(...) for updates with objects that change during operation.
	 * Must be called before interacting with any other function in this module once!
	 * 
	 * @param updater				Interaction on selection changes from the user in the gui
	 * @param building				Information about the building that should be displayed
	 * @param statusAlert			Binding to new status messages
	 * @param autoModeAlgorithm		Enable/disable elevators for automatic/manual mode
	 */
	public void register(UpdateData updater, IBuildingModel building, StatusAlert statusAlert, AutoMode autoModeAlgorithm) {
		if(updater == null || building == null || statusAlert == null || autoModeAlgorithm == null) {
			throw new NullPointerException("MainGuiController.register() NullPointerException");
		}
		
		updateData = updater;
		buildingModel = building;
		autoModeAlgo = autoModeAlgorithm;
		
		//set/initialize elements that don't change anymore after a connection to the rmi
		numFloorsInBuilding = buildingModel.getNumFloors();
		Platform.runLater(() -> labelFloorsText.setText(numFloorsInBuilding.toString()));
		
		
		clearAndFillElevatorListView();
		
		
		//bind status so that gui always show the latest status automatically
		Platform.runLater(() -> labelStatusText.textProperty().bind(statusAlert.status));
	}
	
	
	/**
	 * Update static data that was registered by register(...). See update(...) for updates with objects that change with every tick during operation.
	 */
	public void reUpdate() {
		numFloorsInBuilding = buildingModel.getNumFloors();
		Platform.runLater(() -> labelFloorsText.setText(numFloorsInBuilding.toString()));
		
		clearAndFillElevatorListView();
	}
	
	/**
	 * clear and fill the elevator listview with new data
	 */
	private void clearAndFillElevatorListView() {
		ObservableList<String> elevatorsOl = FXCollections.observableArrayList();
		int numElevators = buildingModel.getNumElevators();
		for(int i = 1; i < numElevators + 1; ++i) {
			elevatorsOl.add("Elevator " + i);
		}
		Platform.runLater(() -> {
			listviewElevators.getItems().setAll(elevatorsOl);
			//automatically select the first elevator. If the list is empty no item will be selected.
			listviewElevators.getFocusModel().focus(0);
			listviewElevators.getSelectionModel().select(0);
			//enable the default disabled checkbox if elevators are in the list/building
			if(!listviewElevators.getItems().isEmpty()) {
				checkboxManualMode.setDisable(false);
			}
		});
	}
}
