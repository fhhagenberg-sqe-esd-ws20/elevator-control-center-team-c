package at.fhhagenberg.sqe.esd.ws20.model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Holds a property for showing alerts and exceptions in the gui
 * 
 * @author Lukas Ebenstein (s1910567015)
 * @since 2021-01-16 01:04
 */
public class StatusAlert {
	public StringProperty status = new SimpleStringProperty("");
	
	
	/**
	 * Updates the status text. 
	 * Does the same as directly calling the set method of the beans property. But this is Timer/ui thread safe.
	 * 
	 * @param newStatus
	 */
	public void setStatus(String newStatus) {
		Platform.runLater(() -> status.set(newStatus));
	}
}
