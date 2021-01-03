//https://stackoverflow.com/questions/10751271/accessing-fxml-controller-class

package at.fhhagenberg.sqe.esd.ws20.view;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import at.fhhagenberg.sqe.esd.ws20.model.AutoModeSimpleAlgo;
import at.fhhagenberg.sqe.esd.ws20.model.BuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.ElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.FloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IBuildingModel;
import at.fhhagenberg.sqe.esd.ws20.model.IElevatorModel;
import at.fhhagenberg.sqe.esd.ws20.model.IFloorModel;
import at.fhhagenberg.sqe.esd.ws20.model.StatusAlert;
import at.fhhagenberg.sqe.esd.ws20.model.UpdateData;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.ElevatorWrapper;
import at.fhhagenberg.sqe.esd.ws20.sqeelevator.ElevatorWrapperStub;
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

	static MainGuiController mainGuiController;
	static Timer scheduler;
	static final int SCHEDULER_POLLING_INTERVAL_MS = 1000;
	
	/**
	 * Initializes and shows the gui.
	 * All controls are saved in the fxml file TODO and use localization.
	 */
	@Override
	public void start(Stage stage) {
		Parent root = null;
		FXMLLoader loader;
		try {
			//TODO Readd localization at the end of the project after all strings were defined and externalized
			loader = new FXMLLoader(getClass().getResource("/MainGui.fxml"));
			root = loader.load();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR, e.getLocalizedMessage());
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/icons8-elevator-24.png"));
			alert.showAndWait();
			return;
		}

		Scene scene = new Scene(root);
		stage.setTitle("Wielander Inc. Elevator Control Center | Team C");
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons8-elevator-24.png")));
		scene.getStylesheets().add("/modena_dark.css");
		stage.setScene(scene);
		stage.show();
		

		//save controller to access functions from it later
		mainGuiController = (MainGuiController)loader.getController();
		
		// Setup and connect objects, which are necessary for the MVC Pattern
		try {
			SetupMVC();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static void main(String[] args) {
        
        launch();
        // stop scheduler, when application is shutting down
        scheduler.cancel();

    }
    
    /**
     * Create models and Controller and connect them with each other
     * @throws RemoteException 
     */
    public void SetupMVC() throws RemoteException
    {
        // Creating models
        StatusAlert statusAlert = new StatusAlert();
        IBuildingModel building = new BuildingModel();
        IFloorModel floor = new FloorModel();
        //ElevatorWrapper sqelevator = new ElevatorWrapper(null);				//TODO: use real Simulator instead of Mock
        ElevatorWrapperStub stub = new ElevatorWrapperStub();
        AutoModeSimpleAlgo autoModeAlgorithm = new AutoModeSimpleAlgo(stub.getElevatorNum());	//TODO: use real Simulator instead of Mock
        
        
        // creating list for the elevators
        List<IElevatorModel> elevators = new ArrayList<IElevatorModel>();
        for(int i = 0; i < stub.getElevatorNum(); i++)							//TODO: use real simulator instead of stub
        {
        	elevators.add(new ElevatorModel());
        	//as all elevators start in automatic mode -> add all elevators to automode algorithm.
        	autoModeAlgorithm.enable(i);
        }
        
        

                
        // Create Scheduler
        scheduler = new Timer();


		// Create updater, which polls values from the elevator every 10ms
        UpdateData updater = new UpdateData(stub, stub, building, floor, elevators, mainGuiController, statusAlert); //TODO: use real Simulator instead of Mock
        
        // set service floors for each elevator
        updater.initializeServicedFloors();

        // give information about the models to the mainGuiController
        mainGuiController.register(updater, building, statusAlert, autoModeAlgorithm);
        
        // start task, which polls values from the elevator every SCHEDULER_POLLING_INTERVAL_MS
        scheduler.schedule(updater, 0, SCHEDULER_POLLING_INTERVAL_MS);
    }

}
