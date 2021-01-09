package at.fhhagenberg.sqe.esd.ws20.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatusAlert {
	public StringProperty Status = new SimpleStringProperty("");
	
	
	/**
	 * Updates the Status text. 
	 * Does the same as directly calling the set method of the beans property. But this is Timer/ui thread safe.
	 * 
	 * @param newStatus
	 */
	public void setStatus(String newStatus) {
		Platform.runLater(() -> {
			Status.set(newStatus);
		});
	}
	
	//TODO when to clear error status? After the next successful rmi access?
	//TODO how to show if the status is current or old? Add a timestamp?
}
