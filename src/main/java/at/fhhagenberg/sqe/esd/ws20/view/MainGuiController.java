package at.fhhagenberg.sqe.esd.ws20.view;

import java.net.URL;
import java.util.ResourceBundle;

import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Sample Skeleton for 'MainGui.fxml' Controller Class
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
    }
    
    public void startcontroller() {
        ObservableList<String>itemList = FXCollections.observableArrayList("item1","item2","item3");
        listview_elevators.setItems(itemList);
        
        System.out.println(itemList);
    }
    
    public void update() {
    	
    }
    
    public void registerModels(IBuildingModel buildingModel, IElevatorModel elevatorModel, IFloorModel floorModel) {
    	
    }
}
