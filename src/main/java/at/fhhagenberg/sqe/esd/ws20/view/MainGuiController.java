//https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number

package at.fhhagenberg.sqe.esd.ws20.view;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;


/**
 * Main Controller for the MainGui.fxml.
 * Also handles checkbox/button events.
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2020-12-30 00:07
 */
public class MainGuiController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="label_payload_text"
    private Label label_payload_text; // Value injected by FXMLLoader

    @FXML // fx:id="label_direction_text"
    private Label label_direction_text; // Value injected by FXMLLoader

    @FXML // fx:id="textfield_floor_number"
    private TextField textfield_floor_number; // Value injected by FXMLLoader

    @FXML // fx:id="listview_stop_button"
    private ListView<?> listview_stop_button; // Value injected by FXMLLoader

    @FXML // fx:id="label_status_text"
    private Label label_status_text; // Value injected by FXMLLoader

    @FXML // fx:id="listview_elevators"
    private ListView<String> listview_elevators; // Value injected by FXMLLoader

    @FXML // fx:id="label_target_text"
    private Label label_target_text; // Value injected by FXMLLoader

    @FXML // fx:id="listview_calls_down"
    private ListView<?> listview_calls_down; // Value injected by FXMLLoader

    @FXML // fx:id="label_speed_text"
    private Label label_speed_text; // Value injected by FXMLLoader

    @FXML // fx:id="listview_calls_up"
    private ListView<?> listview_calls_up; // Value injected by FXMLLoader

    @FXML // fx:id="listview_no_service"
    private ListView<?> listview_no_service; // Value injected by FXMLLoader

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
        assert label_direction_text != null : "fx:id=\"label_direction_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert textfield_floor_number != null : "fx:id=\"textfield_floor_number\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_stop_button != null : "fx:id=\"listview_stop_button\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_status_text != null : "fx:id=\"label_status_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_elevators != null : "fx:id=\"listview_elevators\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_target_text != null : "fx:id=\"label_target_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_calls_down != null : "fx:id=\"listview_calls_down\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_speed_text != null : "fx:id=\"label_speed_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_calls_up != null : "fx:id=\"listview_calls_up\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert listview_no_service != null : "fx:id=\"listview_no_service\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_doors_text != null : "fx:id=\"label_doors_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert button_send_to_floor != null : "fx:id=\"button_send_to_floor\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert label_position_text != null : "fx:id=\"label_position_text\" was not injected: check your FXML file 'MainGui.fxml'.";
        assert checkbox_manual_mode != null : "fx:id=\"checkbox_manual_mode\" was not injected: check your FXML file 'MainGui.fxml'.";
        
        setup();
    }
    
    @FXML
    void checkboxManualAutomatic(ActionEvent event) {
    	//check checkbox state, if checked enable button, otherwise disable. The elevator can only be sent to a floor if in manual mode. 
    	if(checkbox_manual_mode.isSelected()) {
    		button_send_to_floor.setDisable(false);
    		//disable the automatic mode -> enable manual mode
    		//TODO 
    		//if autoMode != null
    		//autoMode.Disable(selectedElevator);
    	}
    	else {
    		button_send_to_floor.setDisable(true);
    		//TODO 
    		// if autoMode != null
    		//autoMode.Enable(selectedElevator);
    	}
    }
    
    @FXML
    void buttonSendToFloor(ActionEvent event) {
    	//get floor number from textfield
    	
    	//check if in range of available floors
    	
    	//send to UpdateData and set as new floor
    	if(updateData == null) {
			throw new NullPointerException("MainGuiController.buttonSendToFloor()");
		}
    	//updateData.setTarget(floorNumber);
    }
    
	private void setup() {
		//only allow integers in textfield
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
	}
    
    
    private UpdateData updateData = null;
    
    public void update(IBuildingModel building, IFloorModel floor, IElevatorModel elevator) {
    	if(building == null || floor == null || elevator == null) {
    		throw new NullPointerException("MainGuiController.update()");
    	}
    	
    	//update gui with new values from the selected elevator
    	//elevator data
    	
    	
    }

	public void register(UpdateData updater) {
		if(updater == null) {
			throw new NullPointerException("MainGuiController.register()");
		}
		
		updateData = updater;
	}
}
