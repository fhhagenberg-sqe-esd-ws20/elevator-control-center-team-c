package at.fhhagenberg.sqe.esd.ws20.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import at.fhhagenberg.sqe.esd.ws20.model.*;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.IRMIConnection;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.RMIConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class ElevatorControlCenter extends Application {

	MainGuiController mainGuiController;
	static Timer scheduler = new Timer();
	static final int SCHEDULER_POLLING_INTERVAL_MS = 50;
	
	/**
	 * Initializes rmi connection and sets up the gui.
	 */
	@Override
	public void start(Stage stage) {
		RMIConnection rmiConnection = new RMIConnection();
		setup(stage, rmiConnection);
	}
	
	/**
	 * Set up app structure and show gui. All controls are saved in the fxml file TODO and use localization.
	 * 
	 * @param stage the stage for the gui
	 * @param rmiConnection rmi connection to elevator simulator or mock object 
	 */
	public void setup(Stage stage, IRMIConnection rmiConnection) {
		Parent root = null;
		FXMLLoader loader;
		try {
			//TODO Readd localization at the end of the project after all strings were defined and externalized
			loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
			root = loader.load();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-96.png"));
			alert.showAndWait();
			return;
		}

		Scene scene = new Scene(root);
		stage.setTitle("Wielander Inc. Elevator Control Center | Team C");
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons8-elevator-96.png")));
		scene.getStylesheets().add("/modena_dark.css");
		stage.setScene(scene);
		stage.show();
		

		//save controller to access functions from it later
		mainGuiController = (MainGuiController)loader.getController();
		
		// Setup and connect objects, which are necessary for the MVC Pattern
		setupMVC(rmiConnection);
	}
	
	

	/**
	 * Entry point of the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch();
		// stop scheduler, when application is shutting down
		scheduler.cancel();
	}

	/**
	 * Create models and Controller and connect them with each other
	 * 
	 * @param rmiConnection rmi connection to elevator simulator or mock object
	 */
	public void setupMVC(IRMIConnection rmiConnection) {
		// creating all models
		StatusAlert statusAlert = new StatusAlert();
		IBuildingModel building = new BuildingModel();
		IFloorModel floor = new FloorModel();

		AutoMode autoModeAlgorithm = new AutoModeEveryFloor();

		// creating list for the elevators
		List<IElevatorModel> elevators = new ArrayList<>();

		// create updater, which polls values from the elevator every 10ms
		UpdateData updater = new UpdateData(building, floor, elevators, mainGuiController, statusAlert, autoModeAlgorithm, rmiConnection);

		// give information about the models to the mainGuiController
		mainGuiController.register(updater, building, statusAlert, autoModeAlgorithm);

		updater.reconnectRMI();

		// start task, which polls values from the elevator every
		scheduler.schedule(updater, 0, SCHEDULER_POLLING_INTERVAL_MS);
	}
}
